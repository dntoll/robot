package daniel;

public abstract class RobotPort {
	protected SyncronizedBuffer readBuffer = new SyncronizedBuffer();
	protected SyncronizedBuffer writeBuffer = new SyncronizedBuffer();
	
	/* (non-Javadoc)
	 * @see daniel.RobotPort#read()
	 */
	public String read() {
		return readBuffer.read();
	}
	
	/* (non-Javadoc)
	 * @see daniel.RobotPort#write(java.lang.String)
	 */
	public void write(String data) {
		writeBuffer.write(data);
	}
	
	/* (non-Javadoc)
	 * @see daniel.RobotPort#readSyncronosly()
	 */
	public String readSyncronosly() throws Exception {
		while(true) {
			String data = read();
			
			if (data.equals("") == false) {
				return data;
			} else {
				Thread.sleep(100);
			
			}
		}
	}

}