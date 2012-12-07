package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Representation of one tangram segment polygon
 */
public class Segment {

	/** ID iterator */
	private static int i = 0;

	/** Array of vertices */
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	/** Polygon created from coordinates */
	private Polygon polygon;

	/** ID */
	private int id = i++;

	/**
	 * C-tor,
	 * creates vertex representation from coordinate int pairs 
	 * 
	 * @param vertices array of array with pairs x and y as coordinate
	 */
	public Segment(final int[][] coordinates)
	{
		for (int[] coord : coordinates)
			this.vertices.add(new Vertex(coord[0], coord[1]));

		this.createPolygon();
	}

	/**
	 * C-tor
	 * 
	 * @param vertices array of coordinates
	 */
	public Segment(final Coordinate[] vertices)
	{
		for (Coordinate vertex : vertices)
			this.vertices.add(new Vertex(vertex));

		this.createPolygon();
	}

	/**
	 * Create polygon from set vertices
	 */
	private void createPolygon()
	{
		GeometryFactory geometryFactory = new GeometryFactory();
		LinearRing holes[] = null;

		Coordinate[] coordinates = new Coordinate[this.vertices.size() + 1];

		int i = 0;
		for (; i < this.vertices.size(); ++i)
			coordinates[i] = this.vertices.get(i).getCoordinate();

		// Close shape
		coordinates[i] = this.vertices.get(0).getCoordinate();

		LinearRing polygonRing = geometryFactory.createLinearRing(coordinates);

		this.polygon = geometryFactory.createPolygon(polygonRing, holes);
	}

	public Polygon getPolygon()
	{
		return this.polygon;
	}

	public String toString()
	{
		return this.id + " : " + this.polygon.toString();
	}

}
