package xiaoyang.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import xiaoyang.server.config.ServerConfig;
import xiaoyang.server.interceptor.Interceptor;
import xiaoyang.server.log.ServerLoggerFactory;

/**
 * @author Ф��
 * socket�������ˣ����ڽ������󣬵��ô���������������ά���������̳߳ء�
 */
public class Server implements Runnable{
	
	Logger logger=ServerLoggerFactory.getLogger(Server.class);
	
	private int port;
	private int minThread=1;
	private int maxThread=10;
	private ServerHandler serverHandler;
	private List<Interceptor> interceptors=new ArrayList<Interceptor>();
	private Queue<Socket> requestSocketQueue= new LinkedBlockingDeque<Socket>(100);//����socket����
	
	private boolean isStarted=false;
	
	public Server(int port,ServerHandler responseHandler){
		this.port=port;
		this.serverHandler=responseHandler;
	} 
	
	public Server(int port,int minThread,int maxThread,ServerHandler responseHandler){
		this.port=port;
		this.minThread=minThread;
		this.maxThread=maxThread;
		this.serverHandler=responseHandler;
	}
	
	public Server(int port,int minThread,int maxThread,ServerHandler responseHandler,List<Interceptor> interceptors){
		this.port=port;
		this.minThread=minThread;
		this.maxThread=maxThread;
		this.serverHandler=responseHandler;
		this.interceptors=interceptors;
	}
	
	
	public Server(ServerConfig config){
		this.port=config.getPort();
		this.minThread=config.getMinThread();
		this.maxThread=config.getMaxThread();
		
		try {
			this.serverHandler=(ServerHandler) Class.forName(config.getServerHandlerClassName()).newInstance();
		} catch (InstantiationException e) {
			String msg="can not initialize the assigned ServerHandler class:"+config.getServerHandlerClassName();
			logger.log(Level.SEVERE, msg, e);
			throw new RuntimeException(msg);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, " ", e);
			throw new RuntimeException();
		} catch (ClassNotFoundException e) {
			String msg="can not find the assigned ServerHandler class:"+config.getServerHandlerClassName();
			logger.log(Level.SEVERE, msg, e);
			throw new RuntimeException(msg);
		}
		
		List<String> interceptorClassNames=config.getInterceptorClassNames();
		Interceptor interceptor=null;
		
		for (String interceptorName : interceptorClassNames) {
			
			try {
				interceptor=(Interceptor) Class.forName(interceptorName).newInstance();
			} catch (InstantiationException e) {
				String msg="can not initialize the assigned interceptor class:"+interceptorName;
				logger.log(Level.SEVERE, msg, e);
				throw new RuntimeException(msg);
			} catch (IllegalAccessException e) {
				String msg="IllegalAccessException";
				logger.log(Level.SEVERE, msg, e);
				throw new RuntimeException(msg);
			} catch (ClassNotFoundException e) {
				String msg="can not find the assigned interceptor class:"+interceptorName;
				logger.log(Level.SEVERE, msg, e);
				throw new RuntimeException(msg);
			}
			
			interceptors.add(interceptor);
		}
		
		
	}
	
	public void start(){
		if(!isStarted){
			Thread t=new Thread(this);
			t.start();
			isStarted=true;
		}else{
			String msg="the server using port ["+port+"] has already started!";
			logger.info(msg);
			throw new RuntimeException(msg);
		}
		
	}
	
	@Override
	public void run() {
		
		Thread.currentThread().setName("Server Thread port["+port+"]");
		
		ServerSocket server=null;
		
		try {
			server=new ServerSocket(port);
		} catch (IOException e) {
			String msg="server started error...";
			logger.log(Level.SEVERE, msg, e);
			System.exit(1);
		}
		
		logger.info("server used port ["+port+"] has started!");
		
		ServerProcessThreadPool processorPool=new ServerProcessThreadPool(requestSocketQueue);//�������̳߳أ�ά����������Լ��������̳߳�
		logger.info("initialized processor thread pool for server["+port+"]!");
		
		while(true){
			Socket socket=null;
			
			try {
				socket=server.accept();
				processorPool.socketEnqueue(socket);//socket�����
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} catch(Exception e){
				e.printStackTrace();
				continue;
			}
			
		}
	}

	/**
	 * �������̳߳�
	 * ά��socket���У���Ϊsocket���䴦���߳�
	 * @author Ф��
	 */
	class ServerProcessThreadPool extends Thread {
		private int minProcessor=minThread;
		private int maxProcessor=maxThread;
		private int curProcessor=0;
		
		private Queue<Socket> requestSocketQueue;
		
		private Stack<Processor> pool=new Stack<Processor>();
		
		ServerProcessThreadPool(Queue<Socket> requestSocketQueue){
			this.requestSocketQueue=requestSocketQueue;
			initPool();
			this.start();
		}
		
		/**
		 * �������,
		 */
		public   void socketEnqueue(Socket s) throws Exception {
			
			synchronized(this.requestSocketQueue){
				
				if(this.requestSocketQueue.size()<=100){//���ö��г���Ϊ100
					
					if(this.requestSocketQueue.isEmpty()){
						this.requestSocketQueue.offer(s);
						this.requestSocketQueue.notify();//�����߳�
					}else{
						this.requestSocketQueue.offer(s);
					}
					
				}else{
					throw new RuntimeException(" requestSocketQueue is too long ");
				}
				
			}
			
		}
		
		/**
		 * �������
		 */
		public Socket  socketDequeue(){
			
			synchronized(this.requestSocketQueue){
				
				Socket s=null;
				
				if(!this.requestSocketQueue.isEmpty()){
					s=this.requestSocketQueue.poll();
				}
				
				return s;
			}
			
		}
		
		Processor createProcessor(){
			Processor p=null;
			if(curProcessor<maxProcessor){
				p=new Processor(serverHandler,interceptors,this);
				pool.push(p);
				curProcessor++;
			}
			return p;
		}
		
		/**
		 * ��һ������������������ء�
		 */
		void rePush(Processor p){
			synchronized(pool){
				this.pool.push(p);
			}
		}
		
		/**
		 * ��ȡһ�����������󡣵����Ӳ�Ϊ����ֱ�ӵ���һ��Ԫ�أ�Ϊ�����½�һ����������������ٵ�����
		 * @return Processor ����������
		 */
		Processor obtainProcessor(){
			synchronized(pool){
				Processor p=null;
				if(!this.pool.empty()){
					p=pool.pop();
				}else{
					p=createProcessor();
					if(p!=null){
						p=pool.pop();
					}
				}
				return p;
			}
		}
		
		private void initPool(){
			while(curProcessor<minProcessor){
				if(curProcessor<maxProcessor){
					createProcessor();
				}
			}
			logger.info("ServerProcessThreadPool initialed");
		}

		@Override
		public void run() {
			Thread.currentThread().setName("ServerProcessThreadPool Thread["+Thread.currentThread().getName()+"]");
			logger.info("ServerProcessThreadPool Thread started!");
			
			while(true){
				doProcess();
			}
			
		}

		private  void doProcess() {
			
			if(!this.requestSocketQueue.isEmpty()){
				Processor processor= this.obtainProcessor();
				
				if(processor!=null){
					Socket socket=socketDequeue();
					
					if(!socket.isClosed() && socket.isConnected()){
						Request request=new Request(socket);
						processor.asign(request);
					}
					
				}
				
			}else{
				
				try {
					
					synchronized (this.requestSocketQueue) {
						this.requestSocketQueue.wait();//�������Ϊ�գ�����
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		

	}
	
}
