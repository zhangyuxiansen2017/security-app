package cn.zhangguimin.security.config;

import cn.zhangguimin.security.config.properties.SecurityConstants;
import cn.zhangguimin.security.config.properties.SecurityProperties;
import cn.zhangguimin.security.exception.ValidateCaptchaException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr. Zhang
 * @description 验证码过滤器
 * @date 2019/3/28 14:32
 * @website https://www.zhangguimin.cn
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SecurityProperties securityProperties;

    private Map<String, String> urlMap = new HashMap<>();

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(securityProperties.getSms().getProcessingUrl(), securityProperties.getSms().getCode());

        urlMap.put(securityProperties.getCaptcha().getProcessingUrl(), securityProperties.getCaptcha().getCode());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String validateCodeType = getValidateCodeType(request);
        if (validateCodeType != null) {
            try {
                validate(request, validateCodeType);
            } catch (ValidateCaptchaException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getValidateCodeType(HttpServletRequest request) {
        String result = null;
        if (!org.apache.commons.lang.StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }

    private void validate(HttpServletRequest request, String validateCodeType) {

        HttpSession session = request.getSession();

        String captcha = (String) session.getAttribute(validateCodeType);
        LocalDateTime ldt = (LocalDateTime) session.getAttribute(SecurityConstants.SESSION_KEY_DATE);
        boolean isExpried = ldt.isBefore(LocalDateTime.now());

        String codeInRequest;
        String username;
        try {
            username = ServletRequestUtils.getStringParameter(request, SecurityConstants.DEFAULT_PARAMETER_NAME_LOGIN_NAME);
            Integer count = (Integer) redisTemplate.opsForValue().get(username);
            count = (count == null ? 0 : count);
            if (count >= 10) {
                throw new ValidateCaptchaException("尝试次数过多，请24小时后再登录！");
            } else {
                redisTemplate.opsForValue().set(username, count + 1,24, TimeUnit.DAYS);
            }
            codeInRequest = ServletRequestUtils.getStringParameter(request, validateCodeType);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCaptchaException("获取验证码的值失败");
        }
        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCaptchaException("验证码的值不能为空");
        }

        if (captcha == null) {
            throw new ValidateCaptchaException("验证码不存在");
        }

        if (isExpried) {
            session.removeAttribute(validateCodeType);
            session.removeAttribute(SecurityConstants.SESSION_KEY_DATE);
            throw new ValidateCaptchaException("验证码已过期");
        }

        if (!codeInRequest.equals(captcha)) {
            throw new ValidateCaptchaException("验证码不匹配");
        }
        session.removeAttribute(validateCodeType);
        session.removeAttribute(SecurityConstants.SESSION_KEY_DATE);
    }

}
