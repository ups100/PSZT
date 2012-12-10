package pl.edu.pw.elka.pszt;

/**
 * Class representing connection between, vertices of tangram element and target big one element
 * 
 * @author Kajo
 */
public class Connector {

	/** Target Segment reference */
	private MultiSegment targetSegment;

	/** Segment connected to target segment by vertices */
	private Segment segment;

	/** Vertex of target segment connected to */
	private Vertex targetSegmentVertex;

	/** Vertex of segment connected by */
	private Vertex segmentVertex;

	/**
	 * C-tor
	 * 
	 * @param segment Segment connecting to target by
	 * @param targetSegment Target tangram segment as reference
	 * @param targetVertex Vertex of segment which connects by to target
	 * @param segmentVertex  Vertex of targetSegment to connect segment to
	 */
	public Connector(final Segment segment, final MultiSegment targetSegment, final Vertex targetVertex,
																							final Vertex segmentVertex)
	{
		this.segment = segment.clone();
		this.targetSegment = targetSegment;

		this.targetSegmentVertex = targetVertex;
		this.segmentVertex = segmentVertex;
	
		this.segment.moveToVertexByVertex(this.targetSegmentVertex, this.segmentVertex);
	}

	/**
	 * C-tor
	 * random connection vertices and invoke other c-tor
	 * 
	 * @param segment Segment connecting to target by
	 * @param targetSegment Target tangram segment as reference
	 */
	public Connector(final Segment segment, final MultiSegment targetSegment)
	{
		this(segment, targetSegment, segment.getRandomVertex(), targetSegment.getRandomVertex());
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
