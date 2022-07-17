package com.wsw.user.remote;

import com.wsw.netty.annotation.Remote;
import com.wsw.netty.util.Response;
import com.wsw.netty.util.ResponseUtil;
import com.wsw.user.bean.User;
import com.wsw.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote {
    @Resource
    private UserService userService;

    public Response saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    public Response saveUsers(List<User> users) {
        userService.saveList(users);
        return ResponseUtil.createSuccessResult(users);
    }

}
