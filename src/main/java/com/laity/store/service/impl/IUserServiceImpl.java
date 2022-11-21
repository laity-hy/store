package com.laity.store.service.impl;

import com.laity.store.entity.User;
import com.laity.store.mapper.UserMapper;
import com.laity.store.service.IUserService;
import com.laity.store.service.ex.InsertException;
import com.laity.store.service.ex.UsernameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户模块业务层的实现类
 */
@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        //通过user参数来获取传递过来的username
        String username = user.getUsername();
        //调用findByUsername(String username)判断用户是否被注册过
        User result = userMapper.findByUsername(username);
        //判断结果集是否为null则抛出用户名被占用的异常
        if (result != null) {
            //抛出异常
            throw new UsernameDuplicatedException("用户名被占用");
        }

        //补全数据：is_delete设置为0，4个日志数据
        user.setIsDelete(0);
        Date date = new Date();
        user.setCreatedUser(user.getUsername());
        user.setCreatedTime(date);
        user.setModifiedUser(user.getUsername());
        user.setModifiedTime(date);

        //执行注册业务功能的实现
        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }
}
