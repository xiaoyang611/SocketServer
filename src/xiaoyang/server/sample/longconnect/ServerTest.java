package xiaoyang.server.sample.longconnect;

import java.util.List;

import xiaoyang.server.core.Server;
import xiaoyang.server.core.ServerFactory;

public class ServerTest {
	
	public static void main(String[] args) {
		
		
		//Æô¶¯server
		List<Server> servers=ServerFactory.createServer("xiaoyang/server/sample/longconnect/config.xml");
		for (Server server : servers) {
			server.start();
		}
		
	}
		
}
