package daniel;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;



public class SerialPort {
	
	
	public static List<SerialPort> connectAndInitialize () throws Exception
    {
		System.out.println("SerialPort::connectAndInitialize Trying to find ports");
		List<SerialPort> ports = new ArrayList<SerialPort>();
    	Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();
    	
    	while(identifiers.hasMoreElements()) {
    		CommPortIdentifier identifier = identifiers.nextElement();
    		
    		System.out.println("SerialPort::connectAndInitialize Port: " + identifier.getName());
    		
    		SerialPort port = new SerialPort(identifier);
    		port.setupPort();
    		ports.add(port);
    	}
    	
    	System.out.println("SerialPort::connectAndInitialize Done identifying ports");
    	
    	return ports;
    }
	
	public String getName() {
		return portIdentifier.getName();
	}
	
	public String read() {
		return readBuffer.read();
	}
	
	public void write(String data) {
		writeBuffer.write(data);
	}
	
	public String toString() {
		return portIdentifier.toString();
	}
	
	private class SyncronizedBuffer {
		public synchronized void write(String message) {
			buffer += message;
		}
		
		public synchronized String read() {
			if (buffer.contains("\n")) {
				int at = buffer.indexOf('\n');
				
				String ret = buffer.substring(0, at+1);
				
				buffer = buffer.substring(at+1);
				return ret;
			}
			
			return "";
		}
		
		
		private String buffer = "";
	}
	
	private SyncronizedBuffer readBuffer = new SyncronizedBuffer();
	private SyncronizedBuffer writeBuffer = new SyncronizedBuffer();
	
	private CommPortIdentifier portIdentifier;
	private gnu.io.SerialPort serialPort;
	private Thread readThread;
	private Thread writeThread;
	
	private SerialPort(CommPortIdentifier identifier) {
		this.portIdentifier = identifier;
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
        		serialPort.setSerialPortParams(57600,
        		gnu.io.SerialPort.DATABITS_8, 
        		gnu.io.SerialPort.STOPBITS_1,
        		gnu.io.SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                readThread = new Thread(new SerialReader(in, readBuffer));
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
	
	/** */
    public class SerialReader implements Runnable 
    {
        InputStream in;
		private SyncronizedBuffer readBuffer;
        
        public SerialReader ( InputStream in, SyncronizedBuffer readBuffer )
        {
            this.in = in;
            this.readBuffer = readBuffer;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                	String readFrom = new String(buffer,0,len);
                	this.readBuffer.write(readFrom);
                	//System.out.println("read : " + readFrom);
                }
                
                Thread.sleep(10);
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
        }
    }

    /** */
    public class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        SyncronizedBuffer writeBuffer;
        
        public SerialWriter ( OutputStream out, SyncronizedBuffer writeBuffer )
        {
            this.out = out;
            this.writeBuffer = writeBuffer;
        }
        
        public void run ()
        {
            try
            {                
                
                while ( true )
                {
                	String buffer = this.writeBuffer.read();
                	
                	/*if (buffer.length() > 0) {
                		this.out.write('c');
                		Thread.sleep(20);
                	}*/
                	for (int i = 0; i < buffer.length();i++) {
                		this.out.write(buffer.charAt(i));
                		
                		//System.out.println(getName() + " wrote: " + buffer.charAt(i));
                	}
                	
                	
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

	public String readSyncronosly() throws Exception {
		while(true) {
			String data = read();
			
			if (data != "") {
				return data;
			} else {
				Thread.sleep(100);
			
			}
		}
	}

	
}
