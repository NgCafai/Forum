package com.wujiahui.forum.interceptor;

import com.wujiahui.forum.dao.LoginTicketMapper;
import com.wujiahui.forum.entity.LoginTicket;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.service.UserService;
import com.wujiahui.forum.util.ForumUtil;
import com.wujiahui.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by NgCafai on 2019/8/2 20:34.
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 查看 request 中有没有 name="ticket" 的 Cookie，若有，检查该Cookie 对应的值是否有效，
     * 若有效，则将对应的 User 对象绑定到 hostHolder 中（使得每个线程可以访问自己的 User 对象）
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 尝试获取 name="ticket" 的 Cookie 的值
        String ticket = ForumUtil.getCookieValue(request, "ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if (loginTicket!= null
                    && new Date().before(loginTicket.getExpired())
                    && loginTicket.getStatus() == 0) {
                // 用户已经登录，还没有退出登录状态，且登录信息没有过期
                User user = userService.findUserById(loginTicket.getUserId());
                // 将用户对象绑定到当前线程
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    /**
     * 若 hostHolder 中持有线程私有的用户对象，且需要对模板进行渲染，
     * 则将 User 对象加到 modelAndView 中
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * 渲染完成后，去掉当前线程持有的用户对象（如有）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.remove();
    }
}
