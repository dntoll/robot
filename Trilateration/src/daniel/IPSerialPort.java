package daniel;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class IPSerialPort extends RobotPort implements Runnable{
	
	Socket server;
	
	public IPSerialPort() throws UnknownHostException, IOException{
		this.server = new Socket("192.168.1.6", 6789);
        System.out.println("Client found robot");
        Thread t = new Thread(this);
        t.start();
    }

	@Override
	public void run() {
		
		
        try {
		int d;
		while (true) {
			
			String buffer = this.writeBuffer.read();
        	for (int i = 0; i < buffer.length();i++) {
        		d = buffer.charAt(i);
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
					
					this.readBuffer.write((byte)d);
					
				}
			}
			Thread.sleep(10);
		}
        } catch (Exception e) {
        	System.err.println(e.getStackTrace());
        }
	}
}
