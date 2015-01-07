package xiaoyang.server.sample.simple;

import java.util.List;

import xiaoyang.server.core.Server;
import xiaoyang.server.core.ServerFactory;

public class ServerTest2 {
	
	public static void main(String[] args) {
		
		//Æô¶¯server
		List<Server> servers=ServerFactory.createServer("xiaoyang/server/sample/simple/config.xml");
		for (Server server : servers) {
			server.start();
		}
		
	}
		
}
