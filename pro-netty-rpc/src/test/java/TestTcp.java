//import com.wsw.netty.client.ClientRequest;
//import com.wsw.netty.util.Response;
//import com.wsw.netty.client.TcpClient;
//import org.junit.jupiter.api.Test;
//import com.wsw.user.bean.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestTcp {
//    @Test
//    public void testGetResponse() {
//        ClientRequest request = new ClientRequest();
//        request.setContent("测试tcp长连接请求");
//        Response resp = TcpClient.send(request);
//        System.out.println(resp.getResult());
//    }
//
//    @Test
//    public void testSaveUser() {
//        ClientRequest request = new ClientRequest();
//        User u = new User();
//        u.setId(1);
//        u.setName("张三");
//
//        request.setContent(u);
//        request.setCommand("com.wsw.user.controller.UserController.saveUser");
//        Response resp = TcpClient.send(request);
//        System.out.println(resp.getResult());
//    }
//
//    @Test
//    public void testSaveUsers() {
//        ClientRequest request = new ClientRequest();
//        List<User> users = new ArrayList<User>();
//        User u = new User();
//        u.setId(1);
//        u.setName("张三");
//        users.add(u);
//
//        request.setContent(users);
//        request.setCommand("com.wsw.user.controller.UserController.saveUsers");
//        Response resp = TcpClient.send(request);
//        System.out.println(resp.getResult());
//    }
//}
