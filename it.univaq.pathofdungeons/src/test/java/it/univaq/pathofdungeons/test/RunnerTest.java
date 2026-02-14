package it.univaq.pathofdungeons.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.univaq.pathofdungeons.Runner;
import it.univaq.pathofdungeons.domain.dungeon.Dungeon;
import it.univaq.pathofdungeons.domain.dungeon.DungeonSizes;
import it.univaq.pathofdungeons.domain.items.equippable.Equippable;
import it.univaq.pathofdungeons.domain.items.equippable.Rarities;
import it.univaq.pathofdungeons.game.impl.DungeonGenerator;
import it.univaq.pathofdungeons.game.impl.ItemFactory;

class RunnerTest {
	
	public Runner it;

	@BeforeEach
	void setUp() {
		it = new Runner();
	}

	@Test
	void testDungeonMapRepeated(){
		final int TEST_RUNS = 100;
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < TEST_RUNS; i++){
			testDungeonMapSingle();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Repeated dungeon generation test completed. Average execution time: " + ((endTime - startTime) / TEST_RUNS) + "ms");
	}

	@SuppressWarnings("all")
	void testDungeonMapSingle(){
		Dungeon dungeon = DungeonGenerator.createDungeon(DungeonSizes.EXTRA_LARGE.size());
	}

	
	@Test
	void testDungeonMap(){
		long startTime = System.currentTimeMillis();
		Dungeon dungeon = DungeonGenerator.createDungeon(DungeonSizes.LARGE.size());
		long endTime = System.currentTimeMillis();
		System.out.println(dungeon);
		System.out.println("Single dungeon generation test completed. Execution time: " + (endTime - startTime) + "ms");
		assertTrue(true);
	}

	@Test
	void testRandomItem(){
		System.out.println(ItemFactory.getRandomEquippable());
		assertTrue(true);
	}

	@Test
	void testRandomItems(){
		for(int i = 0; i < 1000; i++){
			Equippable item = ItemFactory.getRandomEquippable();
			assertEquals(item.getRarity().getNumStats(), item.getStats().size());
			if(item.getRarity().equals(Rarities.EPIC) || item.getRarity().equals(Rarities.LEGENDARY)) assertNotNull(item.getEffect());
			if(item.getRarity().equals(Rarities.LEGENDARY)) assertNotNull(item.getSpell());
		}
	}

}
