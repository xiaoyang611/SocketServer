package xiaoyang.server.core;

import java.net.Socket;

public class Request {
	
	private Socket requestSocket;

	public Request(Socket requestSocket) {
		this.requestSocket = requestSocket;
	}

	public Socket getRequestSocket() {
		return requestSocket;
	}

	public void setRequestSocket(Socket requestSocket) {
		this.requestSocket = requestSocket;
	}
	
	
}
