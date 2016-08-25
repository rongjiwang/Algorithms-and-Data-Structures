package code.renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import code.renderer.Scene.Polygon;

/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 * 
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {

	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		// TODO fill this in.
		Vector3D[] vertices = poly.getVertices();
		Vector3D a = vertices[0];
		Vector3D b = vertices[1];
		Vector3D c = vertices[2];

		Vector3D ans = (b.minus(a)).crossProduct(c.minus(a)).unitVector();
		if (ans.z > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 * 
	 * @param lightDirection
	 *            The Vector3D pointing to the directional light read in from
	 *            the file.
	 * @param lightColor
	 *            The color of that directional light.
	 * @param ambientLight
	 *            The ambient light in the scene, i.e. light that doesn't depend
	 *            on the direction.
	 */
	public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
		// TODO fill this in.
		Vector3D[] vertices = poly.getVertices();
		Vector3D a = vertices[0];
		Vector3D b = vertices[1];
		Vector3D c = vertices[2];

		Vector3D normal = (b.minus(a)).crossProduct(c.minus(a));
		// if (normal.z > 0) {
		// return null;
		// }
		float costh = normal.cosTheta(lightDirection);

		float incidentR = lightColor.getRed() * costh;
		if (incidentR < 0) {
			incidentR = 0;
		}
		if (incidentR > 255) {
			incidentR = 255;
		}

		float incidentG = lightColor.getGreen() * costh;
		if (incidentG < 0) {
			incidentG = 0;
		}
		if (incidentG > 255) {
			incidentG = 255;
		}

		float incidentB = lightColor.getBlue() * costh;
		if (incidentB < 0) {
			incidentB = 0;
		}
		if (incidentB > 255) {
			incidentB = 255;
		}

		int red = (int) ((ambientLight.getRed() + incidentR) * (poly.getReflectance().getRed() / 255f));

		int green = (int) ((ambientLight.getGreen() + incidentG) * (poly.getReflectance().getGreen() / 255f));
		int blue = (int) ((ambientLight.getBlue() + incidentB) * (poly.getReflectance().getBlue() / 255f));
		if (red > 255) {
			red = 255;
		}
		if (green > 255) {
			green = 255;
		}
		if (blue > 255) {
			blue = 255;
		}
		if (red < 0) {
			red = 0;
		}
		if (green < 0) {
			green = 0;
		}
		if (blue < 0) {
			blue = 0;
		}
		// System.out.println(red + " " + green + " " + blue + " " + normal);
		// System.out.println(costh + " " + ambientLight.getRed() + " " +
		// lightColor.getRed() * costh);
		// System.out.println(incidentR + " " + incidentG + " " + incidentB);
		return new Color(red, green, blue);
	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 * 
	 * @param scene
	 *            The original Scene.
	 * @param xRot
	 *            An angle describing the viewer's rotation in the YZ-plane (i.e
	 *            around the X-axis).
	 * @param yRot
	 *            An angle describing the viewer's rotation in the XZ-plane (i.e
	 *            around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 *         rotated accordingly.
	 */
	public static Scene rotateScene(Scene s, float xRot, float yRot) {

		Transform tx = Transform.newXRotation(xRot);
		Transform ty = Transform.newYRotation(yRot);
		Transform tz = Transform.newZRotation(0);
		List<Polygon> list = new ArrayList<>();

		for (Polygon p : s.getPolygons()) {
			Vector3D[] elements = p.getVertices();
			Vector3D e0 = elements[0];
			Vector3D e1 = elements[1];
			Vector3D e2 = elements[2];
			Color e3 = p.getReflectance();

			Vector3D newE0 = tx.multiply(e0);
			Vector3D newE1 = tx.multiply(e1);
			Vector3D newE2 = tx.multiply(e2);
			newE0 = ty.multiply(newE0);
			newE1 = ty.multiply(newE1);
			newE2 = ty.multiply(newE2);
			newE0 = tz.multiply(newE0);
			newE1 = tz.multiply(newE1);
			newE2 = tz.multiply(newE2);
			System.out.println(newE0.toString() + " " + newE1.toString() + newE2.toString());
			list.add(new Polygon(newE0, newE1, newE2, e3));

		}
		// return new Scene(list, scene.getLight());
		return new Scene(list, s.getLight());

	}

	/**
	 * This should translate the scene by the appropriate amount.
	 * 
	 * @param scene
	 * @return
	 */
	public static List<Polygon> translateScene(List<Polygon> poly) {
		// Transform t = new Transform();
		Renderer r = new Renderer();
		List<Polygon> list = poly;
		// list.addAll(scene.getPolygons());
		float diffx = GUI.CANVAS_WIDTH / 2 - r.getBoxWidth() / 2 - r.getMinX();
		float diffy = GUI.CANVAS_HEIGHT / 2 - r.getBoxHeight() / 2 - r.getMinY();
		Transform t = Transform.newTranslation(diffx, diffy, 1);
		for (Polygon p : list) {
			Vector3D[] elements = p.getVertices();
			Vector3D e0 = elements[0];
			Vector3D e1 = elements[1];
			Vector3D e2 = elements[2];
			Color e3 = p.getReflectance();

			Vector3D a = t.multiply(e0);
			Vector3D b = t.multiply(e1);
			Vector3D c = t.multiply(e2);

			list.add(new Polygon(a, b, c, e3));

		}
		System.out.println(list.size() + "--");
		// return new Scene(list, scene.getLight());
		return list;
	}

	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {
		poly.setEdgeListY(poly); // get minY and maxY
		int minY = (int) (poly.getMinY());
		int maxY = (int) (poly.getMaxY());

		float[][] edgeList = new float[GUI.CANVAS_WIDTH][4];

		for (int y = 0; y < edgeList.length; y++) {
			edgeList[y][0] = Float.POSITIVE_INFINITY;
			edgeList[y][1] = Float.POSITIVE_INFINITY; // Set default value
			edgeList[y][2] = Float.NEGATIVE_INFINITY;
			edgeList[y][3] = Float.POSITIVE_INFINITY;
		}
		for (int i = 0; i < 3; i++) {
			Vector3D a = poly.vertices[i];
			Vector3D b = poly.vertices[(i + 1) % 3]; // go through each pair of
														// data
			if (a.y > b.y) {
				Vector3D temp = b;
				b = a;
				a = temp;
			}
			float mX, mZ;

			if ((b.y - a.y) == 0) {
				mX = 0;
				mZ = 0; // 0 can't be divide
			} else {
				mX = (b.x - a.x) / (b.y - a.y);
				mZ = (b.z - a.z) / (b.y - a.y);
			}

			float x = (a.x);
			float z = (a.z);
			float x1 = (b.x);
			float z1 = (b.z);
			int I = (int) (a.y);
			int maxI = (int) (b.y);

			while (I < maxI) {// <=

				if (x < edgeList[I][0]) { // update the edgelist
					edgeList[I][0] = x;
					edgeList[I][1] = z;
				}
				if (x > edgeList[I][2]) {
					edgeList[I][2] = x;
					edgeList[I][3] = z;
				}

				I++;
				x += mX;
				z += mZ;

			}
		}
		// update new data to EdgeList class
		for (int y = minY; y < edgeList.length; y++) {
			Renderer.el.addRow(y, edgeList[y][0], edgeList[y][1], edgeList[y][2], edgeList[y][3]);
		}
		return Renderer.el;
	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 * 
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 * 
	 * @param zbuffer
	 *            A double array of colours representing the Color at each pixel
	 *            so far.
	 * @param zdepth
	 *            A double array of floats storing the z-value of each pixel
	 *            that has been coloured in so far.
	 * @param eList
	 *            The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor
	 *            The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList eList, Color polyColor) {

		int diff = eList.getEndY() - eList.getStartY();
		// check that my 2D array value has been changed or not
		// if fit the condition then it must been overlapping by another color
		for (int y = 0; y < diff; y++) {
			int x = (int) (eList.getLeftX(y));
			float z = eList.getLeftZ(y);
			float mz = (eList.getRightZ(y) - eList.getLeftZ(y)) / (eList.getRightX(y) - eList.getLeftX(y));
			while (x < (int) (eList.getRightX(y))) {
				if (x < GUI.CANVAS_WIDTH && x >= 0 && y >= 0 && y < GUI.CANVAS_HEIGHT && z < zdepth[x][y]) {
					zdepth[x][y] = z;
					zbuffer[x][y] = polyColor;
					Renderer.zdepth[x][y] = z;
					Renderer.zbuff[x][y] = polyColor;
				}
				x++;
				z += mz;
			}
		}
	}

}

// code for comp261 assignments
