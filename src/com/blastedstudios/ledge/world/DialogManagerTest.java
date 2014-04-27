package com.blastedstudios.ledge.world;

import static org.junit.Assert.*;

import org.junit.Test;

public class DialogManagerTest {
	@Test public void testSplitRenderable() {
		assertTrue(DialogManager.splitRenderable("My name is", 5).contains("\n"));
		assertTrue(!DialogManager.splitRenderable("My name is", 80).contains("\n"));
		assertTrue(DialogManager.splitRenderable("My name isMy name isMy name isMy name isMy name isMy name is", 20).contains("\n"));
	}
}
