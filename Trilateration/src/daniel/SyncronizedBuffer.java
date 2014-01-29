package daniel;

class SyncronizedBuffer {
	private String buffer = "";
	
	public synchronized void write(String message) {
		buffer += message;
	}
	
	public synchronized String read() {
		if (buffer.contains("\n")) {
			int at = buffer.indexOf('\n');
			
			String ret = buffer.substring(0, at+1);
			
			buffer = buffer.substring(at+1);
			return ret;
		}
		
		return "";
	}

	public synchronized void write(byte d) {
		
		buffer += (char)d;
	}
	
	
	
}