package pl.edu.pw.elka.pszt;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Vertex representation
 */
public class Vertex {

	/** ID iterator */
	private static int i = 0;

	/** Coordinates */
	private Coordinate coordinate;
	
	/** ID */
	private int id = i++;

	/**
	 * C-tor,
	 * given coordinates as Integer, but stored as double // TODO think about that, int will be better(easier)
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Vertex(final int x, final int y)
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
		this.coordinate = coordinate;
	}

	public void setX(final int x)
	{
		this.coordinate.x = x;
	}

	public int getX()
	{
		return (int) this.coordinate.x;
	}

	public void setY(final int x)
	{
		this.coordinate.x = x;
	}

	public int getY()
	{
		return (int) this.coordinate.x;
	}

	public int getId()
	{
		return this.id;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Coordinate getCoordinate()
	{
		return this.coordinate;
	}

	public String toString()
	{
		return this.id + " : (" + (int) this.coordinate.x + ", " + (int) this.coordinate.y + ")";
	}

}
