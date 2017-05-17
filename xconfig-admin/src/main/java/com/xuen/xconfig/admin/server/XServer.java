package com.xuen.xconfig.admin.server;

import com.google.common.base.Preconditions;
import com.xuen.xconfig.admin.bean.APIResult;
import com.xuen.xconfig.admin.bean.Config;
import com.xuen.xconfig.admin.bean.ConfigStatus;
import com.xuen.xconfig.anno.XValue;
import com.xuen.xconfig.core.ZookeeperFactoryBean;
import com.xuen.xconfig.redis.RedisClient;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-05-16
 */
@Component
public class XServer {

    @XValue("/xuen")
    private int s = 5;


    @Resource
    private ZookeeperFactoryBean zookeeperFactoryBean;

    @Resource
    private RedisClient redisClient;

    public String test() {
        return s + "";
    }

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


    public APIResult create(Config config) {
        Preconditions.checkArgument(config.getConfigType() != null, "configStatus 不能是空");
        return null;
    }
}
