package com.bestroboticsteam.warehouseinterface;

import static org.junit.Assert.*;
import org.junit.Test;

public class InterfaceTest {
	InterfaceView view = new InterfaceView(null);
	InterfaceController controller = new InterfaceController(null, null, null);

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

	@Test
	public void jobProgressTest() {
		// add to job list
		view.setInProgList("job1 : job2 : job3 : job4");
		String actualJob = view.getInProgList();
		String expectedOutput = "job1\njob2\njob3\njob4\n";
		assertEquals(expectedOutput, actualJob);
		
		view.setInProgList("newJob");
		actualJob = view.getInProgList();
		expectedOutput = "newJob\n";
		assertEquals(expectedOutput, actualJob);
		
		// empty job list
		view.emptyProgList();
		actualJob = view.getInProgList();
		expectedOutput = "";
		assertEquals(expectedOutput, actualJob);

	}
	
	@Test
	public void jobFinishedTest(){
		view.setFinishedList("job1 : job2 : job3 : job4");
		String actualJob = view.getFinishedList();
		String expectedOutput = "job1\njob2\njob3\njob4\n";
		assertEquals(expectedOutput, actualJob);
	}

}
