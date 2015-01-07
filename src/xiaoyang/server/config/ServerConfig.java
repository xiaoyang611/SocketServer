package xiaoyang.server.config;

import java.util.ArrayList;
import java.util.List;


/**
 * @author –§—Ô
 * ∑˛ŒÒ∆˜≈‰÷√¿‡
 */
public class ServerConfig {
	private int port;
	private String serverHandlerClassName;
	private int minThread=1;
	private int maxThread=10;
	private List<String> interceptorClassNames=new ArrayList<String>();
	
	public ServerConfig(int port, String serverHandlerClassName) {
		this.port = port;
		this.serverHandlerClassName = serverHandlerClassName;
	}
	
	public ServerConfig(int port, String serverHandlerClassName,int minThread,int maxThread) {
		this.port = port;
		this.serverHandlerClassName = serverHandlerClassName;
		this.maxThread=maxThread;
		this.minThread=minThread;
	}
	
	
	public void addInterceptorClassName(String interceptorClassName){
		this.interceptorClassNames.add(interceptorClassName);
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServerHandlerClassName() {
		return serverHandlerClassName;
	}
	public void setServerHandlerClassName(String serverHandlerClassName) {
		this.serverHandlerClassName = serverHandlerClassName;
	}

	public int getMinThread() {
		return minThread;
	}

	public void setMinThread(int minThread) {
		this.minThread = minThread;
	}

	public int getMaxThread() {
		return maxThread;
	}

	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}

	public List<String> getInterceptorClassNames() {
		return interceptorClassNames;
	}

	public void setInterceptorClassNames(List<String> interceptorClassNames) {
		this.interceptorClassNames = interceptorClassNames;
	}

	
}
