package com.xuen.admin.dao;

import com.xuen.admin.bean.DbApp;
import org.apache.ibatis.annotations.Param;

/**
 * @author zheng.xu
 * @since 2017-05-19
 */
public interface DbAppDao {

    void insertDbApp(@Param("dbApp") DbApp dbApp);

    void deleteApp(@Param("aid") Integer aid);
}
