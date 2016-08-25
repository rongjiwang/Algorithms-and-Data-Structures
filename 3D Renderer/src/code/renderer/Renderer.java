package code.renderer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import code.renderer.Scene.Polygon;

public class Renderer extends GUI {
	public List<Polygon> polySet = new ArrayList<>();
	public Vector3D light;
	// public Scene scene;
	private float shiftx = 0.0f;
	private float shifty = 0.0f; // rotate value

	public static EdgeList el;
	public static Color[][] zbuff;
	public static float[][] zdepth;
	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private float minZ;
	private float maxZ;
	private float boxWidth;
	private float boxHeight;
	private float centerX;
	private float centerY;

	/*
	 * This method should parse the given file into a Scene object, which you
	 * store and use to render an image.
	 */
	@Override
	protected void onLoad(File file) {

		try {
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = buff.readLine();
			String[] lineArray = line.split(" ");
			float lightX = Float.parseFloat(lineArray[0]);
			float lightY = Float.parseFloat(lineArray[1]); // cast file value to
															// float
			float lightZ = Float.parseFloat(lineArray[2]);
			light = new Vector3D(lightX, lightY, lightZ); // light source

			while ((line = buff.readLine()) != null) {
				lineArray = line.split(" ");
				Vector3D polyElementX = new Vector3D(Float.parseFloat(lineArray[0]), Float.parseFloat(lineArray[1]),
						Float.parseFloat(lineArray[2]));
				Vector3D polyElementY = new Vector3D(Float.parseFloat(lineArray[3]), Float.parseFloat(lineArray[4]),
						Float.parseFloat(lineArray[5]));
				Vector3D polyElementZ = new Vector3D(Float.parseFloat(lineArray[6]), Float.parseFloat(lineArray[7]),
						Float.parseFloat(lineArray[8]));

				int ColorRed = Integer.parseInt(lineArray[9]);
				int ColorGreen = Integer.parseInt(lineArray[10]);
				int ColorBlue = Integer.parseInt(lineArray[11]);

				Color color = new Color(ColorRed, ColorGreen, ColorBlue);
				// store the float data for polygon and it's color
				Polygon poly = new Polygon(polyElementX, polyElementY, polyElementZ, color);
				polySet.add(poly);
			}
			buff.close();
		} catch (IOException e) {
			System.out.println("File Reading failed");
		}

	}

	@Override
	protected void onKeyPress(KeyEvent ev) { // keyboard control rotation
		if (ev.getKeyCode() == KeyEvent.VK_LEFT || Character.toUpperCase(ev.getKeyChar()) == 'A') {
			shiftx += 1;
			render();
		} else if (ev.getKeyCode() == KeyEvent.VK_RIGHT || Character.toUpperCase(ev.getKeyChar()) == 'D') {
			shiftx -= 1;
			render();

		} else if (ev.getKeyCode() == KeyEvent.VK_UP || Character.toUpperCase(ev.getKeyChar()) == 'W') {
			shiftx -= 1;
			render();
		} else if (ev.getKeyCode() == KeyEvent.VK_DOWN || Character.toUpperCase(ev.getKeyChar()) == 'S') {
			shiftx -= 1;
			render();
		}

		/*
		 * This method should be used to rotate the user's viewpoint.
		 */
	}

	@Override
	protected BufferedImage render() {
		calculateBoundingBox(); // update bounding data
		List<Polygon> list = new ArrayList<>();
		list = this.translateScene(polySet); // update poly data to be inside
												// the frame
		list = this.scale(list); // update poly data to fit the frame
		// list = this.rotateScene(list, (float) shiftx, 0);
		polySet = list;

		el = new EdgeList((int) 0, GUI.CANVAS_HEIGHT); // access to EdgeList

		int[] ambient = getAmbientLight(); // light of drag bars
		Color amColor = new Color(ambient[0], ambient[1], ambient[2]);

		zbuff = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];
		zdepth = new float[CANVAS_WIDTH][CANVAS_HEIGHT];
		for (int i = 0; i < CANVAS_WIDTH; i++) { // set default value for all
													// pixels
			for (int j = 0; j < CANVAS_HEIGHT; j++) {
				zdepth[i][j] = Float.POSITIVE_INFINITY;
				zbuff[i][j] = Color.BLACK;
			}
		}
		if (!polySet.isEmpty()) {

			for (Polygon p : polySet) {

				if (Pipeline.isHidden(p) == false) { // facing the user
														// direction

					Color shading = Pipeline.getShading(p, light, p.getReflectance(), amColor);

					EdgeList edge = Pipeline.computeEdgeList(p);
					// calculate the final pixel color for the frame
					Pipeline.computeZBuffer(zbuff, zdepth, edge, shading);
				}
			}
			return convertBitmapToImage(zbuff);
		}
		return null;
		/*
		 * This method should put together the pieces of your renderer, as
		 * described in the lecture. This will involve calling each of the
		 * static method stubs in the Pipeline class, which you also need to
		 * fill in.
		 */
	}

	/**
	 * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
	 * indexed by column then row and has imageHeight rows and imageWidth
	 * columns. Note that image.setRGB requires x (col) and y (row) are given in
	 * that order.
	 */
	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				image.setRGB(x, y, bitmap[x][y].getRGB());
			}
		}
		return image;
	}

	/**
	 * Set up some hard code for Bounding box
	 */
	private void calculateBoundingBox() {
		minX = Float.POSITIVE_INFINITY;
		maxX = Float.NEGATIVE_INFINITY;
		minY = Float.POSITIVE_INFINITY;
		maxY = Float.NEGATIVE_INFINITY;
		minZ = Float.POSITIVE_INFINITY;
		maxZ = Float.NEGATIVE_INFINITY;

		for (Polygon p : polySet) {
			for (Vector3D v : p.getVertices()) {
				minX = Math.min(minX, v.x);
				maxX = Math.max(maxX, v.x);
				minY = Math.min(minY, v.y);
				maxY = Math.max(maxY, v.y);
				minZ = Math.min(minZ, v.z);
				maxZ = Math.max(maxZ, v.z);
			}
		}

		boxWidth = maxX - minX;
		boxHeight = maxY - minY;
		centerX = minX + boxWidth / 2;
		centerY = minY + boxHeight / 2;

	}

	/**
	 * rotate zbuffer around to view the back or side of the image
	 * 
	 * @param poly
	 * @param xRot
	 *            moving left or right
	 * @param yRot
	 *            moving up or down
	 * @return updated List of Polygon
	 */
	public List<Polygon> rotateScene(List<Polygon> poly, float xRot, float yRot) {
		calculateBoundingBox();

		Transform tx = Transform.newXRotation(xRot);
		Transform ty = Transform.newYRotation(yRot);
		Transform tz = Transform.newZRotation(0);
		List<Polygon> list = new ArrayList<>();

		for (Polygon p : poly) {
			Vector3D[] elements = p.getVertices();
			Vector3D e0 = elements[0];
			Vector3D e1 = elements[1];
			Vector3D e2 = elements[2];
			Color e3 = p.getReflectance();

			Vector3D newE0 = tx.multiply(e0);
			Vector3D newE1 = tx.multiply(e1);
			Vector3D newE2 = tx.multiply(e2);
			newE0 = ty.multiply(newE0);
			newE1 = ty.multiply(newE1); // new value after rotate matrix
										// calculation
			newE2 = ty.multiply(newE2);
			newE0 = tz.multiply(newE0);
			newE1 = tz.multiply(newE1);
			newE2 = tz.multiply(newE2);

			list.add(new Polygon(newE0, newE1, newE2, e3));
		}
		return list;

	}

	/**
	 * Fix the distance between actual data and center point
	 * 
	 * @param poly
	 * @return updated List of Polygon
	 */
	public List<Polygon> translateScene(List<Polygon> poly) {
		List<Polygon> list = new ArrayList<>();

		float minX = Float.POSITIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		for (Polygon p : poly) {
			for (int i = 0; i < 3; i++) {
				if (p.vertices[i].x < minX) {
					minX = p.vertices[i].x; // update the starting point
				}
				if (p.vertices[i].y < minY) {
					minY = p.vertices[i].y;
				}
			}
		}
		// calculate poly data through translate matrix
		Transform translate = Transform.newTranslation(-minX, -minY, 0.0f);
		for (Polygon p : poly) {
			Vector3D a = translate.multiply(p.vertices[0]);
			Vector3D b = translate.multiply(p.vertices[1]);
			Vector3D c = translate.multiply(p.vertices[2]);
			list.add(new Polygon(a, b, c, p.getReflectance()));
		}
		return list;
	}

	/**
	 * Scale the size of image up or down to fit the frame
	 * 
	 * @param poly
	 * @return updated List of Polygon
	 */
	public List<Polygon> scale(List<Polygon> poly) {
		List<Polygon> list = new ArrayList<>();

		float maxDiff = Float.NEGATIVE_INFINITY;
		for (Polygon p : poly) {
			for (int i = 0; i < 3; i++) {
				if (p.vertices[i].x - CANVAS_WIDTH > maxDiff) {
					maxDiff = p.vertices[i].x - CANVAS_WIDTH; // get the max
																// diff between
																// width or
																// height
				}
				if (p.vertices[i].y - CANVAS_HEIGHT > maxDiff) {
					maxDiff = p.vertices[i].y - CANVAS_HEIGHT;
				}
			}
		}
		// data run through scaler matrix for updating
		float r = ((float) CANVAS_HEIGHT) / ((float) CANVAS_HEIGHT + maxDiff);
		Transform scaler = Transform.newScale(r, r, r);

		for (Polygon p : poly) {
			Vector3D a = scaler.multiply(p.vertices[0]);
			Vector3D b = scaler.multiply(p.vertices[1]);
			Vector3D c = scaler.multiply(p.vertices[2]);
			list.add(new Polygon(a, b, c, p.getReflectance()));
		}

		return list;
	}

	public float getMinX() {
		return minX;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getCenterX() {
		return centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public float getBoxWidth() {
		return boxWidth;
	}

	public float getBoxHeight() {
		return boxHeight;
	}

	public static void main(String[] args) {
		new Renderer();
	}
}

// code for comp261 assignments
