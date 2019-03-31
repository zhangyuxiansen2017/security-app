package cn.zhangguimin.security.config.properties;

import lombok.Data;

/**
 * @author Mr. Zhang
 * @description 公共properties
 * @date 2019-03-29 19:48
 * @website https://www.zhangguimin.cn
 */
@Data
public class BaseProperties {

    private String codeLength = "6";

    private String url = "/";

    private String processingUrl = "/login";

    private String validity = "120";

    private String code = "code";

}
