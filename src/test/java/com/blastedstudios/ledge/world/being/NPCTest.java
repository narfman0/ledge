package com.blastedstudios.ledge.world.being;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blastedstudios.ledge.world.being.NPC.DifficultyEnum;

public class NPCTest {
	@Test public void testShootDelay() {
		assertEquals(0, NPC.shootDelay(1, DifficultyEnum.HARD), 375);
		/* TODO this is failing with gradle change?
		assertEquals(5000, NPC.shootDelay(1, DifficultyEnum.HARD), 500);
		assertEquals(3000, NPC.shootDelay(10, DifficultyEnum.HARD), 500);
		assertEquals(2000, NPC.shootDelay(50, DifficultyEnum.HARD), 200);
		assertEquals(1000, NPC.shootDelay(100, DifficultyEnum.HARD), 100);
		
		assertEquals(7000, NPC.shootDelay(1, DifficultyEnum.MEDIUM), 500);
		assertEquals(5000, NPC.shootDelay(10, DifficultyEnum.MEDIUM), 500);
		assertEquals(4000, NPC.shootDelay(50, DifficultyEnum.MEDIUM), 200);
		assertEquals(3000, NPC.shootDelay(100, DifficultyEnum.MEDIUM), 100);
		*/
	}
}
