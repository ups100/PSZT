package pl.edu.pw.elka.pszt;

import java.util.ArrayList;

/**
 * Class containing representation of app model such as segments and target segment made of small tangram segments
 * 
 * @author Kajo
 */
public class Model {

	/** Union of the tangram segments = target segment */
	private Segment targetSegment = null;

	/** List of tangram segments building big one target segment */
	private ArrayList<Segment> segments = new ArrayList<Segment>();

	/**
	 * Adds segment to list of tangram segments building big target segment
	 * and build target from the beginning of tangram segments
	 * 
	 * @param segment Segment to add
	 */
	public void addSegment(final Segment segment) {
		this.segments.add(segment);

		try
		{
			this.buildTargetSegment();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Build one big target segment from list of tangram segments
	 * 
	 * @throws Exception
	 */
	private void buildTargetSegment() throws Exception
	{
		this.targetSegment = null;

		for (Segment s : this.segments)
			this.targetSegment = s.makeUnionSegment(this.targetSegment);
	}

	/**
	 * Get segment instance by given id from segment list
	 * 
	 * @param id Segment id
	 * @return Segment instance if found equal id
	 * @throws Exception if no id found
	 */
	public Segment getSegmentById(final int id) throws Exception
	{
		for (Segment s : this.segments)
			if (id == s.getId())
				return s;

		throw new Exception("No segment found in model");
	}

	/**
	 * Get list of segments
	 * 
	 * @return List of segments building target tangram element
	 */
	public ArrayList<Segment> getSegments()
	{
		return this.segments;
	}

	/**
	 * Get tangram target segment
	 * 
	 * @return Target segment
	 */
	public Segment getTargetSegment()
	{
		return this.targetSegment;
	}

}
