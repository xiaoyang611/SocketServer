package xiaoyang.server.core;

import java.util.ArrayList;
import java.util.List;

import xiaoyang.server.config.Configuration;
import xiaoyang.server.config.ServerConfig;

/**
 * @author 肖扬
 * 服务器工厂,返回Server对象
 */
public class ServerFactory {
	
	public static Server server;
	
	public static Configuration config ;
	public static List<Server> servers;
	
	public static Server createServer(int port,ServerHandler responseHandler){
		
		if(null == server){
			server= new Server(port, responseHandler);
		}
		
		return server;
	}
	
	public static Server createServer(int port,int minThread,int maxThead,ServerHandler responseHandler){
		
		if(null == server){
			server= new Server(port,minThread,maxThead, responseHandler);
		}
		
		return server;
	}
	
	public static List<Server> createServer(String configFilePath){
		
		if(null == config){
			config=new Configuration(configFilePath);
			List<ServerConfig> configs=config.getServerConfigs();
			servers=new ArrayList<Server>();
			
			for (ServerConfig serverConfig : configs) {
				servers.add(new Server(serverConfig));
			}
			
		}
		
		return servers;
	}
}
