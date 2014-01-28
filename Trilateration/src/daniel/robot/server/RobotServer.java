package daniel.robot.server;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import daniel.SerialReader;
import daniel.SerialWriter;



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
			long keepAliveSent = System.currentTimeMillis();
			
				
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
								keepAliveSent, port, iterations);
					} while(quit != true);
					
					System.out.println("stopped communicating");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("closed robot");
				port.close();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private boolean communicate(Socket clientSocket,
			long keepAliveSent, SerialPort port, int iterations)
			throws IOException, InterruptedException {
		int d;
		if ( port.getInputStream().available() > 0) {
			d = port.getInputStream().read();
			clientSocket.getOutputStream().write(1);
			clientSocket.getOutputStream().write(d);
		} else 	if ( clientSocket.getInputStream().available() > 1) {
			int protocol = clientSocket.getInputStream().read();
			if (protocol == 0) {
				clientSocket.getInputStream().read();
				//keep alive
				keepAliveReceived = System.currentTimeMillis();
			} else {
				d = clientSocket.getInputStream().read();
				port.getOutputStream().write(d);
			}
		} else {
			Thread.sleep(10);
		}
		Thread.sleep(0, 10);
		//
		
		//Send keep alive
		iterations++;
		if (System.currentTimeMillis() - keepAliveSent > 1000) {
			clientSocket.getOutputStream().write(0);
			clientSocket.getOutputStream().write(0);
			//System.out.println("keep alive sent");
			keepAliveSent = System.currentTimeMillis();
			//Thread.sleep(10);
		}
		
		if (System.currentTimeMillis() - keepAliveReceived > 10000) {
			
			System.out.println("Timeout :"  + (int)(System.currentTimeMillis() - keepAliveReceived));
			return true;
		}
		
		return false;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private SerialPort findPort(String portID) throws Exception {
		Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();
		
		while(identifiers.hasMoreElements()) {
    		CommPortIdentifier identifier = identifiers.nextElement();
    		
    		System.out.println("Found identifier" + identifier.getName());
    		
    		if (identifier.getName().equals(portID)) {
    		
	    		if ( identifier.isCurrentlyOwned() )
	            {
	                throw new Exception("Error: Port is currently in use " + identifier.getName());
	            }
	            else
	            {
	                CommPort commPort =  identifier.open(this.getClass().getName(), 2000);
	                
	                if ( commPort instanceof gnu.io.SerialPort )
	                {
	            		SerialPort serialPort = (gnu.io.SerialPort) commPort;
	            		serialPort.setSerialPortParams( 57600,
	    								        		gnu.io.SerialPort.DATABITS_8, 
	    								        		gnu.io.SerialPort.STOPBITS_1,
	    								        		gnu.io.SerialPort.PARITY_NONE);
	                    
	            		//if (serialPort.getName().equals(portID)) {
	            		    return serialPort;
	            		//}
	                }
	                else
	                {
	                	throw new Exception("Error: Only serial ports are handled by this example.");
	                }
	            }
    		}
		}
		throw new Exception("Error: No ports found");
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
		
	}

	
	
}
 