package daniel.robot;

public class BitMapCell {
	
	public static final BitMapCell empty = new BitMapCell();
	public static final BitMapCell blocked = new BitMapCell();
	public static final BitMapCell unknown = new BitMapCell();
	//empty,
	//blocked,
	//unknown;
	boolean isUnknown() {
		return false;
	}
	boolean isEmpty() {
		return false;
	}
	boolean isBlocked() {
		return false;
	}
	
	static BitMapCell[] getArray(int size) {
		return new BitMapCell[size];
		
	}
	
	static BitMapCell getInstance() {
		return new BitMapCell();
	}
}
