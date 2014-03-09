package daniel.robot.sensors;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import daniel.robot.Direction;

public class CameraSensor implements Runnable {
	
	
	private static final int IMAGE_WIDTH = 1024;
	private static final int IMAGE_HEIGHT = 120;
	private BufferedImage panorama = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private String serverAdress;
	private Thread thread;
	private Direction lastDirection;
	private boolean hasUsedThisDirection = true;
	
	
	public CameraSensor(String serverAdress) {
		this.serverAdress = serverAdress;
		
		thread = new Thread(this);
		
		thread.start();
	}

	private synchronized void setImage(BufferedImage image) {
		
		if (hasUsedThisDirection == false) {
			Graphics gl = panorama.getGraphics();
			float camdegrees = 35.5f;
			float camvdegrees = 15.4f;
			float sweepDegrees = 180.0f + camdegrees;
			float startDegree = camdegrees / 2.0f;
			float pixelsPerDegree = (float)IMAGE_WIDTH/sweepDegrees;
			
			gl.drawImage(image, (int)((180-lastDirection.getHeadingDegrees())*pixelsPerDegree), 0, (int)(pixelsPerDegree * camdegrees), (int)(camvdegrees * pixelsPerDegree), null);
			hasUsedThisDirection = true;
		}
		//gl.
		//gl.dispose();
		
	}
	
	public synchronized BufferedImage getPanoramaImage() {
		return panorama;
	}
		
	@Override
	public void run() {
		Socket server;
		DataInputStream din;
		try {
			server = new Socket(serverAdress, 6791);
			din = new DataInputStream(server.getInputStream());
		
	        System.out.println("Client found robot camera");
			
			while (true) {
				
				if (din.available() > 0) {
					int size = din.readInt();
					//System.out.println("read integer" + size);
					byte[] buffer = new byte[size];
					ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
					
					server.getInputStream().read(buffer);
					
					
					BufferedImage image = ImageIO.read(bais);
					setImage(image);
				}
				
				Thread.sleep(40);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public synchronized void update(Direction direction) {
		
		if (direction != null && this.lastDirection != null && direction.getHeadingDegrees() != this.lastDirection.getHeadingDegrees())
			hasUsedThisDirection = false;
		this.lastDirection = direction;
		
	}
	
}
