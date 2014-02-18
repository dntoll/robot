package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.SharpMeasurement;

public class MapData {
	static int size = 200; 
	static float cellSize = 3.0f;
	
	int[] openSpace = new int[size * size]; 
	
	public MapData() {
		for (int i = 0;i< size*size; i++) {
			openSpace[i] = 0;
		}
	}
	
	public MapData(MapData freeArea) {
		for (int i = 0;i< size*size; i++) {
			openSpace[i] = freeArea.openSpace[i];
		}
	}

	public static int getSize() {
		return size;
	}


	public static float getCellSize() {
		return cellSize;
	}


	public boolean isFree(int x, int y) {
		return openSpace[getIndex(x, y)] == 1;
	}
	public boolean isBlocked(int x, int y) {
		return openSpace[getIndex(x, y)] == 2;
	}

	private int getIndex(int x, int y) {
		return y * size + x;
	}

	public void draw(SharpMeasurement sharp1Distance, Float robotPosition,
			Direction headDirection) {
		float length = sharp1Distance.getMin();
		
		
		double dist = length;

		for (int i = 0; i< dist; i++) {
			float x = robotPosition.x + headDirection.getX() * (float)i;
			float y = robotPosition.y + headDirection.getY() * (float)i;
			draw(x, y, 1);
		}
		
		if (sharp1Distance.okDistance()) {
			float blockLen = sharp1Distance.getMedian();
			float x = robotPosition.x + headDirection.getX() * blockLen;
			float y = robotPosition.y + headDirection.getY() * blockLen;
			draw(x, y, 2);
		}
		
	}
	
	private void draw(float rx, float ry, int type) {
		int x = (int) (rx / cellSize + size/2);
		int y = (int) (ry / cellSize + size/2);
		
		if (x >= 0 && x < size && y >= 0 && y < size) {
			if (openSpace[getIndex(x, y)] < 2) {
				openSpace[getIndex(x, y)] = type;
			}
		}
	}

}
