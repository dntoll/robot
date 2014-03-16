package daniel.robot.slam.map.bm;

import java.awt.geom.Point2D.Float;

import daniel.robot.BitMapCell;
import daniel.robot.Bitmap;
import daniel.robot.Direction;
import daniel.robot.sensors.SharpMeasurement;

public class MapData extends Bitmap {
	
	
	protected float cellSize;
	
	public MapData(int size, float cellSize) {
		super(size);
		this.cellSize = cellSize;
	}
	
	public MapData(MapData freeArea) {
		super(freeArea.size);
		
		cellSize = freeArea.cellSize;
		for (int i = 0;i< size*size; i++) {
			grid[i].copy(freeArea.grid[i]);
		}
	}

	public float getCellSize() {
		return cellSize;
	}

	public void draw(SharpMeasurement sharp1Distance, Float robotPosition,
			Direction headDirection) {

		double dist = sharp1Distance.getMin();
		float stdev = sharp1Distance.getStdev();

		for (int i = 0; i< dist; i++) {
			float x = robotPosition.x + headDirection.getX() * (float)i;
			float y = robotPosition.y + headDirection.getY() * (float)i;
			draw(x, y, BitMapCell.CellState.empty, stdev);
		}
		
		if (sharp1Distance.okDistance()) {
			float blockLen = sharp1Distance.getMedian();
			float x = robotPosition.x + headDirection.getX() * blockLen;
			float y = robotPosition.y + headDirection.getY() * blockLen;
			draw(x, y, BitMapCell.CellState.blocked, stdev);
		}
		
	}
	
	private void draw(float rx, float ry, BitMapCell.CellState type, float stdev) {
		int x = (int) (rx / cellSize + size/2);
		int y = (int) (ry / cellSize + size/2);
		
		if (x >= 0 && x < size && y >= 0 && y < size) {
			if (grid[getIndex(x, y)].isBetter(stdev)) 
			{
				grid[getIndex(x, y)].modify(type, stdev);
			}
		}
	}
	
	public float  getDistance(Float from, Direction worldDirection) throws Exception {
		
		Float scaled = new Float(from.x/cellSize + size/2, from.y/cellSize + size/2);
		return super.getDistance(scaled, worldDirection);
	}

	
	
	

}
