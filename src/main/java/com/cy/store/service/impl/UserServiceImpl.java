package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper  userMapper;
    @Override
    public void reg(User user) {
        // 通过user参数来获取传递过来的username
        String username = user.getUsername();
        // 调用findByusername(username）判断用户是否被注册过
        User result = userMapper.findByUsername(username);
        // 判断结果集是否不为null则抛出用户名被占用的异常
        if(result != null){
            throw new UsernameDuplicateException("用户名被占用");
        }

        //密码加密处理的实现：md5算法的形式
        //(串+password+串)----md5算法进行加密，连续加载三次
        //盐值+password+盐值----盐值就是一个随机的字符串
        String oldPassword = user.getPassword();
        // 获取盐值（随机生成一个盐值）
        String salt = UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        // 将密码和盐值作为一个整体进行加密处理
        String md5Password = getMd5Password(oldPassword, salt);

        user.setPassword(md5Password);

        // 补全信息
        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        // 执行注册业务功能的实现(rows==1)
        Integer rows = userMapper.insert(user);
        if(rows != 1){
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }

    @Override
    public User login(String username, String password) {
        // 根据用户名称来查询用户的数据是否存在，如果不在则抛出异常
        User result = userMapper.findByUsername(username);
        if(result == null){
            throw new UserNotFoundException("用户数据不存在");
        }
        // 检测用户的密码是否匹配
        //1.先获取到数据库中的加密之后的密码
        String oldPassword = result.getPassword();
        //2.和用户的传递过来的密码进行比较
        //2.1先获取盐值：上一次在注册时所自动生成的盐值
        String salt = result.getSalt();
        //2.2将用户的密码按照相同的md5算法的规则进行加密
        String newMd5Password = getMd5Password(password,salt);
        //3.将密码进行比较
        if(!newMd5Password.equals(oldPassword)){
            throw new PasswordNotMatchException("用户密码错误");
        }

        if(result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }

        User user = new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

    @Override
    public void changePassword(Integer uid, String username,
                               String oldPassword, String newPassword) {
        User result = userMapper.findByUid(uid);
        if(result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        // 原始密码和数据库中密码进行比较
        String oldMd5Password = getMd5Password(oldPassword, result.getSalt());
        if(!result.getPassword().equals(oldMd5Password)){
            throw new PasswordNotMatchException("密码错误");
        }
        // 将新的密码设置到数据库中，将新的密码进行加密再去更新
        String newMd5Password = getMd5Password(newPassword, result.getSalt());
        Integer rows = userMapper.updatePasswordByUid(uid,newMd5Password,username,new Date());
        if(rows != 1){
            throw new UpdateException("更新数据时产生未知的异常");
        }
    }

    @Override
    public User getByUid(Integer uid) {
        User result = userMapper.findByUid(uid);
        if(result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        User user = new User();
        user.setUid(result.getUid());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setUsername(result.getUsername());

        return user;
    }

    @Override
    public void changeInfo(Integer uid, String username, User user) {
        User result = userMapper.findByUid(uid);
        if(result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        user.setUid(uid);
//        user.setUsername(username);
        user.setModifiedUser(username);
        user.setModifiedTime(new Date());

        Integer rows = userMapper.updateInfoByUid(user);
        if(rows != 1){
            throw new UpdateException("更新数据时产生未知的异常");
        }
    }

    @Override
    public void changeAvatar(Integer uid, String username, String avatar) {
        User result = userMapper.findByUid(uid);
        if(result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("用户数据不存在");
        }
        Integer rows = userMapper.updateAvatarByUid(uid,avatar,username,new Date());
        if(rows != 1){
            throw new UpdateException("更新用户头像时产生未知的异常");
        }
    }

    /**
     * 执行密码加密
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密文
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
