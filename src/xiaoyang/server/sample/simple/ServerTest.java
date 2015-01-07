package xiaoyang.server.sample.simple;

import xiaoyang.server.core.Server;
import xiaoyang.server.core.ServerFactory;

public class ServerTest {
	
	public static void main(String[] args) {
		
		//Æô¶¯server
		Server server=ServerFactory.createServer(12121, 1,5,new MyServerHandler());
		server.start();
		
	}
		
}
