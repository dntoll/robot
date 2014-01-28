package daniel.robot;


//http://www.java-tips.org/other-api-tips/jogl/arcball-rotation-nehe-tutorial-jogl-port.html
public class Vector3f {
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public static void cross(Vector3f Result, Vector3f v1, Vector3f v2) {
        Result.x = (v1.y * v2.z) - (v1.z * v2.y);
        Result.y = (v1.z * v2.x) - (v1.x * v2.z);
        Result.z = (v1.x * v2.y) - (v1.y * v2.x);
    }

    public static float dot(Vector3f v1, Vector3f v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z + v2.z);
    }

    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

	public Vector3f add(float i, float j, float k) {
		
		return new Vector3f(x + i, y + j, z + k);
	}
}