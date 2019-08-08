package com.wujiahui.forum.controller;

import com.wujiahui.forum.entity.Comment;
import com.wujiahui.forum.entity.DiscussPost;
import com.wujiahui.forum.entity.Page;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.service.CommentService;
import com.wujiahui.forum.service.DiscussPostService;
import com.wujiahui.forum.service.UserService;
import com.wujiahui.forum.util.ForumConstant;
import com.wujiahui.forum.util.ForumUtil;
import com.wujiahui.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

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

    @RequestMapping(path = "/post/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPostDetail(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPost);

        // 获取该帖子发布者的信息
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);

        // 分页信息
        page.setLimit(5);
        page.setPath("/post/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        // comment: 给帖子的评论
        // reply: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(ForumConstant.ENTITY_TYPE_POST,
                discussPostId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null ) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment);
                // 发布当前评论的用户
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ForumConstant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        // 发布当前回复的用户
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }

                commentVo.put("replys", replyVoList);

                // 回复的数量
                commentVo.put("replyCount", replyVoList.size());
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }

}
