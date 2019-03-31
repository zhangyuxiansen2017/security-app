package cn.zhangguimin.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableCaching
@SpringBootApplication
@EnableAuthorizationServer
public class SecurityAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityAppApplication.class, args);
    }

}
