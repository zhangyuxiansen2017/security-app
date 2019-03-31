package cn.zhangguimin.security.config.properties;

import lombok.Data;

/**
 * @author Mr. Zhang
 * @description 短信properties
 * @date 2019-03-29 19:55
 * @website https://www.zhangguimin.cn
 */
@Data
public class SmsProperties extends BaseProperties {

    public SmsProperties(){
        setCodeLength("6");
        setUrl("/mobile");
        setProcessingUrl("/authentication/mobile");
        setCode("smsCode");
    }
}
