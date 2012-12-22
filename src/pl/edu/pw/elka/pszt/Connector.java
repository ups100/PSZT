package pl.edu.pw.elka.pszt;



/**
 * Class representing connection between, vertices of tangram element and target big one element
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Connector {

	/** Target Segment reference */
	private MultiSegment targetSegment;

	/** 
	 * Target segment vertex to which segment is connected to 
	 */
	private Vertex targetSegmentVertex;

	/** 
	 * Vertex of segment connected by 
	 * */
	private Vertex segmentVertex;
	
	/** 
	 * Segment connected to target segment by vertices 
	 */
	private Segment segment;

	
	
	
	/**
	 * C-tor
	 * Clones segment object set as reference targetVertex & targetSegment which is 'readOnly'
	 * Moves segment, if specified, by segmentVertex to attach id to targetVertex
	 * 
	 * @param segment Segment connecting to target by
	 * @param targetSegment Target tangram segment as reference
	 * @param targetVertex Vertex of segment which connects by to target
	 * @param segmentVertex  Vertex of targetSegment to connect segment to
	 * @param noMove If we want to not moving it 
	 */
	public Connector(final Segment segment, final MultiSegment targetSegment, final Vertex segmentVertex,
																			final Vertex targetVertex, boolean noMove)
	{
		this.segment = segment.clone();
		this.targetSegment = targetSegment;

		this.targetSegmentVertex = targetVertex;
		this.segmentVertex = this.segment.getVertexById(segmentVertex.getId());
	
		if (!noMove)
			this.segment.moveToVertexByVertex(targetVertex, this.segmentVertex);
	}

	/**
	 * C-tor
	 * random connection vertices and invoke other c-tor
	 * 
	 * @param segment Segment connecting to target by
	 * @param targetSegment Target tangram segment as reference
	 * @param noMove If we do not want to move it
	 */
	public Connector(final Segment segment, final MultiSegment targetSegment, boolean noMove)
	{
		this(segment, targetSegment, segment.getRandomVertex(), targetSegment.getRandomVertex(), noMove);
	}

	/**
	 * Clone connector element
	 * @return cloned connector
	 */
	public Connector clone()
	{
		return new Connector(this.segment, this.targetSegment, this.segmentVertex, this.targetSegmentVertex, true);
	}

	/**
	 * @return Segment connected by
	 */
	public Segment getSegment()
	{
		return this.segment;
	}

	/**
	 * @return Target segment connected to
	 */
	public MultiSegment getTargetSegment()
	{
		return this.targetSegment;
	}

	/**
	 * @return Target segment vertex connected to
	 */
	public Vertex getTargetSegmentVertex()
	{
		return this.targetSegmentVertex;
	}

	/**
	 * @param Target segment vertex connected to
	 */
	public void setTargetSegmentVertex(final Vertex targetSegmentVertex)
	{
		this.targetSegmentVertex = targetSegmentVertex;
	}

	/**
	 * @return the segmentVertex
	 */
	public Vertex getSegmentVertex()
	{
		return this.segmentVertex;
	}

	/**
	 * @param segmentVertex the segmentVertexId to set
	 */
	public void setSegmentVertex(final Vertex segmentVertex)
	{
		this.segmentVertex = segmentVertex;
	}
	
	/**
	 * Get string representation
	 * 
	 * @return ID of segment and what to what is connected
	 */
	public String toString()
	{
		return this.segment + "\tlinked to target vertex " + this.targetSegmentVertex + " by "
																						+ this.segmentVertex;
	}

}
