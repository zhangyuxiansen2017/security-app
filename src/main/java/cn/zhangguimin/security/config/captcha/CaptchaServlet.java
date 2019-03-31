package cn.zhangguimin.security.config.captcha;

import cn.zhangguimin.security.config.properties.SecurityConstants;
import cn.zhangguimin.security.config.properties.SecurityProperties;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * @author Mr. Zhang
 * @description CaptchaServlet
 * @date 2019/3/29 9:04
 * @website https://www.zhangguimin.cn
 */
public class CaptchaServlet extends HttpServlet implements Servlet {

    private Producer captchaProducer = null;

    private String sessionKeyValue = null;

    private SecurityProperties securityProperties;

    public CaptchaServlet(SecurityProperties properties) {
        this.securityProperties = properties;
    }

    @Override
    public void init(ServletConfig conf) throws ServletException {

        super.init(conf);
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", securityProperties.getCaptcha().getBorder());
        properties.setProperty("kaptcha.border.color", securityProperties.getCaptcha().getBorderColor());

        properties.setProperty("kaptcha.textproducer.font.size", securityProperties.getCaptcha().getFontSize());
        properties.setProperty("kaptcha.textproducer.font.color", securityProperties.getCaptcha().getFontColor());
        properties.setProperty("kaptcha.textproducer.char.spac", securityProperties.getCaptcha().getType().equals("char") ? "35":"5");
        properties.setProperty("kaptcha.textproducer.char.length", securityProperties.getCaptcha().getCodeLength());
        properties.setProperty("kaptcha.textproducer.font.names", securityProperties.getCaptcha().getFontNames());
        properties.setProperty("kaptcha.textproducer.impl", securityProperties.getCaptcha().getTextproducerImpl());

        properties.setProperty("kaptcha.image.width", securityProperties.getCaptcha().getImageWidth());
        properties.setProperty("kaptcha.image.height", securityProperties.getCaptcha().getImageHeight());

        properties.setProperty("kaptcha.session.key", securityProperties.getCaptcha().getCode());
        properties.setProperty("kaptcha.noise.color", securityProperties.getCaptcha().getNoiseColor());
        properties.setProperty("kaptcha.noise.impl", securityProperties.getCaptcha().getNoiseImpl());

        properties.setProperty("kaptcha.obscurificator.impl", securityProperties.getCaptcha().getObscurificatorImpl());

        ImageIO.setUseCache(false);

        Config config = new Config(properties);
        this.captchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setContentType("image/jpeg");

        String capStr = null;
        String code = null;
        BufferedImage bi = null;
        if (StringUtils.equals(securityProperties.getCaptcha().getType(),"math")) {
            String capText = captchaProducer.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            bi = captchaProducer.createImage(capStr);
        } else {
            capStr = code = captchaProducer.createText();
            bi = captchaProducer.createImage(capStr);
        }

        req.getSession().setAttribute(this.sessionKeyValue, code);
        req.getSession().setAttribute(SecurityConstants.SESSION_KEY_DATE, LocalDateTime.now().plusSeconds(Integer.valueOf(securityProperties.getCaptcha().getValidity())));

        ServletOutputStream out = resp.getOutputStream();
        ImageIO.write(bi, "jpeg", out);
    }
}
