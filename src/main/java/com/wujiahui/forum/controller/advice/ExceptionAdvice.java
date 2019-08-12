package com.wujiahui.forum.controller.advice;

import com.wujiahui.forum.util.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * service 和 dao 层将 IOException 往上抛，然后针对 Controller 层进行异常处理
 *
 * Created by NgCafai on 2019/8/9 20:54.
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常：" + e.getMessage());
        // 记录 stack trace
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            logger.error(stackTraceElement.toString());
        }

        // 向浏览器作出相应
        boolean isAjaxRequest = "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
        if (isAjaxRequest){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(ForumUtil.getJSONString(1, "服务器异常"));
        } else {
            // request.getContextPath() 返回的是项目的路径：/forum
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
