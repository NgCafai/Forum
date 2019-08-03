package com.wujiahui.forum.controller;

import com.google.code.kaptcha.Producer;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.service.UserService;
import com.wujiahui.forum.util.ForumConstant;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * deal with requests to register a new account or login
 *
 * Created by NgCafai on 2019/7/29 17:23.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * get the register page
     * @return
     */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    /**
     * register a new account
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String regiter(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map != null && !map.isEmpty()) {
            // there is something wrong and the map contains information about what goes wrong
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            // response the register page
            return "/site/register";
        } else {
            // all is well
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件，请尽快激活！（如果收" +
                    "不到邮件，请检查邮件是否被过滤到垃圾箱中）");
            model.addAttribute("target", "/");
            // return the page showing the message above and automatically jump to a  new page defined by the "target"
            return "/site/operate-result";
        }
    }

    /**
     * activate a new account
     * @param model
     * @param userId
     * @param activationCode
     * @return
     */
    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String activationCode) {
        int activationResult = userService.activation(userId, activationCode);
        if (activationResult == ForumConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了！请登录");
            model.addAttribute("target", "/login");
        } else if (activationResult == ForumConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(HttpServletRequest request) {
        return "/site/login";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码文字内容及对应的图片
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码文字内容存到 Session 中
        session.setAttribute("kaptcha", text);

        // 将图片传给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("响应验证码图片失败：" + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String kaptcha, boolean rememberMe, Model model,
                        HttpSession session, HttpServletResponse response) {
        // 检查验证码
        String correctKaptcha = (String) session.getAttribute("kaptcha");
        if (!correctKaptcha.equalsIgnoreCase(kaptcha)) {
            model.addAttribute("kaptchaMsg", "验证码不正确!");
            return "/site/login";
        }

        long expiredSeconds = rememberMe ? ForumConstant.REMEMBER_EXPIRED_SECONDS : ForumConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password,expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", (String) map.get("ticket"));
            cookie.setPath(contextPath);
            cookie.setMaxAge((int)expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}








