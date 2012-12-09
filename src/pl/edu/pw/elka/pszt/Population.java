package pl.edu.pw.elka.pszt;

import java.util.ArrayList;

/**
 * Represents population -> some entities
 * 
 * @author Kajo
 */
public class Population {

	/** Amount of first population at least 2! */
	public static final int FIRST_AMOUNT = 10;

	/** List of entities */
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/** Target segment instance */
	private final MultiSegment targetSegment;

	/** Number of generation */
	private int generationNumber = 0;

	/**
	 * C-tor,
	 * creates first population entities
	 * 
	 * @param targetSegment Multi segment with target and all segments building target inside
	 */
	public Population(final MultiSegment targetSegment)
	{
		this.targetSegment = targetSegment;

		for (int i = 0; i < FIRST_AMOUNT; ++i)
			this.entities.add(createRandomEntity());
	}

	/**
	 * Create random generated entity, usefull especially for first generation
	 * 
	 * @return Generated entity
	 */
	private Entity createRandomEntity()
	{
		ArrayList<Segment> segments = this.targetSegment.getSegments();
		Entity entity = new Entity(this.generationNumber);

		for (Segment segment : segments)
		{
			// TODO check random algorithm, except some already chosen vertices ?
			Connector connector = new Connector(segment, this.targetSegment);

			entity.addConnector(connector);
		}

		return entity;
	}

	/**
	 * Make next generation by coitus, mutation and natural selection
	 */
	public void nextGeneration()
	{
		this.generationNumber++;
		copulateEntities();
		// TODO mutate entities every time ?
		// TODO sometime select entities
	}

	/**
	 * Append new entities to population
	 */
	private void copulateEntities()
	{
		// TODO choose entities and copulate them
	}

	/**
	 * Select more adapted entites
	 */
	private void selectEntites()
	{
		// TODO sometime remove weak entities
	}

	/**
	 * Get number of actual generation
	 * 
	 * @return Generation number
	 */
	public int getGenerationNumber()
	{
		return this.generationNumber;
	}

	/**
	 * Test things if few entities only
	 */
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("\t{ \n");

		for (Entity entity : this.entities)
			str.append(entity + "\n");

		str.append("\t}");

		return str.toString();
	}

}
