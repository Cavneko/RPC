# Introduction 
**Developed a high-performance, lightweight distributed RPC framework based on Netty**


# Features
- **Supported long connection**
- **Achieved asynchronous calls**
- **Supported heartbeat monitoring mechanism**
- **Implemented JSON-based sequences**
- **Built the annotation-based zero configuration framework**
- **Developed service registry based on ZooKeeper**
- **Service node monitoring and dynamic management function**
- **Provided client service discovery function**
- **Support server service registration function**
- **Based on Netty4.X version**

# Quick Start
### server development
- **Add your own Service under Service on the server side, and add the @Service annotation**
	<pre>
	@Service
	public class TestService {
		public void test(User user){
			System.out.println("调用了TestService.test");
		}
	}
	</pre>

- **Generate a service interface and generate a class that implements the interface**
	###### interface is as follows
	<pre>
	public interface TestRemote {
		public Response testUser(User user);  
	}
	</pre>
	###### The implementation class is as follows, add the @Remote annotation to your implementation class, this class is where you actually call the service and you can generate any kind of Response you want to return to the client

	<pre> 
	@Remote
	public class TestRemoteImpl implements TestRemote{
		@Resource
		private TestService service;
		public Response testUser(User user){
			service.test(user);
			Response response = ResponseUtil.createSuccessResponse(user);
			return response;
		}
	}	
	</pre>


### Client development
- **Generate an interface on the client side that is the interface you want to call**
	<pre>
	public interface TestRemote {
		public Response testUser(User user);
	}
	</pre>

### Usage
- **Generate a property in the form of an interface where you want to call it, and add the @RemoteInvoke annotation to the property**
	<pre>
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(classes=RemoteInvokeTest.class)
	@ComponentScan("\\")
	public class RemoteInvokeTest {
		@RemoteInvoke
		public static TestRemote userremote;
		public static User user;
		@Test
		public void testSaveUser(){
			User user = new User();
			user.setId(1000);
			user.setName("张三");
			userremote.testUser(user);
		}
	}	
	</pre>

### Rsult
- **10000 call results**
![Markdown](https://s1.ax1x.com/2018/07/06/PZMMBF.png)

- **100000 call results**
![Markdown](https://s1.ax1x.com/2018/07/06/PZM3N9.png)

- **1000000 call results**
![Markdown](https://s1.ax1x.com/2018/07/06/PZMY1x.png)



# Overview

![Markdown](https://s1.ax1x.com/2018/07/06/PZK3SP.png)
