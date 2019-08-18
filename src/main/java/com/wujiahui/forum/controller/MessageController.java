package com.wujiahui.forum.controller;

import com.alibaba.fastjson.JSONObject;
import com.wujiahui.forum.entity.Message;
import com.wujiahui.forum.entity.Page;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.service.MessageService;
import com.wujiahui.forum.service.UserService;
import com.wujiahui.forum.util.ForumUtil;
import com.wujiahui.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

import static com.wujiahui.forum.util.ForumConstant.TOPIC_COMMENT;

/**
 * Created by NgCafai on 2019/8/9 16:35.
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 根据 conversationId 获取会话的对象
     * @param conversationId
     * @return
     */
    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int firstId = Integer.parseInt(ids[0]);
        int secondId = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == firstId) {
            return userService.findUserById(secondId);
        } else {
            return userService.findUserById(firstId);
        }
    }

    /**
     * 返回当前用户收到的，未读的消息的 id 组成的列表
     * @param messageList
     * @return
     */
    private List<Integer> getMessageIds(List<Message> messageList) {
        List<Integer> ids = new ArrayList<>();

        if (messageList != null) {
            for (Message message : messageList) {
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    /**
     * 处理获取会话（即消息）列表的请求
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/message/list", method = RequestMethod.GET)
    public String getMessageList(Model model, Page page) {
        User user = hostHolder.getUser();

        // 设置分页信息
        page.setLimit(5);
        page.setPath("/message/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversationVoList = new ArrayList<>();
        if(conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                // 当前会话总的消息数
                map.put("letterCount", messageService.findMessageCount(message.getConversationId()));
                // 当前会话的未读消息数
                map.put("unreadCount", messageService.findUnreadMessageCount(user.getId(), message.getConversationId()));
                // 当前会话是与哪个用户进行对话
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversationVoList.add(map);
            }
        }
        model.addAttribute("conversations", conversationVoList);


        // 查询未读消息数量
        int letterUnreadCount = messageService.findUnreadMessageCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/letter";
    }

    /**
     * 处理获取会话详情的请求
     * @param conversationId
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(path = "/message/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/message/detail/" + conversationId);
        page.setRows(messageService.findMessageCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findMessages(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messageVoList = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                messageVoList.add(map);
            }
        }
        model.addAttribute("letters", messageVoList);

        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));

        // 将状态为未读的消息设为已读
        List<Integer> ids = getMessageIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";

    }

    /**
     * 处理发送消息的请求
     * @param toName
     * @param content
     * @return
     */
    @RequestMapping(path = "/message/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return ForumUtil.getJSONString(1, "目标用户不存在！");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return ForumUtil.getJSONString(0, "发送成功！");
    }


    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) contentMap.get("userId")));
            messageVO.put("entityType", contentMap.get("entityType"));
            messageVO.put("entityId", contentMap.get("entityId"));
            messageVO.put("postId", contentMap.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("unread", unread);
        } else {
            messageVO.put("message", null);
        }
        model.addAttribute("commentNotice", messageVO);

        // 查询点赞类通知


        // 查询关注类通知


        // 查询未读消息数量
        int letterUnreadCount = messageService.findUnreadMessageCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    @RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) contentMap.get("userId")));
                map.put("entityType", contentMap.get("entityType"));
                map.put("entityId", contentMap.get("entityId"));
                map.put("postId", contentMap.get("postId"));
                // 通知作者，即系统用户
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getMessageIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }


}
