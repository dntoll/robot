package daniel.robot;

import java.awt.geom.Point2D.Float;



public class Bitmap {
	
	
	protected int size; 
	
	
	protected BitMapCell[] grid;// = (T[]) T.getArray(size * size);//new T[size * size]; 
	
	public Bitmap(int size2) {
		size = size2;
		grid = new BitMapCell[size * size];
		for (int i = 0;i< size*size; i++) {
			grid[i] = BitMapCell.getUnknown();
		}
	}
	
	public int getSize() {
		return size;
	}
	public boolean isFree(int x, int y) {
		return grid[getIndex(x, y)].isEmpty();
	}
	public boolean isBlocked(int x, int y) {
		return grid[getIndex(x, y)].isBlocked();
	}
	
	protected int getIndex(int x, int y) {
		if (x < 0 || y < 0 || x > size-1 || y > size-1) {
			return 0;
		}
		return y * size + x;
	}
	
	protected float getDistance(Float from, Direction worldDirection) throws Exception {
		
		float xdir = worldDirection.getX();
		float ydir = worldDirection.getY();
		
		
		
		float minu = java.lang.Float.MAX_VALUE;
		
		if (xdir > 0) {
			for (int x = (int)from.x+1; (float)x < size; x++) {
				float u = ((float)x - from.x) / xdir;
				float y = from.y + ydir * u;
				if ( y < 0 || y > size-1) {
					break;
				}
				if (grid[getIndex((int)x, (int)y)].isBlocked() || 
					grid[getIndex((int)x+1, (int)y)].isBlocked()) {
					if (u < minu)
						minu = u;
					break;
				}
				
			}
		} else if (xdir < 0) {
			for (int x = (int)from.x; (float)x > 0; x--) {
				float u = ((float)x - from.x) / xdir;
				float y = from.y + ydir * u;
				if ( y < 0 || y > size-1) {
					break;
				}
				if (grid[getIndex((int)x, (int)y)].isBlocked() || 
					grid[getIndex((int)x-1, (int)y)].isBlocked()) {
					if (u < minu)
						minu = u;
					break;
				}
			}
		}
		
		if (ydir > 0) {
			for (int y = (int)from.y+1; (float)y < size; y++) {
				float u = ((float)y - from.y) / ydir;
				float x = from.x + xdir * u;
				if ( x < 0 || x > size-1) {
					break;
				}
				if (grid[getIndex((int)x, (int)y)].isBlocked() || 
					grid[getIndex((int)x, (int)y+1)].isBlocked()) {
					if (u < minu)
						minu = u;
					break;
				}
				
			}
		} else if (ydir < 0) {
			for (int y = (int)from.y; (float)y > 0; y--) {
				float u = ((float)y - from.y) / ydir;
				float x = from.x + xdir * u;
				if ( x < 0 || x > size-1){
					break;
				}
				if (grid[getIndex((int)x, (int)y)].isBlocked() || 
					grid[getIndex((int)x, (int)y-1)].isBlocked()) {
					if (u < minu)
						minu = u;
					break;
				}
			}
		}
		
		if (minu >= java.lang.Float.MAX_VALUE)
			throw new Exception("no obstacle found");
		
		return minu;
		
	}
}
