package daniel.robot.glWindow;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.FPSAnimator;

import daniel.robot.SLAM.SLAM;
import daniel.robot.SLAM.SavedRobotReadings;
import daniel.robot.glWindow.controller.MetaController;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.RobotModel;
import daniel.robot.glWindow.view.MetaView;
import daniel.robot.simulation.SimulationReadings;
import daniel.robot.SLAM.TrueRobotReadings;

public class Main {
	protected static final int CANVAS_WIDTH = 1024;
	protected static final int CANVAS_HEIGHT = 640;
	protected static final int FPS = 60;
	private static final String TITLE = "Camcam";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Run the GUI codes in the event-dispatching thread for thread safety
	      SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	        	
	        	IRobotInterface robotInterface;
				try {
					//model = new RobotModel("127.0.0.1");
					
					//robotInterface = new SavedRobotReadings("Sat Feb 22 09:06:08 CET 2014");
					
					robotInterface = new TrueRobotReadings(new RobotModel("127.0.0.1"));
					//robotInterface = new TrueRobotReadings(new RobotModel("192.168.1.6"));
					
				//	robotInterface = new SimulationReadings();
					SLAM slam = new SLAM(robotInterface);
					
					
					//SLAM slam = new SLAM(new TrueRobotReadings(model));
					//model = new RobotModel("192.168.2.3");
					//model = new RobotModel("192.168.1.6");
				
					Dimension windowSize = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
		        	MetaView canvas = new MetaView(robotInterface, slam.m_world, windowSize);
		        	
		        	MetaController controller = new MetaController();
		            
		            canvas.setPreferredSize(windowSize);
	
		            // Create a animator that drives canvas' display() at the specified FPS. 
		            final FPSAnimator animator = new FPSAnimator(new MVCAdapter(controller, canvas), FPS, true);
		            
		            // Create the top-level container
		            final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
		            frame.getContentPane().add(canvas);
		            frame.addWindowListener(new WindowAdapter() {
		               @Override 
		               public void windowClosing(WindowEvent e) {
		                  // Use a dedicate thread to run the stop() to ensure that the
		                  // animator stops before program exits.
		                  new Thread() {
		                     @Override 
		                     public void run() {
		                        if (animator.isStarted()) 
		                        	animator.stop();
		                        System.exit(0);
		                     }
		                  }.start();
		               }
		            });
		            frame.setTitle(TITLE);
		            frame.pack();
		            frame.setVisible(true);
		            animator.start(); // start the animation loop
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	            
	         }
	      });
		
		
		
		
		
	}
}
