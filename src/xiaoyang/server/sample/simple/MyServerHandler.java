package xiaoyang.server.sample.simple;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Logger;

import xiaoyang.server.core.Request;
import xiaoyang.server.core.ServerHandler;
import xiaoyang.server.log.ServerLoggerFactory;

public class MyServerHandler extends ServerHandler {
	
	Logger logger=ServerLoggerFactory.getLogger(MyServerHandler.class);

	@Override
	public void process(Request request) {

		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Socket requestSocket=request.getRequestSocket();
		InputStream is=null;
		
		try {
			
			is = requestSocket.getInputStream();
			byte[] buf=new byte[512];
			int len;
			
			while((len=is.read(buf))!=-1){
				System.out.println(new String(buf, 0,len,"utf-8"));
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if(is!=null){
					is.close();
				}
				
				if(requestSocket!= null){
					requestSocket.close();
				}
				
			} catch (IOException e) {
				ServerLoggerFactory.throwRuntimeExceptionAndLog(logger, "resource release exception", e);
			}
			
		}
		
	}

}
