package com.wsw.user.remote;

import com.wsw.netty.annotation.Remote;
import com.wsw.netty.util.ResponseUtil;
import com.wsw.user.service.UserService;
import com.wsw.user.model.User;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote {
    @Resource
    private UserService userService;

    public Object saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    public Object saveUsers(List<User> users) {
        userService.saveList(users);
        return ResponseUtil.createSuccessResult(users);
    }

}
