package com.wsw.service;

import com.alibaba.fastjson.JSONObject;
import com.wsw.client.annotation.RemoteInvoke;
import com.wsw.user.model.User;
import com.wsw.user.remote.UserRemote;
import org.springframework.stereotype.Service;

@Service
public class BasicService {
    @RemoteInvoke
    private UserRemote userRemote;

    public void testSaveUser() {
        User u = new User();
        u.setId(1);
        u.setName("张三");
        Object response = userRemote.saveUser(u);
        System.out.println(JSONObject.toJSONString(response));
    }
}
