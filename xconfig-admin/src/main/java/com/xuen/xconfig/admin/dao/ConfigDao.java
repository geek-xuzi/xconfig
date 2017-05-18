package com.xuen.xconfig.admin.dao;

import com.xuen.xconfig.module.Config;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author zheng.xu
 * @since 2017-05-18
 */
public interface ConfigDao {

    List<Config> getConf(@Param("token") String token);

}
