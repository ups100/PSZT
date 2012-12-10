package pl.edu.pw.elka.pszt;

import java.util.ArrayList;

/**
 * Represents entity made of connectors and segments as well
 * 
 * @author Kajo
 */
public class Entity {

	/** List of connectors */
	private ArrayList<Connector> connectors = new ArrayList<Connector>();

	/** In which generation he/she/it has been born */
	private int bornGeneration;

	/*
	 * Just for debug information
	 */
	/** Area of overlapping segments */
	private double overlapArea = -1;

	/** Area which covers target */
	private double coveredArea = -1;

	/** Target area */
	private double targetArea = -9999;

	/**
	 * C-tor
	 * set generation number
	 * 
	 * @param generation Actual generation number
	 */
	public Entity(final int generation)
	{
		this.bornGeneration = generation;
	}

	/**
	 * Add connector between segment, vertex from segment and vertex from target segment to the entity
	 * 
	 * @param connector Specified connector
	 */
	public void addConnector(final Connector connector)
	{
		this.connectors.add(connector);

		// Some optimization, that entity has changed
		this.coveredArea = -1;
	}

	public Entity copulateWith(final Entity other)
	{
		// TODO make them love each other
		return null;
	}

	public void mutateEntity()
	{
		// TODO mutation algorithm 
		// Access to necessary segments inside conectors

		// Some optimization, that entity has changed
		this.coveredArea = -1;
	}

	/**
	 * Adaptation function, check how much entity covers target entity
	 * 
	 * @return Number in range -1 : 1, represents percentage of covering
	 * 					negative value if overlap a lot without cover anything of target
	 */
	public double getAdaptationSize()
	{
		// TODO question if we accept overlapping entities ? -> answer: much easier ;) it's random algorithm anyway..

		// If already computed
		if (this.coveredArea < 0)
			calculateAdaptation();

		return this.coveredArea / this.targetArea;
	}

	/**
	 * Calculates intersection between entity and target segment
	 * and set fields with gather information such as
	 * 	- overlapping area
	 *  - cover area
	 *  - target area
	 */
	private void calculateAdaptation()
	{
		// Get mins to move entity connected segments to (0, 0) as minimum coordinate
		int minX = this.connectors.get(0).getSegment().getMinX();
		int minY = this.connectors.get(0).getSegment().getMinY();

		for (int i = 0; i < this.connectors.size(); ++i)
		{
			int minXt = this.connectors.get(i).getSegment().getMinX();
			if (minX > minXt)
				minX = minXt;

			int minYt = this.connectors.get(i).getSegment().getMinY();
			if (minY > minYt)
				minY = minYt;
		}

		MultiSegment target = this.connectors.get(0).getTargetSegment();
		MultiSegment mp = new MultiSegment();

		for (Connector connector : this.connectors)
			mp.addSegment(connector.getSegment().clone());


		double[] result = target.calculateIntersectionArea(mp);

		this.coveredArea = result[0] - result[1];
		this.overlapArea = result[1];
		this.targetArea = result[2];
	}

	/**
	 * Set overlapping area size
	 * 
	 * @param overlapArea overlap area size
	 */
	public void setOverlapArea(final double overlapArea)
	{
		this.overlapArea = overlapArea;
	}

	/**
	 * Get overlapping area
	 * 
	 * @return Overlapping area size
	 */
	public double getOverlapArea()
	{
		return this.overlapArea;
	}

	/**
	 * Set size of source area covers target area
	 * 
	 * @param coveredArea Cover area
	 */
	public void setCoveredArea(final double coveredArea)
	{
		this.coveredArea = coveredArea;
	}

	/**
	 * Get size as source covers target area
	 * @return
	 */
	public double getCoveredArea()
	{
		return this.coveredArea;
	}

	/**
	 * Set target segment area
	 * 
	 * @param targetArea Target segment area
	 */
	public void setTargetArea(final double targetArea)
	{
		this.targetArea = targetArea;
	}

	/**
	 * Get target segment area
	 * 
	 * @return Target segment area
	 */
	public double getTargetArea()
	{
		return this.targetArea;
	}

	/**
	 * Get entity born generation
	 * 
	 * @return Number of generation in which has been born
	 */
	public int getBornGeneration()
	{
		return this.bornGeneration;
	}

	/**
	 * Get information about entity
	 * 
	 * @return Entity information
	 */
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("[ ");

		str.append("Adaptation = " + (int) (this.getAdaptationSize() * 100) + "%,\t");

		for (Connector connector : this.connectors)
			str.append(connector + ";\t");

		str.append(" Born in " + this.bornGeneration + " g.");

		str.append(" ]");

		return str.toString();
	}

}
