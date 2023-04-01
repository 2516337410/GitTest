package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicateException;
import com.cy.store.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// @Controller
@RestController // @Controller + @ResponseBody
@RequestMapping("users")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    @RequestMapping("reg")
    // @ResponseBody // 表示此方法的响应结果以json格式进行数据的响应给到前端
    public JsonResult<Void> reg(User user){
        userService.reg(user);
        return new JsonResult<>(OK);
    }

    @RequestMapping("login")
    public JsonResult<User> login(String username,
                                  String password,
                                  HttpSession session){
        User data = userService.login(username,password);

        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());

//        System.out.println(getUsernameFromSession(session));
//        System.out.println(getuidFromSession(session));

        return new JsonResult<User>(OK, data);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword,
                                           String newPassword,
                                           HttpSession session){
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changePassword(uid,username,oldPassword,newPassword);
        return new JsonResult<>(OK);
    }

    @RequestMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session){
        User data = userService.getByUid(getuidFromSession(session));
        return new JsonResult<>(OK, data);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user,HttpSession session){
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changeInfo(uid,username,user);
        return new JsonResult<>(OK);
    }

    /*设置上传文件的最大值*/
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;

    /*限制上传文件的类型*/
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }

    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session, MultipartFile file){
        // 判断上传的文件是否为空
        if(file.isEmpty()){
            throw new FileEmptyException("文件为空");
        }
        if(file.getSize() > AVATAR_MAX_SIZE){
            throw new FileSizeException("文件大小超出限制");
        }
        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        if(!AVATAR_TYPE.contains(contentType)){
            throw new FileTypeException("文件类型不支持");
        }
        // 上传的文件.../upload/文件.png
        String parent = session.getServletContext().getRealPath("upload");
        // //File对象指向这个路径，File是否存在
        File dir = new File(parent);
        if(!dir.exists()){ // /(检测目录是否存在
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
//        System.out.println("originalFilename = " + originalFilename);

        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String filename = UUID.randomUUID().toString().toUpperCase() + suffix;

        File dest = new File(dir, filename); // 是一个空文件
        try {
            file.transferTo(dest); // //将file文件中的数据写入到dest文件
        } catch (FileStateException e) {
            throw new FileStateException("文件状态异常");
        }catch (IOException e) {
            throw new FileUploadIOException("文件读写异常");
        }

        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        String avatar = "/upload/" + filename;
        userService.changeAvatar(uid,username,avatar);
        return new JsonResult<>(OK,avatar);
    }

    /*
    @RequestMapping("reg")
    // @ResponseBody // 表示此方法的响应结果以json格式进行数据的响应给到前端
    public JsonResult<Void> reg(User user){
        // 创建响应结果对象
        JsonResult<Void> result =  new JsonResult<>();
        try {
            userService.reg(user);
            result.setState(200);
            result.setMessage("用户注册成功");
        } catch (UsernameDuplicateException e) {
            result.setState(4000);
            result.setMessage("用户名被占用");
        }catch (InsertException e) {
            result.setState(5000);
            result.setMessage("注册时产生了未知的异常");
        }
        return result;
    }
    */
}
