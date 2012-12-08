package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Representation of one tangram segment polygon with id identifying it
 * 
 * @author Kajo
 */
public class Segment {

	/** ID iterator */
	private static int i = 0;

	/** Array of vertices */
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	/** Polygon created from coordinates */
	private Polygon polygon;

	/** Unique segment ID */
	private int id = i++;

	/**
	 * C-tor,
	 * creates vertex representation from coordinate int pairs 
	 * 
	 * @param vertices array of array with pairs x and y as coordinate
	 * @throws Exception if not enough coordinates
	 */
	public Segment(final int[][] coordinates) throws Exception
	{
		if (coordinates.length < 3)
			throw new Exception("Not enough coordinates");

		for (int[] coord : coordinates)
			this.vertices.add(new Vertex(coord[0], coord[1]));

		this.createPolygon();
	}

	/**
	 * C-tor
	 * 
	 * @param vertices array of coordinates
	 * @throws Exception if not enough coordinates
	 */
	public Segment(final Coordinate[] vertices) throws Exception
	{
		if (vertices.length < 3)
			throw new Exception("Not enough coordinates");
		
		for (Coordinate vertex : vertices)
			this.vertices.add(new Vertex(vertex));

		this.createPolygon();
	}

	/**
	 * C-tor
	 * 
	 * @param vertices ArrayList of vertices
	 * @throws Exception if not enough vertices
	 */
	public Segment(final ArrayList<Vertex> vertices) throws Exception
	{
		if (vertices.size() < 3)
			throw new Exception("Not enough coordinates");

		this.vertices = vertices;

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

	/**
	 * Creates union of this segment and other segment
	 * 
	 * @param other Segment to union with
	 * @return new segment instance made of given segments
	 * @throws Exception
	 */
	public Segment makeUnionSegment(final Segment other) throws Exception
	{
		Segment sum = new Segment(this.vertices);
		
		sum.unionSegment(other);

		return sum;
	}

	/**
	 * Union segments, make proper list of vertices
	 * and create polygon from new vertices
	 * 
	 * @param other Segment to union with
	 */
	private void unionSegment(final Segment other)
	{
		Polygon sum;

		// If null make union with itself
		if (other == null)
			sum = this.polygon;
		else
			sum = (Polygon) this.polygon.union(other.getPolygon());

		Coordinate[] coords = sum.getCoordinates();

		this.vertices.clear();
		// Vertices without closing one
		for (int i = 0; i < coords.length - 1; ++i)
			this.vertices.add(new Vertex(coords[i]));

		this.createPolygon();
	}

	/**
	 * Get vertex instance by given id from vertices list
	 * 
	 * @param id Vertex id
	 * @return Vertex instance if found equal id
	 * @throws Exception if no id found
	 */
	public Vertex getVertexById(final int id) throws Exception
	{
		for (Vertex v : this.vertices)
			if (id == v.getId())
				return v;

		throw new Exception("No vertex found in segmnet");
	}

	/**
	 * Get vertices list from segment
	 * 
	 * @return Vertices list (Without duplicated closing one vertex)
	 */
	public ArrayList<Vertex> getVertices()
	{
		return this.vertices;
	}

	/**
	 * Get segment polygon
	 * 
	 * @return Segment polygon
	 */
	public Polygon getPolygon()
	{
		return this.polygon;
	}

	/**
	 * Get id of segment
	 * 
	 * @return ID of segment
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get segment information
	 * 
	 *  @return segment ID and polygon contained information about coordinates
	 */
	public String toString()
	{
		return this.id + " : " + this.polygon.toString();
	}

}
