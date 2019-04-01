package cn.zhangguimin.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Mr. Zhang
 * @description 自定义AuthorizationServer，如果AuthenticationManager无法注入，则需要在主启动类中添加配置，请参考主启动类
 * @date 2019/4/1 14:13
 * @website https://www.zhangguimin.cn
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore redisTokenStore;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(redisTokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * inMemory：使用内存模式
     * withClient：clientId
     * secret：secret
     * accessTokenValiditySeconds：token有效期
     * authorizedGrantTypes：授权模式（password，refresh_token..五种）
     * scopes：权限（all、read、write...）
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("zgmid")
                .secret("zgmsecret")
                .accessTokenValiditySeconds(7200)
                .authorizedGrantTypes("password","refresh_token")
                .scopes("all");
    }
}
