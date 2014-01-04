package daniel;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;



public class SerialPort extends RobotPort {
	
	
	@SuppressWarnings("unchecked")
	public static List<SerialPort> connectAndInitialize () throws Exception
    {
		System.out.println("SerialPort::connectAndInitialize Trying to find ports");
		List<SerialPort> ports = new ArrayList<SerialPort>();
    	Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();
    	
    	while(identifiers.hasMoreElements()) {
    		CommPortIdentifier identifier = identifiers.nextElement();
    		
    		System.out.println("SerialPort::connectAndInitialize Port: " + identifier.getName());
    		
    		SerialPort port = new SerialPort(identifier);
    		
    		ports.add(port);
    	}
    	
    	System.out.println("SerialPort::connectAndInitialize Done identifying ports");
    	
    	return ports;
    }
	
	public String getName() {
		return portIdentifier.getName();
	}
	
	
	
	public String toString() {
		return portIdentifier.toString();
	}
	
	
	
	private CommPortIdentifier portIdentifier;
	private gnu.io.SerialPort serialPort;
	private Thread readThread;
	private Thread writeThread;
	
	private SerialPort(CommPortIdentifier identifier) throws Exception {
		this.portIdentifier = identifier;
		
		setupPort();
	}
	
	
	
	private void setupPort() throws Exception {
		if ( portIdentifier.isCurrentlyOwned() )
        {
            throw new Exception("Error: Port is currently in use " + portIdentifier.getName());
            
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            
            if ( commPort instanceof gnu.io.SerialPort )
            {
        		serialPort = (gnu.io.SerialPort) commPort;
        		serialPort.setSerialPortParams( 57600,
								        		gnu.io.SerialPort.DATABITS_8, 
								        		gnu.io.SerialPort.STOPBITS_1,
								        		gnu.io.SerialPort.PARITY_NONE);
                
                InputStream in   = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                readThread  = new Thread(new SerialReader(in, readBuffer));
                writeThread = new Thread(new SerialWriter(out, writeBuffer));
                
                readThread.start();
                writeThread.start();
            }
            else
            {
            	throw new Exception("Error: Only serial ports are handled by this example.");
            }
        }
    	
    }
	
	

	
}
