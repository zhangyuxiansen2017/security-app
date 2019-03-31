package cn.zhangguimin.security.config;

import cn.zhangguimin.security.config.captcha.CaptchaServlet;
import cn.zhangguimin.security.config.properties.SecurityProperties;
import cn.zhangguimin.security.config.sms.SmsServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr. Zhang
 * @description 验证码配置
 * @date 2019-03-28 11:47
 * @website https://www.zhangguimin.cn
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
class SecurityPropertiesConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(name = "captchaServlet")
    public ServletRegistrationBean captchaServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new CaptchaServlet(securityProperties), securityProperties.getCaptcha().getUrl());
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsServlet")
    public ServletRegistrationBean smsServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new SmsServlet(securityProperties), securityProperties.getSms().getUrl());
        return registrationBean;
    }
}