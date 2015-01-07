package xiaoyang.server.interceptor;

import java.util.List;

import xiaoyang.server.core.Request;
import xiaoyang.server.core.ServerHandler;

/**
 * 调用处理器，持有多个拦截器对象的引用，维持各个拦截器的调用顺序。
 * @author 肖扬
 */
public class InvocationHandle {
	
	//拦截器列表
	private List<Interceptor> interceptors;
	
	//当前该调用的拦截器列表索引
	private int interceptorIndex=0;
	
	//服务端处理逻辑对象以及对应的请求对象
	private ServerHandler action;
	private Request request;
	
	public InvocationHandle(Request request,ServerHandler action,List<Interceptor> interceptors){
		this.request=request;
		this.interceptors=interceptors;
		this.action=action;
	}
	
	public void invoke(){
		
		if(interceptorIndex==this.interceptors.size()){
			action.process(request);
			return ;
		}
		
		this.interceptors.get(interceptorIndex++).intercept(this);
	}
	
}
