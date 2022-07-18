//import com.wsw.netty.annotation.RemoteInvoke;
//import com.wsw.user.bean.User;
//import com.wsw.user.remote.UserRemote;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.Component;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = RemoteInvokingTest.class)
//@ComponentScan("com.wsw")
//public class RemoteInvokingTest {
//    @RemoteInvoke
//    private UserRemote userRemote;
//
//    @Test
//    public void testSaveUser() {
//        User u = new User();
//        u.setId(1);
//        u.setName("张三");
//        userRemote.saveUser(u);
//    }
//
//    @Test
//    public void testSaveUsers() {
//        List<User> users = new ArrayList<User>();
//        User u = new User();
//        u.setId(1);
//        u.setName("张三");
//        users.add(u);
//        userRemote.saveUsers(users);
//    }
//}
