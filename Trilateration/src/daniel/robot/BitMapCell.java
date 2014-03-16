package daniel.robot;

public class BitMapCell {
	
	public enum CellState {
		unknown,
		empty,
		blocked
	}
	float blocks = 0;
	float empty = 0;
	float stdev;
	
	private BitMapCell(int blocks, int empty, float stdev) {
		this.blocks = blocks;
		this.empty = empty;
		this.stdev = stdev;
	}
	
	//Load functions
	public static BitMapCell getBlocked() {
		return new BitMapCell(1,0, 1.0f);
	}
	
	public static BitMapCell getEmpty() {
		return new BitMapCell(0,1, 1.0f);
	}
	
	public static BitMapCell getUnknown() {
		return new BitMapCell(0,0, Float.MAX_VALUE);
	}
	
	/*public boolean isUnknown() {
		return !isBlocked() && ! isEmpty();
	}*/
	
	public boolean isBetter(float stdev) {
		return stdev < this.stdev;
	}
	
	boolean isEmpty() {
		return empty > blocks && empty > 0;
	}
	boolean isBlocked() {
		return blocks > empty && blocks > 0;
	}

	public void modify(CellState type, float stdev) {
		if (type == CellState.empty) {
			empty += 1.0f/stdev;
		} else if (type == CellState.blocked) {
			blocks += 1.0f/stdev;
		}
		this.stdev = stdev;
	}

	public void copy(BitMapCell bitMapCell) {
		this.empty = bitMapCell.empty;
		this.blocks = bitMapCell.blocks;
		this.stdev = bitMapCell.stdev;
	}

	
}
