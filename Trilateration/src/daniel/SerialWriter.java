package daniel;

import java.io.IOException;
import java.io.OutputStream;

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
            	for (int i = 0; i < buffer.length();i++) {
            		this.out.write(buffer.charAt(i));
            	}
            	
            	Thread.sleep(1);
            	
            }                
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