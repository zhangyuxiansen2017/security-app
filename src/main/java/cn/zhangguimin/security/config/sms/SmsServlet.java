package cn.zhangguimin.security.config.sms;

import cn.zhangguimin.security.config.properties.SecurityConstants;
import cn.zhangguimin.security.config.properties.SecurityProperties;
import org.apache.commons.lang.RandomStringUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Mr. Zhang
 * @description 发送短信
 * @date 2019-03-29 23:56
 * @website https://www.zhangguimin.cn
 */
public class SmsServlet extends HttpServlet implements Servlet {

    private SecurityProperties securityProperties;

    public SmsServlet(SecurityProperties properties) {
        this.securityProperties = properties;
    }

    @Override
    public void init(ServletConfig conf) throws ServletException {

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String mobile = req.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE);

        String code = RandomStringUtils.randomAlphabetic(Integer.valueOf(securityProperties.getSms().getCodeLength()));
        req.getSession().setAttribute(securityProperties.getSms().getCode(), code);
        req.getSession().setAttribute(SecurityConstants.SESSION_KEY_DATE, LocalDateTime.now().plusSeconds(Integer.valueOf(securityProperties.getCaptcha().getValidity())));

        System.out.println("手机号："+mobile+" 验证码："+code);
    }
}
