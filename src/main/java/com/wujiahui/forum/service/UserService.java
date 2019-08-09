package com.wujiahui.forum.service;

import com.wujiahui.forum.dao.LoginTicketMapper;
import com.wujiahui.forum.dao.UserMapper;
import com.wujiahui.forum.entity.LoginTicket;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.util.ForumConstant;
import com.wujiahui.forum.util.ForumUtil;
import com.wujiahui.forum.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by NgCafai on 2019/7/28 17:24.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> resultMap = new HashMap<>();

        // check whether necessary information are provided in the argument
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            resultMap.put("usernameMsg", "账号不能为空！");
            return resultMap;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            resultMap.put("passwordMsg", "密码不能为空！");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            resultMap.put("emailMsg", "邮箱不能为空!");
            return resultMap;
        }

        // check whether the information is valid
        User tempUser = userMapper.selectByName(user.getUsername());
        if (tempUser != null) {
            resultMap.put("usernameMsg", "账号已存在！");
            return resultMap;
        }

        tempUser = userMapper.selectByEmail(user.getEmail());
        if (tempUser != null) {
            resultMap.put("emailMsg", "该邮箱已被注册！");
            return resultMap;
        }

        // now the information is valid
        // register the new user
        user.setSalt(ForumUtil.generateUUID().substring(0, 5));
        user.setPassword(ForumUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(ForumUtil.generateUUID());
        user.setHeaderUrl(String.format(domain + contextPath + "/img/%d.jpg", new Random().nextInt(30)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);



        // try to send the activation mail
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // the activation link has the following form:
        // http://localhost:8080/forum/activation/{userId}/code
        String activationUrl = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("activationUrl", activationUrl);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return resultMap;
    }

    public int activation(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            int status = user.getStatus();
            if (status == 0 && user.getActivationCode().equals(activationCode)) {
                userMapper.updateStatus(userId, 1);
                return ForumConstant.ACTIVATION_SUCCESS;
            } else if (status == 1) {
                return ForumConstant.ACTIVATION_REPEAT;
            }
        }
        return ForumConstant.ACTIVATION_FAILURE;
    }

    public Map<String, Object> login(String username, String password, long expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "此用户名不存在");
            return map;
        }

        // 验证账号是否已经激活
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        if (!user.getPassword().equals(ForumUtil.md5(password + user.getSalt()))) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // all is well now
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(ForumUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }
}

