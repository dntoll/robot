package daniel.robot.server;


import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;



public class ImageServer implements Runnable {
	/*static {
	    Webcam.setDriver(new GStreamerDriver());
	}*/
	
	private ServerSocket m_serverSocket;
	int m_socketPort;
	public ImageServer(String cameraPort, int port) throws IOException {
		m_socketPort = port;
		
	}

	public static void main(String argv[]) throws Exception {
		
		String cameraPort = "/dev/video0";
		ImageServer cameraServer =  new ImageServer(cameraPort, 6791);
		
		
		Thread camera = new Thread(cameraServer);
		camera.start();
		
		
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("starting up camera and listening to " + m_socketPort);
			Socket clientSocket;
			DataOutputStream out;
			try {
				m_serverSocket = new ServerSocket(m_socketPort); 
				clientSocket = m_serverSocket.accept();
				out = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e2) {
				
				e2.printStackTrace();
				break;
			}
			System.out.println("camera accepted connection");
		/*
			try {
 				Webcam camera = Webcam.getDefault();
 				
 				
 				camera.setViewSize(new Dimension(320,240));
				camera.open(false);
				System.out.println("found camera");
				try {
					
					boolean quit = false;
					do {
						Thread.sleep(250);
						
					
						BufferedImage image = camera.getImage();
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
						
						ImageIO.write(image, "JPG", bout);
						out.writeInt(bout.size());
						bout.writeTo(out);
						bout.reset();
						//sSystem.out.println("wrote image");
						
					} while(quit != true);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("closing camera");
					camera.close();
				}
				System.out.println("closed camera");
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
	}
}
