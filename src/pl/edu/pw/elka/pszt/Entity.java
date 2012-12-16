package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;

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
	
	private int numberOfSegments;
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

	public Entity(final Entity entity)
	{
		this.bornGeneration = entity.bornGeneration;
		this.overlapArea = entity.overlapArea;
		this.coveredArea = entity.coveredArea;
		this.targetArea = entity.targetArea;
		this.connectors = new ArrayList<Connector>(entity.connectors);
		this.numberOfSegments = connectors.size();
	}
	/**
	 * Add connector between segment, vertex from segment and vertex from target segment to the entity
	 * 
	 * @param connector Specified connector
	 */
	public void addConnector(final Connector connector)
	{
		this.connectors.add(connector);
		this.numberOfSegments++;
		// Some optimization, that entity has changed
		this.coveredArea = -1;
	}
	
	/**
	 * 
	 * @param other "Father" in a relationship (this is mother!)
	 * @return baby
	 * @brief separator is a decision what is taken from mother
	 */
	public Entity copulateWith(final Entity other)
	{
		Entity baby = new Entity(this.bornGeneration);
		Random generator = new Random();		
		int separator = generator.nextInt(numberOfSegments-1) +1;
		int i = 0;
		for (;i<separator; ++i)
		{
			baby.addConnector(new Connector(this.connectors.get(i)));
		}
		for(;i<numberOfSegments;++i)
		{
			baby.addConnector(new Connector(other.connectors.get(i)));
		}	
		return baby;
	}

	public void mutateEntity()
	{
		// TODO mutation algorithm 
		// Access to necessary segments inside connectors
		/**
		 * Decision: Just one connection is mutated
		 * It can be simple changed to be allowed for more than one
		 * but this means loops, draw without returning and other weird stuff
		 */
		Random generator = new Random();

		int connectorToChange = generator.nextInt(numberOfSegments);
		Connector mutant = connectors.get(connectorToChange);
		int actualVertex = mutant.getSegmentVertex().getId();
		ArrayList<Vertex>  baseForSearching = mutant.getSegment().getVertices();
		
		int newVertex = actualVertex;
		while (newVertex == actualVertex)
		{
			try
			{
				newVertex = baseForSearching.get(generator.nextInt(baseForSearching.size())).getId();			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		mutant.getSegment().moveToVertexByVertex(mutant.getTargetSegmentVertex(),mutant.getSegment().getVertexById(newVertex));
		
		
		
		/**
		 * Random vertex from the segment, will be changed
		 */
		
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

<<<<<<< HEAD
	public boolean compare(Entity entity) {
		for (int i = 0; i<connectors.size(); ++i)
		{
			if(!this.connectors.get(i).compare(entity.connectors.get(i)))
				return false;
		}
		return true;
	}

=======
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
}
