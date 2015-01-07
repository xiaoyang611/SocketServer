package xiaoyang.server.core;

import java.util.List;
import java.util.logging.Logger;

import xiaoyang.server.interceptor.Interceptor;
import xiaoyang.server.interceptor.InvocationHandle;
import xiaoyang.server.log.ServerLoggerFactory;
import xiaoyang.server.sample.simple.MyServerHandler;


/**
 * @author Ф��
 * �����������ڴ�������ÿ����������Ӧһ���߳�
 */
public class Processor implements Runnable{
	
	Logger logger=ServerLoggerFactory.getLogger(MyServerHandler.class);
	
	private Request request;
	private ServerHandler responseHandler;
	private List<Interceptor> interceptors;
	private boolean requestAvailable = false;
	private Server.ServerProcessThreadPool processorPool;
	
	Processor(ServerHandler responseHandler,List<Interceptor> interceptors,Server.ServerProcessThreadPool processorPool) {
		this.responseHandler=responseHandler;
		this.interceptors=interceptors;
		this.processorPool=processorPool;
		Thread t=new Thread(this);
		t.start();
	}

	 
	/**
	 * ��û������ɴ���ʱ���߳����ߡ�
	 */
	public synchronized Request await(){
		
		if(!requestAvailable){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		requestAvailable=false;
		Request request= this.request;
		notifyAll();
		return request;
		
	}
	
	/**
	 * �������󣬼����̡߳�
	 * @param request  �������
	 */
	public synchronized void asign(Request request){
		if(requestAvailable){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.request=request;
		requestAvailable=true;
		notifyAll();
	}
	
	private void process(Request request, ServerHandler responseHandler, List<Interceptor> interceptors){
		System.out.println("process thread is :"+Thread.currentThread().getName());
		System.out.println("processor  is :"+this.toString());
		InvocationHandle handler=new InvocationHandle(request, responseHandler, interceptors);
		handler.invoke();
		
	}
	
	
	@Override
	public void run() {
		
		Thread.currentThread().setName("Precessor Thread["+this.toString()+"]");

		while(true){
			
			Request request=await();
			if(request==null) continue;
			
			try {
				process(request,this.responseHandler,this.interceptors);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				processorPool.rePush(this);
			}
			
		}
		
	}
	

}
