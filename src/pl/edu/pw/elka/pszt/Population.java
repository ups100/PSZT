package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Represents population -> some entities
 * 
 * @author Kajo
 */
public class Population {

	/** Amount of first population at least 2! */
	public static final int FIRST_AMOUNT = 50;

	/** List of entities */
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/** Target segment instance */
	private final MultiSegment targetSegment;

	/** Number of generation */
	private int generationNumber = 0;

	/**
	 * C-tor, creates first population entities
	 * 
	 * @param targetSegment Multi segment with target and all segments building target inside
	 */
	public Population(final MultiSegment targetSegment) {
		this.targetSegment = targetSegment;

		for (int i = 0; i < FIRST_AMOUNT; ++i)
			this.entities.add(createRandomEntity());
	}

	/**
	 * Create random generated entity, useful especially for first generation
	 * 
	 * @return Generated entity
	 */
	private Entity createRandomEntity()
	{
		ArrayList<Segment> segments = this.targetSegment.getSegments();
		Entity entity = new Entity(this.generationNumber);

		for (Segment segment : segments) {
			// TODO check random algorithm, except some already chosen vertices
			Connector connector = new Connector(segment, this.targetSegment, false);
			entity.addConnector(connector);
		}
		
		return entity;
	}

	/**
	 * Make next generation by coitus, mutation and natural selection
	 * @return Best adapted entity
	 */
	public Entity nextGeneration()
	{
		this.generationNumber++;

		/**
		 * Choose entities from population to copuplate them
		 */
		ArrayList<Entity> evolvingEntities = getRandomEntitiesFromPopulation();

		/** 
		 * Maybe some other variable will be useful but by now
		 * I will just set the result of copulation to the existing
		 * evolvingEntities variable
		 */
		ArrayList<Entity> children = copulateEntities(evolvingEntities);

		/**
		 * Children mutation
		 */
		for (Entity e: children)
			e.mutateEntity();


		//TODO 2 Variants of selection
		if (false)
		{
			selectTheBestOnes(children);
		}
		else
		{
			// Add children to population
			this.entities.addAll(children);
	
			/**
			 * Get best adapted entities
			 */
			selectBestAdaptedEntities();
		}


		return getMostAdaptedEntity();
	}

	/**
	 * Select best entities and add children to population
	 * 
	 * @param children Children after mutation
	 */
	private void selectTheBestOnes(ArrayList<Entity> children)
	{
//		// my change Variant 1
		//TODO check remove that
		ArrayList<Entity> allEntities = new ArrayList<>(this.entities);
		allEntities.addAll(children);

		Collections.sort(allEntities);
		Collections.reverse(allEntities);

		ArrayList<Entity> newPopulation = new ArrayList<>();

		// Get at most half of parent entities
		int parentsLeft = Math.max(this.entities.size() / 2, this.entities.size() - children.size());

		for (int i = 0; i < allEntities.size(); ++i)
		{
			Entity ent = allEntities.get(i);

			// If parents are enough don't add
			if (this.entities.contains(ent))
				if (parentsLeft <= 0)
					continue;
				else
					--parentsLeft;

			newPopulation.add(ent);

			if (newPopulation.size() == FIRST_AMOUNT)
				break;
		}

		this.entities = newPopulation;
	}

	/**
	 * Selection from all entities in population
	 * Remove(kill) weak entities from population
	 * Leave only best adapted
	 * Each population has at least so many entities how much there was in first population
	 */
	private void selectBestAdaptedEntities() 
	{
//		// my change Variant 2
		//TODO check that
		ArrayList<Entity> check = new ArrayList<Entity>(this.entities);

		Collections.sort(check);
		Collections.reverse(check);

		for (int i = 0; i < check.size(); ++i)
			if (i >= FIRST_AMOUNT)
				this.entities.remove(check.get(i));
	}

	/**
	 * Append new entities to population There is a need to change from public
	 * to private (I guess so)
	 */
	public ArrayList<Entity> copulateEntities(ArrayList<Entity> evolvingEntities)
	{
//		/** Size of afterCopulationList is size of evolvingEntities ^ 2 
//		 * I don't know if it is right (or especially fast) but it is very hippie-style
//		 * everyone with everyone
//		 * It can be easily changed
//		 * */
//		ArrayList<Entity> afterCopulationList = new ArrayList<Entity>();
//		for(int i = 0; i < evolvingEntities.size(); ++i)
//		{
//			for (int j = 0; j < evolvingEntities.size(); ++j)
//			{
//				if (i == j) continue; //no self-love!
//				afterCopulationList.add(evolvingEntities.get(i).copulateWith(evolvingEntities.get(j), this.generationNumber));
//			}
//		}
//		return afterCopulationList;

		// My change commented
		ArrayList<Entity> afterCopulationList = new ArrayList<Entity>();

		// Sort them by better adaptation
		Collections.sort(evolvingEntities);
		Collections.reverse(evolvingEntities);

		for (int i = 0; i < evolvingEntities.size() - 1; i += 2)
		{
			Entity child = evolvingEntities.get(i).copulateWith(evolvingEntities.get(i + 1), this.generationNumber);

			afterCopulationList.add(child);
		}

		return afterCopulationList;
	}

	/**
	 * Get random amount of entities from population
	 * At least 2 entities necessary in population !
	 * 
	 * @return Random amount of entities
	 */
	private ArrayList<Entity> getRandomEntitiesFromPopulation()
	{
//		// yours
//		Random generator = new Random();
//		
//		/** 
//		 * Number is random now, but it can be easily changed 
//		 */
//		int theChosenOnes = 0;
//		while(theChosenOnes < 4)
//		theChosenOnes = generator.nextInt(this.entities.size()-1) + 1;
//		Vector<Integer> previousNumbers = new Vector<>();
//		ArrayList<Entity> list = new ArrayList<Entity>();
//		int i = 0;
//		while (i != theChosenOnes)
//		{
//			Integer tmp = generator.nextInt(this.entities.size());
//			if(!previousNumbers.contains(tmp))
//			{			
//				previousNumbers.add(tmp);
//				/**
//				 * Just getting reference to parents
//				 * instead of cloning them
//				 */
//				//list.add(new Entity(this.entities.get(tmp)));	
//				list.add(this.entities.get(tmp));
//				i++;
//			}
//					
//		}
//		/** Just for tests whether the copy of entities is shallow or deep */
//		//this.entities.clear();
//		return list;

		// My change commented
		Random rand = new Random();
		ArrayList<Integer> lefter = new ArrayList<Integer>();
		ArrayList<Entity> chosen = new ArrayList<Entity>();

		int left = rand.nextInt(this.entities.size() - 1) + 2;
		while (left > 0)
		{
			int tmp = rand.nextInt(this.entities.size());

			if (!lefter.contains(tmp))
			{
				chosen.add(this.entities.get(tmp));
				lefter.add(tmp);
				--left;
			}
		}

		return chosen;
	}

	/**
	 * Get most adapted entity
	 * 
	 * @return Most adapted entity
	 */
	private Entity getMostAdaptedEntity()
	{
		ArrayList<Entity> en = new ArrayList<Entity>(this.entities);
		
		Collections.sort(en);
		Collections.reverse(en);

		return en.get(0);
	}

	/**
	 * Get number of actual generation
	 * 
	 * @return Generation number
	 */
	public int getGenerationNumber() {
		return this.generationNumber;
	}

	/**
	 * Test things if few entities only
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("\t{ \n");

		for (Entity entity : this.entities)
			str.append(entity + "\n");

		str.append("\t}");

		return str.toString();
	}

	public void mutateThem() {
		// TODO Auto-generated method stub
		for (Entity e : this.entities) {
			e.mutateEntity();
		}
		
	}

}