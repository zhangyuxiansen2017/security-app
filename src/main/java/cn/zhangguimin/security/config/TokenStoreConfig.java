package cn.zhangguimin.security.config;

import cn.zhangguimin.security.config.jwt.ZgmJwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Mr. Zhang
 * @description token持久化
 * @date 2019/4/1 16:23
 * @website https://www.zhangguimin.cn
 */
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 使用redis存储token
     * @param factory
     * @return
     */
    //@Bean
    public TokenStore tokenStore(RedisConnectionFactory factory){
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }

    /**
     * 使用jwt保存token
     * @return
     */
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("zgm");
        return jwtAccessTokenConverter;
    }

    /**
     * 自定义信息
     * @return
     */
    @Bean
    public TokenEnhancer jwtTokenEnhancer(){
        return new ZgmJwtTokenEnhancer();
    }
}
