/*package xiaoyang.server.config;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

*//**
 * @author 肖扬
 * 配置类，用于读取配置文件，并保存配置信息。
 *//*
public class Configuration4dom4j {
	private String configFilePath;
	private List<ServerConfig> serverConfigs=new ArrayList<ServerConfig>();
	
	public Configuration4dom4j(String configFilePath){
		this.configFilePath=configFilePath;
		this.parseConfigFile();
	}

	*//**
	 * 读取配置文件
	 *//*
	@SuppressWarnings("unchecked")
	public List<ServerConfig> parseConfigFile(){
		
		if(configFilePath==null){
			throw new RuntimeException("please assign a config file path!");
		}
		
		SAXReader reader=new SAXReader();
		Document doc;
		
		try {
			doc=reader.read(this.getClass().getClassLoader().getResourceAsStream(configFilePath));
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("load config file path occured  an  error！");
		}
		
		
		List<Node> serverNodes=doc.selectNodes("/servers/server");
		ServerConfig sc=null;
		
		for (Node node : serverNodes) {
			 Node  portNode= node.selectSingleNode("port");
			 String portStr=portNode.valueOf("@value");
			 
			 if(portStr == null || "".equals(portStr)){
				 throw new RuntimeException("please assign the port parameter in the config file");
			 }
			 
			 //解析端口号
			 Integer port;
			 
			 try {
				port = Integer.parseInt(portStr);
			 } catch (NumberFormatException e1) {
				e1.printStackTrace();
				throw new RuntimeException("please assign a right port parameter in the config file");
			 } 
			 
			 //解析服务器端处理逻辑类
			 Node serverHandlerNode=node.selectSingleNode("serverHandler");
			 String serverHandlerClassName=serverHandlerNode.valueOf("@class");
			 
			 sc=new ServerConfig(port, serverHandlerClassName);
			 
			 //解析线程池设置
			 Node poolNode=node.selectSingleNode("pool");
			 
			 if(poolNode!=null){
				 String minThread=poolNode.valueOf("@minThread");
				 String maxThread=poolNode.valueOf("@maxThread");
				 
				 if(null!=minThread && !"".equals(minThread)){
					 try {
						sc.setMinThread(Integer.parseInt(minThread));
					} catch (NumberFormatException e) {
						e.printStackTrace();
						throw new RuntimeException("please assign a right minThread parameter in the config file");
					}
				 }
				 
				 if(null!=maxThread && !"".equals(maxThread)){
					 try {
						sc.setMaxThread(Integer.parseInt(maxThread));
					} catch (NumberFormatException e) {
						e.printStackTrace();
						throw new RuntimeException("please assign a right maxThread parameter in the config file");
					}
				 }
				 
			 }
			 
			 //解析拦截器
			 List<Node> intercepterNodeList=node.selectNodes("./interceptors/interceptor");
			 for (Node interceptorNode : intercepterNodeList) {
				 sc.addInterceptorClassName(interceptorNode.valueOf("@class"));
			 }
			 
			 serverConfigs.add(sc);
		}
		
		return serverConfigs;
	}
	
	

	public List<ServerConfig> getServerConfigs() {
		return serverConfigs;
	}

	public void setServerConfigs(List<ServerConfig> serverConfigs) {
		this.serverConfigs = serverConfigs;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	
	public static void main(String[] args) {
		Configuration4dom4j conifg=new Configuration4dom4j("config.xml");
		conifg.parseConfigFile();
	}
	
}
*/