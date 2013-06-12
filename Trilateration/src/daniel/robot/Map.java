package daniel.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */
public class Map {
	/*private class Tile {

		public void set(int color) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	Tile m_tiles[][];*/
	int m_mapSize;
	
	BufferedImage m_image;
	List<Point2D.Float> m_obstacles = new ArrayList<Point2D.Float>();
	
	public Map(float mapSizeInMeters, int a_pixelsPerMeter) {
		m_mapSize = (int) (mapSizeInMeters * a_pixelsPerMeter);
		
		m_image = new BufferedImage(m_mapSize, m_mapSize, BufferedImage.TYPE_USHORT_GRAY);
		/*m_tiles = new Tile[m_mapSize][];
		for (int x = 0 ; x < m_mapSize; x++) {
			m_tiles[x] = new Tile[m_mapSize];
			for (int y = 0 ; y < m_mapSize; y++) {
				m_tiles[x][y] = new Tile();
			}
		}*/
	}

	public void Add(State bestGuess, SensorReading reading) {
		
		float scale = 1;
		int x0 = (int) (bestGuess.m_position.x + m_mapSize/2); 
		int y0 = (int) (bestGuess.m_position.y + m_mapSize/2);  
		
		Graphics2D g2d = m_image.createGraphics();
		
		g2d.setColor(Color.WHITE);
		g2d.translate(x0, y0);
		
		int degrees = 5;
		for (IRReading ir : reading.m_ir) {
			
			float distance = ir.m_distance * scale;
			
			float direction = getDirection(bestGuess, ir.m_servo);
			
			
			g2d.fillArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, (int)direction-degrees/2, degrees);
			
			float x = bestGuess.m_position.x + (float) (Math.cos(direction) * distance);
			float y = bestGuess.m_position.y + (float) (Math.sin(direction) * distance);
			m_obstacles.add(new Point2D.Float(x ,y ));
		}
		
		g2d.translate(-x0, -y0);		
	}

	private float getDirection(State state, float servoDirection) {
		float direction = state.m_heading - servoDirection +90;
		
		direction += 180.0f;
		
		while (direction > 360)
			direction -= 360;
		while (direction < 0)
			direction += 360;
		
		direction = 360 - direction;
		return direction;
	}

	public float getDistance(State state, float a_servoDirection, float beamWidth) throws Exception {
		WritableRaster raster = m_image.getRaster();
		float direction = getDirection(state, a_servoDirection);
		
		Point2D.Float start = state.m_position;
		
		float minLenSquare = 1000000.0f;
		boolean found = false;
		
		for (Point2D.Float end : m_obstacles ) {
			float dx = (start.x - end.x);
			float dy = (start.y - end.y);
			
			float toObstacle = (float) Math.atan2(dy, dx);
			
			if (Direction.getDegreeDifference(toObstacle, direction) < beamWidth / 2) {
				float distanceSquare = dx * dx + dy * dy;
				if (distanceSquare  < minLenSquare) {
					minLenSquare = distanceSquare;
					found = true;
				}
			}
			//Point2D.Float end = state.m_position.x + Math.cos(direction) * ;
		}
		
		if (found == false) {
			throw new Exception("no distance here");
		}
		
		//raster.getPixels(x, y, w, h, iArray);
		return (float) Math.sqrt(minLenSquare);
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return m_image;
	}

	/*private void FillTriangleSimple(int x0, int y0, int x1, int y1, int x2, int y2, int color)
	{
		Tile[][] pixels = m_tiles;
	    int width = m_mapSize;
	    int height = m_mapSize;
	    // sort the points vertically
	    int temp;
	    if (y1 > y2)
	    {
	    	temp = x1;
	    	x1 = x2;
	    	x2 = temp;
	        
	        
	        temp = y1;
	    	y1 = y2;
	    	y2 = temp;
	        
	    }
	    if (y0 > y1)
	    {
	        temp = x1;
	    	x1 = x0;
	    	x0 = temp;
	        
	    	temp = y1;
	    	y1 = y0;
	    	y0 = temp;
	    }
	    if (y1 > y2)
	    {
	    	temp = x1;
	    	x1 = x2;
	    	x2 = temp;
	    	
	    	temp = y1;
	    	y1 = y2;
	    	y2 = temp;
	    }
	 
	    double dx_far = (double)(x2 - x0) / (y2 - y0 + 1);
	    double dx_upper = (double)(x1 - x0) / (y1 - y0 + 1);
	    double dx_low = (double)(x2 - x1) / (y2 - y1 + 1);
	    double xf = x0;
	    double xt = x0 + dx_upper; // if y0 == y1, special case
	    for (int y = y0; y <= (y2 > height-1 ? height-1 : y2); y++)
	    {
	        if (y >= 0)
	        {
	            for (int x = (xf > 0 ? (int)xf : 0); x <= (xt < width ? xt : width-1) ; x++)
	                pixels[x][y].set(color);
	            for (int x = (xf < width ? (int)xf : width-1); x >= (xt > 0 ? xt : 0); x--)
	            	pixels[x][y].set(color);
	        }
	        xf += dx_far;
	        if (y < y1)
	            xt += dx_upper;
	        else
	            xt += dx_low;
	    }
	}*/

	
}
