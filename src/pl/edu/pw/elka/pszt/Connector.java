package pl.edu.pw.elka.pszt;

/**
 * Class representing connection between, vertices of tangram element and target big one element
 * 
 * @author Kajo
 */
public class Connector {

	/** Vertex of big one connected to */
	private int bigOneVertexId;

	/** Segment id connected to big one by vertices */
	private int segmentId;

	/** Vertex of segment connected by */
	private int segmentVertexId;

	/**
	 * C-tor
	 * 
	 * @param bigOneVertex Vertex of big one segment to attach
	 * @param segmentId Segment id which is being combined
	 * @param segmentVertexId Vertex id of segment combining by
	 */
	public Connector(final int bigOneVertex, final int segmentId, final int segmentVertexId)
	{
		this.bigOneVertexId = bigOneVertex;
		this.segmentId = segmentId;
		this.segmentVertexId = segmentVertexId;
	}

	
}
