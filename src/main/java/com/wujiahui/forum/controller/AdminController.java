package com.wujiahui.forum.controller;

import com.wujiahui.forum.dao.DiscussPostMapper;
import com.wujiahui.forum.dao.UserMapper;
import com.wujiahui.forum.dao.elasticsearch.DiscussPostRepository;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

/**
 * Created by NgCafai on 2019/8/20 23:40.
 */
@Controller
public class AdminController {

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @RequestMapping(value = "/admin/setHeader", method = RequestMethod.GET)
    @ResponseBody
    public String setHeaders(String url){
        if ("xiaoli".equals(hostHolder.getUser().getUsername())){
            List<User> users = userMapper.selectUsers();
            for (User user : users) {
                if (user.getId() <= 200) {
                    if (StringUtils.isNotBlank(url) && url.length() >= 2) {
                        userMapper.updateHeader(user.getId(), String.format(url + "/img/%d.jpg", new Random().nextInt(30)));
                    } else {
                        userMapper.updateHeader(user.getId(), String.format(domain + contextPath + "/img/%d.jpg", new Random().nextInt(30)));
                    }
                    userMapper.updateEmail(user.getId(), "null@sina.com");
                }
            }
            return "success";
        }
        return "fail";
    }


    @RequestMapping("/admin/initializeES")
    @ResponseBody
    public String initializeES() {
        if ("xiaoli".equals(hostHolder.getUser().getUsername())){
            List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(0, 0, 1000);

            discussPostRepository.saveAll(discussPostList);
            return "success";
        }

        return "fail";
    }

}
