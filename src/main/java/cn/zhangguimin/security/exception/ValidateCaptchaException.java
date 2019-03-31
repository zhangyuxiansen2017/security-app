package cn.zhangguimin.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Mr. Zhang
 * @description 验证码错误
 * @date 2019/3/28 14:39
 * @website https://www.zhangguimin.cn
 */
public class ValidateCaptchaException extends AuthenticationException {

    public ValidateCaptchaException(String explanation) {
        super(explanation);
    }
}
