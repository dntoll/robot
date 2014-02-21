package daniel.robot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import jssc.SerialPort;
import jssc.SerialPortException;



/**
 * This is the server running at the robot
 * Inspired by 
 * http://systembash.com/content/a-simple-java-tcp-server-and-tcp-client
 * @author daniel
 *
 */
public class RobotServer implements Runnable {
	
	private ServerSocket m_serverSocket;
	private String m_sensorsPort;
	private int m_socketPort;

	RobotServer(String sensorsPort, int socketPort) throws IOException {
		m_socketPort = socketPort;
		m_serverSocket = new ServerSocket(socketPort); 
		this.m_sensorsPort = sensorsPort;
	}
	
	long keepAliveReceived;
	long keepAliveSent;
	public void run() {
		
		
		while (true) {
			System.out.println("starting up and listening to " + m_socketPort);
			Socket clientSocket;
			try {
				clientSocket = m_serverSocket.accept();
			} catch (IOException e2) {
				
				e2.printStackTrace();
				break;
			}
			System.out.println("accepted connection");
		
			keepAliveReceived = System.currentTimeMillis();
			keepAliveSent = System.currentTimeMillis();
			
				
			try {
 				SerialPort port = findPort(m_sensorsPort);
				System.out.println("found robot");
				try {
					int iterations = 0;
					
					port.setDTR(true);
					Thread.sleep(10);
					port.setDTR(false);
					boolean quit = false;
					do {
						
						quit = communicate(clientSocket, 
								port, iterations);
					} while(quit != true);
					
					System.out.println("stopped communicating");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("closed robot");
				port.closePort();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private boolean communicate(Socket clientSocket,
			SerialPort port, int iterations)
			throws IOException, InterruptedException, SerialPortException {
		byte d;
		
		;
		
		if ( port.getInputBufferBytesCount() > 0) {
			while ( port.getInputBufferBytesCount() > 0) {
				d = port.readBytes(1)[0] ;;
				clientSocket.getOutputStream().write(1);
				clientSocket.getOutputStream().write(d);
			}
		} else 	if ( clientSocket.getInputStream().available() > 1) {

			while ( clientSocket.getInputStream().available() > 1) {
				int protocol = clientSocket.getInputStream().read();
				if (protocol == 0) {
					clientSocket.getInputStream().read();
					//keep alive
					keepAliveReceived = System.currentTimeMillis();
				} else {
					d = (byte) clientSocket.getInputStream().read();
					port.writeByte(d);
				}
			}
		} else {
			Thread.sleep(10);
		}
		Thread.sleep(0, 150);
		//
		
		//Send keep alive
		iterations++;
		if (System.currentTimeMillis() - keepAliveSent > 2000) {
			clientSocket.getOutputStream().write(0);
			clientSocket.getOutputStream().write(0);
			//System.out.println("keep alive sent");
			keepAliveSent = System.currentTimeMillis();
			//Thread.sleep(10);
		}
		
		if (System.currentTimeMillis() - keepAliveReceived > 20000) {
			
			System.out.println("Timeout :"  + (int)(System.currentTimeMillis() - keepAliveReceived));
			return true;
		}
		
		return false;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private SerialPort findPort(String portID) throws Exception {
		 SerialPort serialPort = new SerialPort(portID);
		 serialPort.openPort();
         serialPort.setParams(57600, 8, 1, 0);
         return serialPort;
	}

	public static void main(String argv[]) throws Exception {
		
		String sensorsPort = "/dev/ttyACM0";
		RobotServer sensorServer =  new RobotServer(sensorsPort, 6789);
		
		String motorPort = "/dev/ttyUSB0";
		RobotServer motorServer =  new RobotServer(motorPort, 6790);
		
		Thread sensors = new Thread(sensorServer);
		sensors.start();
		Thread motor = new Thread(motorServer);
		motor.start();
		
		String cameraPort = "/dev/video0";
		ImageServer cameraServer =  new ImageServer(cameraPort, 6791);
		
		
		Thread camera = new Thread(cameraServer);
		camera.start();
	}

	
	
}
 