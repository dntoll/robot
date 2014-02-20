package daniel.robot.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class RobotClient {
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
		@SuppressWarnings("resource")
		//Socket server = new Socket("localhost", 6789);
		Socket server = new Socket("192.168.1.6", 6789);
        System.out.println("Client found robot");
		int d;
		while (true) {
			if ( System.in.available() > 0) {
				d = System.in.read();
				server.getOutputStream().write(1);
				server.getOutputStream().write(d);
			}
			if ( server.getInputStream().available() > 1) {
				int protocol = server.getInputStream().read();
				if (protocol == 0) {
					server.getInputStream().read();
					//keep alive
					server.getOutputStream().write(0);
					server.getOutputStream().write(0);
				} else {
					d = server.getInputStream().read();
					System.out.write(d);
				}
			}
			//Thread.sleep(10);
		}
	}
}
