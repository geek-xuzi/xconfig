package com.xuen.xconfig;

import com.xuen.xconfig.core.ZookeeperFactoryBean;
import com.xuen.xconfig.redis.RedisClient;
import com.xuen.xconfig.redis.RedisFartory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

/**
 * @author zheng.xu
 * @since 2017-05-19
 */
@Configuration
@ComponentScan("com.xuen.xconfig.*")
public class WebConfig extends DelegatingWebMvcConfiguration {


    @Bean(name = "redisClient")
    public RedisClient createRedisClient() {
        return new RedisClient(RedisFartory.createSimpleAsync("xuzi520.cn", 6379, "xuen123456"));
    }

//    @Bean
//    public CoreHolder createCoreHolder() {
//        return new CoreHolder();
//    }

    @Bean
    public ZookeeperFactoryBean createZookeeperFactoryBean() {
        return new ZookeeperFactoryBean("xuzi520.cn:2181,xuzi520.cn:2182,xuzi520.cn:2183");
    }
}
