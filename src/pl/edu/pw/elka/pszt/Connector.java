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
	 * @param targetSegmentVertex Vertex of big one segment to attach
	 */
	public Connector(final Segment segment, final MultiSegment targetSegment)
	{
		this.segment = segment.clone();
		this.targetSegment = targetSegment;

		this.targetSegmentVertex = this.targetSegment.getRandomVertex();
		this.segmentVertex = this.segment.getRandomVertex();
	
		this.segment.moveToVertexByVertex(this.targetSegmentVertex, this.segmentVertex);
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
		return this.segment.getId() + " linked to target vertex " + this.targetSegmentVertex.getId() + " by "
																						+ this.segmentVertex.getId();
	}

}
