package xiaoyang.server.sample.longconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;



public class Client {
	
	private static final int BUFFER_SIZE=1024; 
	
	public static void main(String[] args) {
		//while(true){
			Socket s=null;
			try {
				 s=new Socket("localhost", 12121);
				 
				 new ClientWriter(s).start();
				 new ClientReader(s).start();
				 
			} catch (Exception e) {
				System.out.println("error-->client main thread");
				e.printStackTrace();
			}
		//}
		
	}
	
	private static class ClientReader extends Thread {

		private Socket incoming;

		private boolean done;
		
		ClientReader(Socket incoming) {
			this.incoming = incoming;
		}

		@Override
		public void run() {

			
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
					
					System.out.println("from server:"+ sb.toString());
					
					if(sb.toString().contains("~end!")){
						done=true;
					}
					
					sb.delete(0, sb.length());
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				try {
					
					if (incoming != null && !incoming.isClosed()){ 
						incoming.shutdownInput();
					}
					if(incoming.isOutputShutdown() && !incoming.isClosed()){
						incoming.close();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private static class ClientWriter extends Thread {

		private Socket incoming;

		ClientWriter(Socket incoming) {
			this.incoming = incoming;
		}

		@Override
		public void run() {
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
				e.printStackTrace();
			} finally {

				try {
					if (incoming != null && !incoming.isClosed()) {
						incoming.shutdownOutput();
					}
					if(incoming.isInputShutdown() && !incoming.isClosed()){
						incoming.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

}
