package cn.zhangguimin.security.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mr. Zhang
 * @description 登录失败后需要处理的事情，比如向redis中存储登录尝试次数，到达一定次数锁定账号,处理特定异常放回给前端
 * @date 2019-03-27 22:17
 * @website https://www.zhangguimin.cn
 */
@Component
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("处理登录失败需要做的。。。。。。。");
        request.setAttribute("msg","验证码错误");
        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
