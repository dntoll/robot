package daniel;

import java.io.IOException;
import java.io.InputStream;

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