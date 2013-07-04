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
public class RobotServer {
	
	private ServerSocket m_serverSocket;

	RobotServer() throws IOException {
		m_serverSocket = new ServerSocket(6789); 
	}
	
	private void run() throws IOException {
		
		
		while (true) {
			System.out.println("starting up and listening to 6789");
			Socket clientSocket = m_serverSocket.accept();
			System.out.println("accepted connection");
		
			long keepAliveReceived = System.currentTimeMillis();
			long keepAliveSent = System.currentTimeMillis();
			try {
				SerialPort port = findRobots();
				System.out.println("found robot");
				try {
					int iterations = 0;
					
					port.setDTR(true);
					Thread.sleep(10);
					port.setDTR(false);
					
					while(true) {
						
						int d;
						if ( port.getInputStream().available() > 0) {
							d = port.getInputStream().read();
							clientSocket.getOutputStream().write(1);
							clientSocket.getOutputStream().write(d);
						}

						if ( clientSocket.getInputStream().available() > 1) {
							int protocol = clientSocket.getInputStream().read();
							if (protocol == 0) {
								clientSocket.getInputStream().read();
								//keep alive
								keepAliveReceived = System.currentTimeMillis();
								//System.out.println("keep alive received");
							} else {
								d = clientSocket.getInputStream().read();
								port.getOutputStream().write(d);
							}
						}
						Thread.sleep(10);
						
						//Send keep alive
						iterations++;
						if (System.currentTimeMillis() - keepAliveSent > 1000) {
							clientSocket.getOutputStream().write(0);
							clientSocket.getOutputStream().write(0);
							//System.out.println("keep alive sent");
							keepAliveSent = System.currentTimeMillis();
						}
						
						if (System.currentTimeMillis() - keepAliveReceived > 10000) {
							break;
						}
					}
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
	
	
	
	@SuppressWarnings("unchecked")
	private SerialPort findRobots() throws Exception {
		Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();
		
		while(identifiers.hasMoreElements()) {
    		CommPortIdentifier identifier = identifiers.nextElement();
    		
    		System.out.println("Found identifier" + identifier.getName());
    		
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
                    
            		if (serialPort.getName().equals("COM14") || 
            				serialPort.getName().equals("/dev/ttyUSB0") ||
            				serialPort.getName().equals("/dev/ttyUSB1")) {
            		
            			
	                    return serialPort;
            		}
                }
                else
                {
                	throw new Exception("Error: Only serial ports are handled by this example.");
                }
            }
		}
		throw new Exception("Error: No ports found");
	}

	public static void main(String argv[]) throws Exception {
		RobotServer server =  new RobotServer();
		
		server.run();
		
	}

	
	
}
 