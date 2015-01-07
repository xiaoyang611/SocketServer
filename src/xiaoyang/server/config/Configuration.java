package xiaoyang.server.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author 肖扬
 * 配置类，用于读取配置文件，并保存配置信息。
 */
public class Configuration {
	private String configFilePath;
	private List<ServerConfig> serverConfigs=new ArrayList<ServerConfig>();
	
	public Configuration(String configFilePath){
		this.configFilePath=configFilePath;
		this.parseConfigFile();
	}

	/**
	 * 读取配置文件
	 */
	public List<ServerConfig> parseConfigFile() {
		
		if(configFilePath==null){
			throw new RuntimeException("please assign a config file path!");
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=null;
		Document doc=null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc=builder.parse(this.getClass().getClassLoader().getResourceAsStream(configFilePath));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("parse config file  occured  an  error！");
		}
		
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath  path = xpfactory.newXPath();
		
		try {
			//读取日志配置
			String logfileLocation= path.evaluate("//log-config/@file", doc);
			String logLevel=path.evaluate("//log-config/@level", doc);
			
			Global.put("file", logfileLocation);
			Global.put("level", logLevel);
			
			//读取服务器配置
			NodeList serverNodes=(NodeList) path.evaluate("//servers/server", doc, XPathConstants.NODESET);
			ServerConfig sc=null;
			
			for(int i=0,len=serverNodes.getLength();i<len;i++){
				 Node serverNode=serverNodes.item(i);
				 String  portStr=  (String) path.evaluate("./port/@value", serverNode,XPathConstants.STRING);
				 
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
				 String serverHandlerClassName=path.evaluate("serverHandler/@class", serverNode);
				 sc=new ServerConfig(port, serverHandlerClassName);
				 
				//解析线程池设置
				 Node poolNode=(Node) path.evaluate("pool", serverNode, XPathConstants.NODE) ;
				 
				 if(poolNode!=null){
					 String minThread=path.evaluate("@minThread", poolNode);
					 String maxThread=path.evaluate("@maxThread", poolNode);
					 
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
				 NodeList intercepterNodeList=(NodeList) path.evaluate("./interceptors/interceptor", serverNode, XPathConstants.NODESET);
				 
				 if(intercepterNodeList!=null && intercepterNodeList.getLength()>0){
					 
					 for(int j=0,len2=intercepterNodeList.getLength();j<len2;j++){
						 Node interceptorNode=intercepterNodeList.item(j);
						 sc.addInterceptorClassName(path.evaluate("@class", interceptorNode));
					 }
					 
				 }
				 
				 serverConfigs.add(sc);
				 
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			throw new RuntimeException("parse config file  occured  an  error！");
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
		Configuration conifg=new Configuration("config.xml");
		conifg.parseConfigFile();
	}
	
}
