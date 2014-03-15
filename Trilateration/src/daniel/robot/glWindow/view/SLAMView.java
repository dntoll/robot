package daniel.robot.glWindow.view;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.IMap;
import daniel.robot.glWindow.model.IPose;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.State;
import daniel.robot.slam.map.lm.Map;

public class SLAMView {
	private CameraView cameraView = new CameraView();
	
	private CompassView compass;
	private DistanceMeasurementView distances;
	private PoseCollection world;

	private ViewCore core;

	private IRobotInterface robot;
	
	public SLAMView(IRobotInterface robot, PoseCollection world, ViewCore core ) {
		this.world = world;
		this.core = core;
		this.robot = robot;
		compass = new CompassView(core);
		distances = new DistanceMeasurementView(core);
	}
	
	
	public void doSlam(GL2 gl, GLU glu, Dimension windowSize) {
		cameraView.drawCamera(gl, glu, windowSize);
	    
	    try {
		    if (world.getLastPose() != null) {
			    IMap lastMap = world.getLastPose().getBestMap();
			    State bestKnownPosition = world.getLastPose().getBestGuessPosition();
			    distances.drawMap(gl,lastMap, bestKnownPosition, world.getLastPose().getParticleFilter()); 
		    }
	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	    }
	    if (robot.getDistanceSensorReadings() != null) {
	    	distances.drawTopDown(gl, robot.getDistanceSensorReadings(), new daniel.robot.glWindow.model.State(new Point2D.Float(450,0), new Direction(0)));
	    }
	    
	   // compass.drawCompassArrow(gl, robot.getCompass());
	    
	   /* float temp = robot.getTemperature();
	    if (robot.getCompassDirection() != null) {
	    	
	    	gl.glLoadIdentity(); 
		    float direction = robot.getCompassDirection().getHeadingDegrees();
		    String out = "Temperature " + temp + " direction " + direction; 
		    core.renderStrokeString(gl, out); // Print GL Text To The Screen
		    cameraView.updateCameraTexture(gl, robot.getPanoramaImage(), windowSize);
	    }*/
	    gl.glLoadIdentity(); 
	    
	    
	    cameraView.drawCamera(gl, glu, windowSize);
	 //   cameraView.drawPerspective(gl, glu, windowSize, model.getDistanceSensorReadings());
	    
	    try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
