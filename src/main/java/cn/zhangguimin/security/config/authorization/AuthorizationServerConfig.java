package cn.zhangguimin.security.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;

    /**
     * tokenStore：定义token保存位置，cn.zhangguimin.security.config.TokenStoreConfig中定义了方式
     *              可以保存在redis、jwt中。
     *              使用jwt时需要将JwtAccessTokenConverter设置到accessTokenConverter中
     *              可以自定义jwt，在TokenEnhancer,如果需要将自定义信息解析出来需要加入pom依赖jjwt
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(jwtTokenEnhancer);
        tokenEnhancers.add(jwtAccessTokenConverter);
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain)
                .accessTokenConverter(jwtAccessTokenConverter);
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
                .authorizedGrantTypes("password","refresh_token","authorization_code")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(2592000)
                .scopes("all");
    }
}
