package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents entity made of connectors and segments as well
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Entity implements Comparable<Entity> {

	/** List of connectors */
	private ArrayList<Connector> connectors = new ArrayList<Connector>();

	/** In which generation he/she/it has been born */
	private int bornGeneration;

	/** Area which entity covers target multi segment */
	private double coveredArea = -1;

	/** Area of overlapping segments in entity */
	private double overlapArea = -1;

	/** Target area */
	private double targetArea = -9999;

	public static int i = 0;
	public int id = i++;

	/**
	 * C-tor set generation number
	 * 
	 * @param generation
	 *            Actual generation number
	 */
	public Entity(final int generation) {
		this.bornGeneration = generation;
	}

	/**
	 * Create target entity from multi segment
	 */
	public Entity(MultiSegment multiSegment) {
		for (Segment segment : multiSegment.getSegments())
			this.connectors.add(new Connector(segment, multiSegment, segment
					.getVertices().get(0), segment.getVertices().get(0), true));
	}

	/**
	 * Add connector between segment, vertex from segment and vertex from target
	 * segment to the entity
	 * 
	 * @param connector
	 *            Specified connector
	 */
	public void addConnector(final Connector connector) {
		this.connectors.add(connector);

		this.coveredArea = -1;
	}

	/**
	 * Creates new baby from parents "this" and "other"
	 * 
	 * @param other
	 *            "Father" in a relationship ("this" is mother)
	 * @return new Entity that represents the baby
	 */
	public Entity copulateWith(final Entity other, int copulateGeneration) {
		Entity baby = new Entity(copulateGeneration + 1);
		Random generator = new Random();
		
		int numberDifferent = 0;
		for(int i = 0; i < connectors.size(); ++i) {
			int j = i;
			if(connectors.get(i).getSegment().getId() 
					!= other.connectors.get(i).getSegment().getId()) {
			}

			if((connectors.get(i).getSegmentVertex().getId() 
					!=other.connectors.get(j).getSegmentVertex().getId()) 
					|| 
					(connectors.get(i).getTargetSegmentVertex().getId() 
					!= other.connectors.get(j).getTargetSegmentVertex().getId())) {
				++numberDifferent;
			}
		}
		
		int numberTakenFromThis;
		int numberTakenFromOther;

		
		if (this.getAdaptationSize() > other.getAdaptationSize()) {
			numberTakenFromThis = numberDifferent - numberDifferent/2;
			numberTakenFromOther = numberDifferent/2;

		} else {
			numberTakenFromOther = numberDifferent - numberDifferent/2;
			numberTakenFromThis = numberDifferent/2;
		}
		
		if(numberTakenFromOther == 0) {
			numberTakenFromOther = generator.nextInt(1);
			numberTakenFromThis = numberDifferent - numberTakenFromOther;
		} else if(numberTakenFromThis == 0) {
			numberTakenFromThis = generator.nextInt(1);
			numberTakenFromOther = numberDifferent - numberTakenFromThis;
		}
		
		int fromThis = 0;
		int fromOther = 0;
		for (int i = 0; i < this.connectors.size(); ++i) {
			int j = i;
			if((connectors.get(i).getSegmentVertex().getId() 
					!=other.connectors.get(j).getSegmentVertex().getId()) 
					|| 
					(connectors.get(i).getTargetSegmentVertex().getId() 
					!= other.connectors.get(j).getTargetSegmentVertex().getId())) {
				
				boolean tmp = generator.nextBoolean();
				if (tmp) {
					if (fromThis < numberTakenFromThis) {
						baby.addConnector(this.connectors.get(i).clone());
						fromThis++;
					} else {
						baby.addConnector(other.connectors.get(i).clone());
						fromOther++;
					}
				} else {
					if (fromOther < numberTakenFromOther) {
						baby.addConnector(other.connectors.get(i).clone());
						fromOther++;
					} else {
						baby.addConnector(this.connectors.get(i).clone());
						fromThis++;
					}
				}
			} else {
				baby.addConnector(this.connectors.get(i).clone());
			}
			
		}
		return baby;
	}	
	
	/**
	 * Mutates entity, randomly changing the vertexes
	 */
	public void mutateEntity() {
		Random generator = new Random();

		int mutationStrenght = generator.nextInt(1) +1;
		
		while (mutationStrenght > 0) {
			int connectorId = generator.nextInt(this.connectors.size());
			Connector connectorToMutate = this.connectors.get(connectorId);

			Segment segmentToMove = connectorToMutate.getSegment();

			Vertex segmentVertex = segmentToMove.getRandomVertex();

			connectorToMutate.setSegmentVertex(segmentVertex);
			
			Vertex targetSegmentVertex;
			if(generator.nextBoolean()) {
				targetSegmentVertex = connectorToMutate.getTargetSegment()
						.getRandomVertex();
				connectorToMutate.setTargetSegmentVertex(targetSegmentVertex);
			} else {
				targetSegmentVertex = connectorToMutate.getTargetSegmentVertex();
			}
			
			segmentToMove.moveToVertexByVertex(targetSegmentVertex,
					segmentVertex);
			--mutationStrenght;
		}
		this.coveredArea = -1;
	}

	
	/**
	 * Adaptation function, check how much entity covers target entity
	 * 
	 * @return Number in range -1 : 1, represents percentage of covering
	 */
	public double getAdaptationSize() {
		if (this.coveredArea < 0)
			calculateAdaptation();

		return this.coveredArea / this.targetArea;
	}

	/**
	 * Calculates intersection between entity and target segment
	 */
	private void calculateAdaptation() {
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
	 * Setter for the overlap area
	 * 
	 * @param overlapArea
	 *            overlap area size
	 */
	public void setOverlapArea(final double overlapArea) {
		this.overlapArea = overlapArea;
	}

	/**
	 * Getter for the Overlap Area
	 * 
	 * @return Overlapping area size
	 */
	public double getOverlapArea() {
		return this.overlapArea;
	}

	/**
	 * Setter for the Covered Area
	 * 
	 * @param coveredArea
	 *            Cover area
	 */
	public void setCoveredArea(final double coveredArea) {
		this.coveredArea = coveredArea;
	}

	/**
	 * Getter for the covered area
	 * 
	 * @return Covered Area
	 */
	public double getCoveredArea() {
		return this.coveredArea;
	}

	/**
	 * Setter for the target area
	 * 
	 * @param targetArea
	 *            Target segment area
	 */
	public void setTargetArea(final double targetArea) {
		this.targetArea = targetArea;
	}

	/**
	 * Getter for the target area
	 * 
	 * @return Target segment area
	 */
	public double getTargetArea() {
		return this.targetArea;
	}

	/**
	 * Function that tells, in which generation the entity was born
	 * 
	 * @return Number of generation in which has been born
	 */
	public int getBornGeneration() {
		return this.bornGeneration;
	}

	/**
	 * Allows to get all connectors
	 * 
	 * @return List of connectors of particular entity
	 */
	public ArrayList<Connector> getConnectors() {
		return this.connectors;
	}

	/**
	 * Allows to invoke println on he object
	 * 
	 * @return Entity information
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Adaptation = " + (int) (this.getAdaptationSize() * 100)
				+ "%,\t");
		return str.toString();
	}

	/**
	 * Function used to compare two entities
	 * 
	 * @param other
	 *            the other entity that we want to compare to
	 * @return value represents the difference between entities
	 */
	public int compareTo(Entity other) {
		return (this.getAdaptationSize() < other.getAdaptationSize() ? -1
				: (this.getAdaptationSize() > other.getAdaptationSize() ? 1 : compareSegments(other)));
	}
	
	private int compareSegments(Entity other)
	{
		for(int i = 0; i < connectors.size(); ++i) {
			int id1 = connectors.get(i).getSegmentVertex().getId();
			int id2 = other.connectors.get(i).getSegmentVertex().getId();
			
			if( id1 != id2 ) return id1 < id2 ? -1 : 1;
			
			int id3 = connectors.get(i).getTargetSegmentVertex().getId();
			int id4 = other.connectors.get(i).getTargetSegmentVertex().getId();
			
			if( id3 != id4 ) return id3 < id4 ? -1 : 1;
		
		}
		return 0;
	}

	/**
	 * Function that return list of segments of the entity
	 * 
	 * @return ArrayList of Segments
	 */
	public ArrayList<Segment> getSegments() {
		ArrayList<Segment> segments = new ArrayList<Segment>();

		for (Connector connector : this.connectors)
			segments.add(connector.getSegment());

		return segments;
	}
}
