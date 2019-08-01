package com.wujiahui.forum.controller;

import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.Page;
import com.wujiahui.forum.service.DiscussPostService;
import com.wujiahui.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by NgCafai on 2019/7/28 17:26.
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getHomePage(Model model, Page page) {
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/");

        List<DiscussPost> postList = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> mapList = new LinkedList<>();
        if (postList != null) {
            for (DiscussPost post : postList) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("user", userService.findUserById(post.getUserId()));
                mapList.add(map);
            }
        }
        model.addAttribute("mapList", mapList);
        return "/home";



    }
}
