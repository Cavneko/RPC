package com.wsw.user.remote;

import com.wsw.netty.util.Response;
import com.wsw.user.bean.User;

import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> users);

}
