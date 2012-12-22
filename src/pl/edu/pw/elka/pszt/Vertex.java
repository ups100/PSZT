package pl.edu.pw.elka.pszt;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Vertex representation with id identifying it
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Vertex {

	/** ID iterator */
	private static int i = 1;

	/** Coordinates */
	private Coordinate coordinate;
	
	/** Unique vertex ID */
	private int id = i++;

	/**
	 * C-tor,
	 * given coordinates as doubles
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Vertex(final double x, final double y)
	{
		this.coordinate = new Coordinate(x, y);
	}

	/**
	 * C-tor
	 * 
	 * @param coordinate Specified coordinate
	 */
	public Vertex(final Coordinate coordinate)
	{
		this.coordinate = new Coordinate(coordinate);
	}

	/**
	 * C-tor
	 * 
	 * @param coordinate Specified coordinate
	 * @param id Vertex id
	 */
	public Vertex(final Coordinate coordinate, final int id)
	{
		this.coordinate = new Coordinate(coordinate);
		this.id = id;
	}

	/**
	 * Get clone of the vertex
	 */
	@Override
	public Vertex clone()
	{
		return new Vertex(this.coordinate, this.id);
	}

	/**
	 * Set x coordinate
	 * 
	 * @param x X coordinate
	 */
	public void setX(final double x)
	{
		this.coordinate.x = x;
	}

	/**
	 * Get x coordinate
	 * 
	 * @return X coordinate
	 */
	public double getX()
	{
		return this.coordinate.x;
	}

	/**
	 * Set y coordinate
	 * 
	 * @param y Y coordinate
	 */
	public void setY(final double y)
	{
		this.coordinate.y = y;
	}

	/**
	 * Get y coordinate
	 * 
	 * @return Y coordinate
	 */
	public double getY()
	{
		return this.coordinate.y;
	}

	/**
	 * Get vertex id
	 * 
	 * @return Vertex ID
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Set coordinate by given coordinate
	 * 
	 * @param coordinate Coordinate to set
	 */
	public void setCoordinate(final Coordinate coordinate) 
	{
		this.coordinate = coordinate;
	}

	/**
	 * Get coordinate representation of vertex
	 * 
	 * @return Vertex coordinate
	 */
	public Coordinate getCoordinate()
	{
		return this.coordinate;
	}
	
	/**
	 * Get information about vertex
	 * 
	 * @return ID of vertex and coordinates
	 */
	@Override
	public String toString()
	{
		return this.id + " : (" + this.coordinate.x + ", " + this.coordinate.y + ")";
	}

}