package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;


/**
 * Represents entity made of connectors and segments as well
 * 
 * @author Kajo
 */
public class Entity implements Comparable<Entity> {

	/** List of connectors */
	private ArrayList<Connector> connectors = new ArrayList<Connector>();

	/** In which generation he/she/it has been born */
	private int bornGeneration;

	/*
	 * Optimization get
	 */
	/** Area which entity covers target multi segment */
	private double coveredArea = -1;

	/*
	 * Just for debug information
	 */
	/** Area of overlapping segments in entity */
	private double overlapArea = -1;

	/** Target area */
	private double targetArea = -9999;

	public static int i = 0;
	public int id = i++;
	/*
	 * End of debug info declaration
	 */
	
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
	 * Create target entity from multi segment
	 */
	public Entity(MultiSegment multiSegment)
	{
		for (Segment segment : multiSegment.getSegments())
			this.connectors.add(new Connector(segment, multiSegment, segment.getVertices().get(0), segment.getVertices().get(0), true));
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
	
	/**
	 * 
	 * @param other "Father" in a relationship (this is mother!)
	 * @return baby entity
	 * @brief separator is a decision what is taken from mother
	 */
	public Entity copulateWith(final Entity other, int copulateGeneration)
	{
		Entity baby = new Entity(copulateGeneration + 1);
		Random generator = new Random();		
		int separator = generator.nextInt(this.connectors.size() - 1);
		
		int numberTakenFromThis;
		int numberTakenFromOther;
		
		double max = Math.max(this.getAdaptationSize(), other.getAdaptationSize());
		double min = Math.min(this.getAdaptationSize(), other.getAdaptationSize());
		if(this.getAdaptationSize() > other.getAdaptationSize())
		{
			 numberTakenFromThis = (int) ((double)this.connectors.size() * (min/max));
			 numberTakenFromOther = this.connectors.size() - numberTakenFromThis;
			
		}
		else
		{
			 numberTakenFromOther = (int) ((double)this.connectors.size() * (min/max));
			 numberTakenFromThis = this.connectors.size() - numberTakenFromOther;
		}
		
		
		int fromThis = 0;
		int fromOther = 0;
		for(int i = 0; i<this.connectors.size(); ++i)
		{
			boolean tmp = generator.nextBoolean();
			
			
			if(tmp = true)
			{
				if(fromThis<numberTakenFromThis)
				{
					baby.addConnector(this.connectors.get(i).clone());
					fromThis++;
				}
				else
				{
					baby.addConnector(other.connectors.get(i).clone());
					fromOther++;
				}
			}
			else
			{
				if(fromOther<numberTakenFromOther)
				{
					baby.addConnector(other.connectors.get(i).clone());
					fromOther++;
				}
				else
				{
					baby.addConnector(this.connectors.get(i).clone());
					fromThis++;
				}
			}
			
		}
		/*for (int i = 0; i < this.connectors.size(); ++i)
		{
			if (i < separator)
				baby.addConnector(this.connectors.get(i).clone());
			else
				baby.addConnector(other.connectors.get(i).clone());
		}
		/*Collections.sort(connectors);
		Collections.sort(other.getConnectors());
		for(int i = 0; i<this.connectors.size(); ++i)
		{
			//baby.addConnector(this.connectors.get(i)) 
		}*/
		return baby;
	}

	public ArrayList<Connector> getConnectors()
	{
		return this.connectors;
	}

	/**
	 * Mutate entity
	 */
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
		
		// Choose connector to mutate
		int connectorId = generator.nextInt(this.connectors.size());
		Connector connectorToMutate = this.connectors.get(connectorId);

		// Get its segment
		Segment segmentToMove = connectorToMutate.getSegment();

		// Choose vertex to move segment by
		Vertex segmentVertex = segmentToMove.getRandomVertex();
		// Set new segment vertex in connector
		connectorToMutate.setSegmentVertex(segmentVertex);

		// Choose vertex to move segment to
		Vertex targetSegmentVertex = connectorToMutate.getTargetSegment().getRandomVertex();
		// Set new target vertex in connector
		connectorToMutate.setTargetSegmentVertex(targetSegmentVertex);

		
		// Do proper mutation
		segmentToMove.moveToVertexByVertex(targetSegmentVertex, segmentVertex);
		
		
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
		MultiSegment target = this.connectors.get(0).getTargetSegment();
		MultiSegment mp = new MultiSegment();

		for (Connector connector : this.connectors)
			mp.addSegment(connector.getSegment().clone());


		// Check proper intersection
		double[] result = target.calculateIntersectionArea(mp);

		// Save information
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
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("[ ");

		str.append("Adaptation = " + (int) (this.getAdaptationSize() * 100) + "%,\t");

		for (Connector connector : this.connectors)
			str.append(connector + ";\t");

		str.append(" ID:" + this.id + " Born in " + this.bornGeneration + " g.");

		str.append(" ]");

		return str.toString();
	}

	/**
	 * 
	 * @param other
	 * @return
	 */
	@Override
	public int compareTo(Entity other)
	{
		return (this.getAdaptationSize() < other.getAdaptationSize() ? -1 :
				(this.getAdaptationSize() > other.getAdaptationSize() ? 1 : 0 ));
	}

	/**
	 * To paint it
	 * @return
	 */
	public ArrayList<Segment> getSegments()
	{
		ArrayList<Segment> segments = new ArrayList<>();

		for (Connector connector : this.connectors)
			segments.add(connector.getSegment());

		return segments;
	}

}
