package xiaoyang.server.sample.longconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import xiaoyang.server.core.Request;
import xiaoyang.server.core.ServerHandler;
import xiaoyang.server.log.ServerLoggerFactory;

public class LongconnetServerHandler extends ServerHandler {
	
	Logger logger=ServerLoggerFactory.getLogger(LongconnetServerHandler.class);

	private static final int BUFFER_SIZE=512;
	
	@Override
	public void process(Request request) {
		
		Socket s=request.getRequestSocket();
	    new ServerWriter(s);
		new ServerReader(s);
	
	}
	
	private static class ServerReader implements Runnable {

		private Socket incoming;
		private boolean done=false;
		
		ServerReader(Socket incoming) {
			this.incoming = incoming;
			new Thread(this).start();
		}

		@Override
		public void run() {
			
			Thread.currentThread().setName("ServerReader:"+Thread.currentThread().getName());
			
			InputStream in = null;

			try {
				in = incoming.getInputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				StringBuffer sb=new StringBuffer(32);
				
				while(!done){
					
					int len;
					
					if ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
						sb.append(new String(buffer, 0, len));
					}
					
					System.out.println("from client:"+ sb.toString());
					
					if(sb.toString().contains("~end!")){
						done=true;
					}
					
					sb.delete(0, sb.length());
					
				}
				
			} catch (Exception e) {
				System.out.println("error--> ServerReader thread :"+Thread.currentThread().getName());
				e.printStackTrace();
			} finally {
				
				try {
					
					if (incoming != null && !incoming.isClosed()){
						incoming.shutdownInput();
					}
					
					if(incoming.isOutputShutdown() && !incoming.isClosed()){
						incoming.close();
						System.out.println("closed request socket --> ServerReader thread :"+Thread.currentThread().getName());
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private static class ServerWriter implements Runnable {

		private Socket incoming;

		ServerWriter(Socket incoming) {
			this.incoming = incoming;
			new Thread(this).start();
		}

		@Override
		public void run() {
			
			Thread.currentThread().setName("ServerWriter:"+Thread.currentThread().getName());

			OutputStream out = null;
			try {

				out = incoming.getOutputStream();
				String str=null;
				Scanner reader=new Scanner(System.in);
				
				while(true){
					
					
					System.out.println("input-->");
					
					if(reader.hasNextLine()){
						str=reader.nextLine();
					}
					
					out.write(str.getBytes());
					out.flush();
				}
				
			} catch (Exception e) {
				
				System.out.println("error--> ServerWriter thread :"+Thread.currentThread().getName());
				e.printStackTrace();
				
			} finally {
				
				try {
					
					if (incoming != null){
						incoming.shutdownOutput();
					}
					if(incoming.isInputShutdown()){
						incoming.close();
						System.out.println("closed request socket --> ServerWriter thread :"+Thread.currentThread().getName());
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

	}

	
	

}
