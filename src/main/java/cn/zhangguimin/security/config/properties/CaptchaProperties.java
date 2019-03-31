package cn.zhangguimin.security.config.properties;

import lombok.Data;

/**
 * @author Mr. Zhang
 * @description 短信properties
 * @date 2019-03-29 19:45
 * @website https://www.zhangguimin.cn
 * Unread field:
 */
@Data
public class CaptchaProperties extends BaseProperties {

    private String type = "char";

    private String border = "yes";

    private String borderColor = "105,179,90";

    private String fontColor = "blue";

    private String imageWidth = "200";

    private String imageHeight = "40";

    private String fontSize = "38";

    private String fontNames = "Arial,Courier";

    private String noiseColor = "white";

    private String textproducerImpl = "";

    private String noiseImpl = "";

    private String obscurificatorImpl = "";

    public CaptchaProperties(){
        setCodeLength("6");
        setCode("captchaCode");
        setProcessingUrl("/authentication/form");
        setUrl("/image");
    }

}
