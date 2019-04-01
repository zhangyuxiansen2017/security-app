package cn.zhangguimin.security.controller;

import cn.zhangguimin.security.config.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author Mr. Zhang
 * @description 登录
 * @date 2019-03-27 20:30
 * @website https://www.zhangguimin.cn
 */
@Controller
public class LoginController {
    @Autowired
    private SecurityProperties securityProperties;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    /**
     * 可以直接  getCurrentUser(Authentication  authentication) 直接return authentication，这样会返回很多没必要的数据
     * 可以使用 @AuthenticationPrincipal UserDetails user。返回的只是UserDetails信息
     * 在任何地方获取：UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     *
     * @param user
     * @return
     */
    @GetMapping("/me")
    @ResponseBody
    public Object getCurrentUser(HttpServletRequest request) throws UnsupportedEncodingException {
        //解析自定义jwt
        String authentication = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(authentication, "bearer");
        Claims claims = Jwts.parser().setSigningKey("zgm".getBytes("UTF-8")).parseClaimsJws(token).getBody();
        return claims;
    }

    @GetMapping(value = "/invalidSession")
    public String invalidSession() {
        return "invalidSession";
    }
}
