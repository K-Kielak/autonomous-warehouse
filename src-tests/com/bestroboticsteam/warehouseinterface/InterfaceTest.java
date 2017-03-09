package com.bestroboticsteam.warehouseinterface;

import static org.junit.Assert.*;
import org.junit.Test;

public class InterfaceTest {
	InterfaceView view = new InterfaceView();

	@Test
	public void jobListTest() {

		// add to job list
		view.setJobList("job1 : job2 : job3 : job4");
		String actualJob = view.getJobList();
		String expectedOutput = "job1\njob2\njob3\njob4\n";
		assertEquals(expectedOutput, actualJob);

		// add one item to job list
		view.addOneToJobList("job5");
		actualJob = view.getJobList();
		expectedOutput = "job1\njob2\njob3\njob4\njob5\n";
		assertEquals(expectedOutput, actualJob);

		// empty job list
		view.emptyJobList();
		actualJob = view.getJobList();
		expectedOutput = "";
		assertEquals(expectedOutput, actualJob);

	}
}
