package com.wujiahui.forum.controller;

import com.wujiahui.forum.annotation.LoginRequired;
import com.wujiahui.forum.entity.User;
import com.wujiahui.forum.service.UserService;
import com.wujiahui.forum.util.ForumUtil;
import com.wujiahui.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by NgCafai on 2019/8/2 22:16.
 */
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${forum.path.upload}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/user/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 处理上传头像的请求
     * @param headerImage
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/user/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确！");
            return "/site/setting";
        }

        // generate a new random file name
        fileName = ForumUtil.generateUUID() + suffix;
        // define the path of to store the image
        File dest = new File(uploadPath + File.separator + fileName);
        try {
            // store the image
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传头像失败：" + e.getMessage());
            throw new  RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/forum/user/header/xxx.png
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        User user = hostHolder.getUser();
        userService.updateHeader(user.getId(), headerUrl);

        // 上传成功，重定向到首页
        return "redirect:/";
    }

    /**
     * 返回头像
     */
    @RequestMapping(path = "/user/header/{fileName}", method = RequestMethod.GET)
    public void getUserHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        String filePath = uploadPath + File.separator + fileName;

        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        response.setContentType("image/" + suffix);
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {

            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }
}
