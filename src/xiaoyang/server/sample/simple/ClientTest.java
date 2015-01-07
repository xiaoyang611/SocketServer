package xiaoyang.server.sample.simple;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTest {

	public static void main(String[] args) {
		int i = 0;
		while (i < 120) {
			i++;
			// ¿Í»§¶Ë²âÊÔ´úÂë
			Socket client = null;

			try {
				client = new Socket(InetAddress.getLocalHost(), 12121);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			OutputStream out = null;

			try {
				out = client.getOutputStream();
				String msg = "Hello Server!" + i;
				out.write(msg.getBytes("utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					out.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			System.out.println(i);
		}

	}

}
