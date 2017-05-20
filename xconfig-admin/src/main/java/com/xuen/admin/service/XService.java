package com.xuen.admin.service;

import com.xuen.admin.bean.APIResult;
import com.xuen.xconfig.module.Config;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public interface XService {

    APIResult createConf(Config config);

    APIResult updateConf(Config config);

    List<Config> getConf(String token);

    Properties test();

    APIResult upload(MultipartFile file, String token) throws IOException;
}
