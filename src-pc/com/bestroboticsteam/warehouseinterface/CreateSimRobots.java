package com.bestroboticsteam.warehouseinterface;
import lejos.robotics.RangeFinder;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.control.RandomGridWalk;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.MapVisualisationComponent;
public class CreateSimRobots {
	public static GridMap map = MapUtils.createRealWarehouse();
	public static MapBasedSimulation sim = new MapBasedSimulation(map);
	public static GridMapVisualisation mapVis = new GridMapVisualisation(map, sim.getMap());
	
	public static GridMapVisualisation robots() {
		int robots = 3;
		for (int i = 0; i< robots; i++){
			GridPose gridStart = new GridPose(3*i, 0, Heading.PLUS_Y);
			MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
					map.toPose(gridStart));
		}
		MapVisualisationComponent.populateVisualisation(mapVis, sim);
		return mapVis;
	}
}