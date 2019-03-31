package cn.zhangguimin.security.config.sms;

import cn.zhangguimin.security.config.properties.SecurityConstants;
import cn.zhangguimin.security.config.properties.SecurityProperties;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mr. Zhang
 * @description 短信登录AuthenticationFilter，参考UsernamePasswordAuthenticationFilter
 * @date 2019/3/29 16:46
 * @website https://www.zhangguimin.cn
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;

    private SecurityProperties securityProperties;


    public SmsCodeAuthenticationFilter(SecurityProperties securityProperties) {
        super(new AntPathRequestMatcher(securityProperties.getSms().getProcessingUrl(), "POST"));
        this.securityProperties=securityProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }

        mobile = mobile.trim();

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 获取手机号
     *
     * @param request
     * @return security
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_LOGIN_NAME);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
