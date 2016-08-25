package code.renderer;

public class EdgeData {
	
	private float xLeft;
	private float xRight;
	private float zLeft;
	private float zRight;

	public EdgeData(float xLeft, float zLeft, float xRight, float zRight){
		this.xLeft = xLeft;
		this.xRight = xRight;
		this.zLeft = zLeft;
		this.zRight = zRight;
	}

	public float getxLeft() {
		return xLeft;
	}

	public void setxLeft(float xLeft) {
		this.xLeft = xLeft;
	}

	public float getxRight() {
		return xRight;
	}

	public void setxRight(float xRight) {
		this.xRight = xRight;
	}

	public float getzLeft() {
		return zLeft;
	}

	public void setzLeft(float zLeft) {
		this.zLeft = zLeft;
	}

	public float getzRight() {
		return zRight;
	}

	public void setzRight(float zRight) {
		this.zRight = zRight;
	}
}
