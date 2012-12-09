package pl.edu.pw.elka.pszt;

import java.util.ArrayList;

/**
 * Represents population -> some entities
 * 
 * @author Kajo
 */
public class Population {

	/** Amount of first population at least 2! */
	public static final int FIRST_AMOUNT = 2;

	/** List of entities */
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/** Target segment instance */
	private MultiSegment targetSegment;
	

	public Population(ArrayList<Segment> segments, final MultiSegment targetSegment)
	{
		this.targetSegment = targetSegment;

		int tries = 0; // TODO remove, only to show that random is ready
		//for (int i = 0; i < FIRST_AMOUNT; ++i) // Uncoment that
		while(true) // remove that
		{
			Entity entity = new Entity();

			for (Segment segment : segments)
			{
				// TODO check random algorithm, except some already chosen vertices ?
				Connector connector = new Connector(segment, targetSegment);
	
				entity.addConnector(connector);
			}
		
			this.entities.add(entity);
			System.out.println("try number: " + tries++ + " => " + entity.getAdaptationSize());
			if (entity.getAdaptationSize() > 0.9) // remove that
				break;
		}
	}

	/**
	 * Make next generation by coitus, mutation and natural selection
	 */
	public void nextGeneration()
	{
		copulateEntities();
		// TODO mutate entities every time ?
		// TODO sometime select entities
	}

	/**
	 * Append new entites to population
	 */
	private void copulateEntities()
	{
		// TODO choose entites and copulate them
	}

	/**
	 * Select more adapted entites
	 */
	private void selectEntites()
	{
		// TODO sometime remove weak entities
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
