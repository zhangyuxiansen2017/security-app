package cn.zhangguimin.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Mr. Zhang
 * @description 验证码参数
 * @date 2019/3/28 13:47
 * @website https://www.zhangguimin.cn
 */
@Data
@ConfigurationProperties(prefix = "zgm.security")
public class SecurityProperties {

    private SmsProperties sms = new SmsProperties();

    private CaptchaProperties captcha = new CaptchaProperties();

}
