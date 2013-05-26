package daniel.trilateration;

import java.util.ArrayList;
import java.util.List;

import daniel.SerialPort;


public class TrilaterationRunner
{
    public TrilaterationRunner()
    {
        super();
    }
    
    
    public static void main ( String[] args )
    {
        try
        {
            List<SerialPort> ports = SerialPort.connectAndInitialize();
            
            ArrayList<MicrophoneCircuit> microphones = new ArrayList<MicrophoneCircuit>(); 
            
            float initialDistance = 40.0f;
            for(SerialPort port : ports) {
            	MicrophoneCircuit mic = new MicrophoneCircuit(port);
            	microphones.add(mic);
            	
            }
            
            Thread.sleep(5000);
            
            //Calibrate
           for (int i = 0;i< MicrophoneCircuit.NUM_CALIBRATIONS; i++) {
            	
            	for (int m = microphones.size()-1; m >= 0 ; m--) {
            		MicrophoneCircuit mic = microphones.get(m);
            	
            		mic.send();
            		
            	}
            	Thread.sleep(100);
            	for(MicrophoneCircuit mic : microphones) {
            		mic.readDistance(i, initialDistance);
	            }
	            
	            Thread.sleep(300);
	            //System.out.println("d");
            }
            
            System.out.println("Done Calibrating");
            for(MicrophoneCircuit mic : microphones) {
            	mic.doneCalibration();
            	mic.startAccumulate();
            }
            while(true) {
	            Thread.sleep(5000);
	            
	            for (int i  = 0; i< MicrophoneCircuit.NUM_CALIBRATIONS; i++) {
	            	for (int m = microphones.size()-1; m >= 0 ; m--) {
	            		MicrophoneCircuit mic = microphones.get(m);
	            		mic.send();
	            	}
	            	
	            	//Thread.sleep(100);
	            	for(MicrophoneCircuit mic : microphones) {
	            		float distance = mic.readDistance(i, mic.error);
	            		System.out.print(mic.getName() + " : ");
	            		System.out.println(distance);
		            }
	            	Thread.sleep(300);
		        }
	            for(MicrophoneCircuit mic : microphones) {
	            	float distance = mic.getMedian();
	            	System.out.print(mic.getName() + " Median Distance : ");
	        		System.out.println(distance);
	            }
            }
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}