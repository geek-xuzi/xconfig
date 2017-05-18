package com.xuen.xconfig.admin.service;

import com.google.common.base.Preconditions;
import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.admin.dao.ConfigDao;
import com.xuen.xconfig.anno.XValue;
import com.xuen.xconfig.core.ZookeeperFactoryBean;
import com.xuen.xconfig.module.Config;
import com.xuen.xconfig.module.ConfigType;
import com.xuen.xconfig.redis.RedisClient;
import com.xuen.xconfig.util.ZkUtils;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
@Service
public class XServiceImpl implements XService {

    @XValue("xuen")
    private int s = 5;

    @XValue("sadas")
    public int n = 5;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XServiceImpl.class);

    @Resource
    private ZookeeperFactoryBean zookeeperFactoryBean;

    @Resource
    private RedisClient redisClient;

    @Autowired
    private ConfigDao configDao;


    public APIResult updateConf(String path, String value) throws Exception {
        Preconditions.checkArgument(!StringUtils.isEmpty(path), "path 不能为空");
        CuratorFramework zkClient = zookeeperFactoryBean.getZkClient();
        try {
            // TODO: 17-5-16 持久化
            redisClient.set(path, value);
            zkClient.setData().forPath(path, value.getBytes("utf-8"));
            return new APIResult(1, "配置更改成功", value);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public APIResult createConf(Config config) {
        Preconditions.checkArgument(config.getConfigType() != null, "configStatus不能是空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(config.getAppName()), "appName不能是空");
        Preconditions
                .checkArgument(StringUtils.isNotEmpty(config.getConfigName()), "configName不能是空");
        // 1 异步存redis
        insertRedis(config);
        // 2 异步存zk
        try {
            insertZk(config);

        } catch (Exception e) {
            return new APIResult(0, "创建失败");

        }
        return new APIResult(1, "创建成功" +s);
    }

    private void insertZk(Config config) {
        String appName = config.getAppName();
        String configName = config.getConfigName();
        String configData = config.getConfigData();
        ConfigType configType = config.getConfigType();
        try {
            byte[] bytes = configData.getBytes("utf-8");
            CompletableFuture.runAsync(() -> {
                ZkUtils.createForPath(createPath(appName, configName, configType.getDesc()), bytes,
                        zookeeperFactoryBean.getZkClient());
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("编码失败");
        } catch (Exception e) {
            if (e instanceof NodeExistsException) {
                logger.info("zookeeper中存在zNode：{}",
                        createPath(appName, configName, configType.getDesc()));
                throw e;
            }
            e.printStackTrace();
        }
    }

    private void insertRedis(Config config) {
        ConfigType configType = config.getConfigType();
        String configData = config.getConfigData();
        if (configType == ConfigType.JSON && isJsonStr(configData)) {
            redisClient.sAdd(createKey(config.getAppName(), config.getConfigName(),
                    configType.getDesc()), configData);
        }

        if (configType == ConfigType.PROPERTIES) {
            redisClient.sAdd(createKey(config.getAppName(), config.getConfigName(),
                    configType.getDesc()), configData);
        }

    }

    @Override
    public APIResult updateConf(Config config) {
        String appName = config.getAppName();
        String configName = config.getConfigName();
        ConfigType configType = config.getConfigType();
        Preconditions.checkArgument(config.getConfigType() != null, "configStatus不能是空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(config.getAppName()), "appName不能是空");
        Preconditions
                .checkArgument(StringUtils.isNotEmpty(config.getConfigName()), "configName不能是空");
        insertRedis(config);
        try {
            byte[] bytes = config.getConfigData().getBytes("utf-8");
            // 异步更改zk
            CompletableFuture.runAsync(() -> {
                try {
                    zookeeperFactoryBean.getZkClient().setData()
                            .forPath(createPath(appName, configName, configType.getDesc()), bytes);
                } catch (Exception e) {
                    return;
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("编码失败");
            return new APIResult(0, "更改失败");
        }

        return new APIResult(1, "更改成功");
    }

    @Override
    public List<Config> getConf(String token) {

        return configDao.getConf(token);
    }


    private String createPath(String appName, String configName, String desc) {
        return "/" + appName + "/" + configName + "/" + desc;
    }

    private String createKey(String appName, String configName, String type) {
        return appName + "#" + configName + "%" + type;
    }


    public boolean isJsonStr(String s) {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("nashorn");
        try {
            se.eval(s);
            return false;
        } catch (ScriptException e) {
            return true;
        }
    }
}
