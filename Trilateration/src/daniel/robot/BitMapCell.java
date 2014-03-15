package daniel.robot;

public class BitMapCell {
	
	public enum CellState {
		unknown,
		empty,
		blocked
	}
	float isBlocked = 0.5f;
	float stdev;
	
	private BitMapCell(float state, float stdev) {
		this.isBlocked = state;
		this.stdev = stdev;
	}
	
	//Load functions
	public static BitMapCell getBlocked() {
		return new BitMapCell(1.0f, 1.0f);
	}
	
	public static BitMapCell getEmpty() {
		return new BitMapCell(0.0f, 1.0f);
	}
	
	public static BitMapCell getUnknown() {
		return new BitMapCell(0.5f, Float.MAX_VALUE);
	}
	
	/*public boolean isUnknown() {
		return !isBlocked() && ! isEmpty();
	}*/
	
	public boolean isBetter(float stdev) {
		return stdev < this.stdev;
	}
	
	boolean isEmpty() {
		return isBlocked <= 0.3f;
	}
	boolean isBlocked() {
		return isBlocked >= 0.7f;
	}

	public void modify(CellState type, float stdev) {
		if (type == CellState.empty && isBlocked > 0.0f) {
			isBlocked -= 0.2f;
		} else if (type == CellState.blocked  && isBlocked < 1.0f) {
			isBlocked += 0.2f;
		}
		this.stdev = stdev;
	}

	public void copy(BitMapCell bitMapCell) {
		this.isBlocked = bitMapCell.isBlocked;
	}

	
}
