package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2GL3.GL_QUADS;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceSensorReadings;

public class CameraView {
	BufferedImage oldImage = null;
	private Texture cameraTexture;
	TextureCoords textureCoords;
	private float textureTop, textureBottom, textureLeft, textureRight;
	private int imageWidth;
	private int imageHeight;
	
	
	void drawCamera(GL2 gl, GLU glu, Dimension windowSize) {
		
	     if (cameraTexture != null) {
		     cameraTexture.enable(gl);
		     cameraTexture.bind(gl);
			  gl.glEnable(GL_BLEND);       // Turn blending on
			  gl.glDisable(GL_DEPTH_TEST); // Turn depth testing off
		      gl.glBegin(GL_QUADS); // of the color cube
		      gl.glColor3f(1, 1, 1);
		      drawQuad(gl, 0, windowSize.height-imageHeight, imageWidth, imageHeight);
		      gl.glEnd();
		      cameraTexture.disable(gl);
	      
	     }
	      
	}



	public void updateCameraTexture(GL2 gl, BufferedImage newImage, Dimension windowSize) {
		if (newImage == null)
			return;
		if (oldImage != null)
			oldImage.flush();
		oldImage = newImage;	
		cameraTexture = AWTTextureIO.newTexture(GLProfile.getDefault(), newImage, false);
		
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			
			 
	    textureCoords = cameraTexture.getImageTexCoords();
	    textureTop = textureCoords.top();
	    textureBottom = textureCoords.bottom();
	    textureLeft = textureCoords.left();
	    textureRight = textureCoords.right();
	    imageWidth = windowSize.width;
	    imageHeight = (int) ((float)imageWidth * (float)newImage.getHeight() / (float)newImage.getWidth());
		      
	}



	public void drawPerspective(GL2 gl, GLU glu, Dimension windowSize, DistanceSensorReadings reading) {
		/*cameraTexture.enable(gl);
		     cameraTexture.bind(gl);
	      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
		  gl.glLoadIdentity();             // reset projection matrix
		  glu.gluPerspective(90, windowSize.width/windowSize.height, 0.1, 20.0f);
		  
		  gl.glMatrixMode(GL_MODELVIEW);
		  glu.gluLookAt(0, 5, -2.0f, 0, 0, 0, 0, 1, 0); 
	      
		  gl.glBegin(GL_QUADS); // of the color cube
	      gl.glColor3f(1, 1, 1);
	      gl.glDisable(GL_BLEND);
	      
	      boolean first = true;
	      
	      
	      for(DirectionalReading reading1 : reading.getReadings().values()) {
				Direction direction = reading1.getDirection();
				float textureCoordinate = direction.getHeadingDegrees()/180;
				Measurement fwd = reading1.getSharp1Distance();
				
				
				float forwardDistance = fwd.getMean()/160.0f;
				if (!first) {
					gl.glTexCoord2f(textureCoordinate, textureBottom);
				    drawPoint(gl, 0, 0, 0, forwardDistance, direction);
					gl.glTexCoord2f(textureCoordinate, textureTop);
					drawPoint(gl, 0, 1, 0, forwardDistance, direction);	
				    
				    
				} 
				first = false;
				gl.glTexCoord2f(textureCoordinate, textureTop);
				drawPoint(gl, 0, 1,0, forwardDistance, direction);
				
				gl.glTexCoord2f(textureCoordinate, textureBottom);
			    drawPoint(gl, 0,0,0, forwardDistance, direction);
				
			    
			    
			    
				
		  }
	      
	      gl.glEnd();
		  
	      cameraTexture.disable(gl);*/
	}
	
	private void drawPoint(GL2 gl, float centerx, float y, float centerz, float radius, Direction direction) {
		float dx = direction.getX();
		float dz = direction.getY();
		float x = centerx + dx * radius;
		float z = centerz + dz * radius;
		
		gl.glVertex3f(x, y, z);
	}
	
	
	
	private void drawQuad(GL2 gl, float x, float y, float width, float height) {
		gl.glNormal3f(0.0f, 0.0f, 1.0f);
	    gl.glTexCoord2f(textureLeft, textureBottom);
	    gl.glVertex3f(x, y, 0.0f); // bottom-left of the texture and quad
	    gl.glTexCoord2f(textureRight, textureBottom);
	    gl.glVertex3f(x+width, y, 0.0f);  // bottom-right of the texture and quad
	    gl.glTexCoord2f(textureRight, textureTop);
	    gl.glVertex3f(x+width, y+height, 0.0f);   // top-right of the texture and quad
	    gl.glTexCoord2f(textureLeft, textureTop);
	    gl.glVertex3f(x, y+height, 0.0f);  // top-left of the texture and quad
	}
}
