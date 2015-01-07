# SocketServer
Java  multiple thread socket server ,based BIO

usage:

  Extends abstract class "ServerHandler",implements the mothod "process(request)":

  public class MyServerHandler extends ServerHandler {
	
  	Logger logger=ServerLoggerFactory.getLogger(MyServerHandler.class);

	  @Override
  	public void process(Request request) {
	
		  Socket requestSocket=request.getRequestSocket();
		
		  //...
		
	  }

  }
	
	If needed,there are interceptors you can assign before handler is invoked:
	public class Mynterceptor1 implements Interceptor{

	  @Override
	  public void intercept(InvocationHandle handler) {
		  System.out.println("Mynterceptor1 before invoke()");
	    handler.invoke();
		  System.out.println("Mynterceptor1 after invoke()");
  	}
	
  }
  
  
Then,write the config file,for example:

<?xml version="1.0" encoding="UTF-8"?>
<config>
	<log-config  file="d:\log.txt" level="INFO" />
	<servers>
  		<server>
  			<port value="12121" />
  			<pool minThread="1" maxThread="10" />
  			<interceptors>
  				<interceptor class="xiaoyang.server.sample.simple.Mynterceptor1" />
  				<interceptor class="xiaoyang.server.sample.simple.Mynterceptor2" />
  			</interceptors>
  			<serverHandler class="xiaoyang.server.sample.simple.MyServerHandler" />
  		</server>
  	 
  		<server>
  			<port value="12122" />
  			<pool minThread="5" maxThread="10" />
  			<serverHandler class="xiaoyang.server.sample.simple.MyServerHandler" />
  		</server>
  		
  	</servers>
	
  </config>
  
  
  Last,start the serveres from config file :

  List<Server> servers=ServerFactory.createServer("config.xml");
	for (Server server : servers) {
			server.start();
	}
	
	Or:
	
	Server server=ServerFactory.createServer(12121, 1,5,new MyServerHandler());
	server.start();
	
