package com.wujiahui.forum.controller;

import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.service.DiscussPostService;
import com.wujiahui.forum.util.ForumUtil;
import com.wujiahui.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by NgCafai on 2019/8/7 12:49.
 */
@Controller
public class DiscussPostController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/post/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return ForumUtil.getJSONString(403, "请先登录");
        }

        if (StringUtils.isBlank(title)) {
            return ForumUtil.getJSONString(403, "标题不能为空");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        return ForumUtil.getJSONString(0, "发布成功！");
    }
}
