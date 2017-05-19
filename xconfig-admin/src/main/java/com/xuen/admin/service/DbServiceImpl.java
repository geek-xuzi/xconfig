package com.xuen.admin.service;

import com.google.common.base.Preconditions;
import com.xuen.admin.bean.APIResult;
import com.xuen.admin.bean.DbApp;
import com.xuen.admin.dao.DbAppDao;
import com.xuen.xconfig.util.TokenUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng.xu
 * @since 2017-05-19
 */
@Service
public class DbServiceImpl implements DbService {

    @Autowired
    private DbAppDao dbAppDao;

    @Override
    public APIResult createApp(String appName) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(appName), "appName 不能为空");
        DbApp dbApp = new DbApp(appName, TokenUtil.generateToken(), new Date());
        try {
            dbAppDao.insertDbApp(dbApp);
            return new APIResult(1, "插入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResult(0, "插入失败");
        }
    }

    @Override
    public APIResult deleteApp(Integer aid) {
        try {
            dbAppDao.deleteApp(aid);
            return new APIResult(1, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResult(0, "删除失败");
        }

    }


}
