package com.bestroboticsteam.warehouseinterface;
import com.bestroboticsteam.robotsmanagement.RobotInfo;
import com.bestroboticsteam.robotsmanagement.RobotsManager;
import java.awt.Point;
import rp.robotics.MobileRobotWrapper;
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
	private static MobileRobotWrapper<MovableRobot> wrapper;
	private static RobotInfo[] robotArray;
	
	public static GridMapVisualisation robots(RobotsManager robots) {
		robotArray = new RobotInfo[robots.getRobotInfos().length];
		int numOfRobots = getRobotNumber();
		robotArray = getRobotInfos(robots);
		for (int i = 0; i< numOfRobots; i++){
			GridPose gridStart = new GridPose(getPosX(i), getPosY(i), Heading.PLUS_Y);
			wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, false), map.toPose(gridStart));
			RobotSimController controller = new RobotSimController(wrapper.getRobot(), map, gridStart, robotArray[i]);
			new Thread(controller).start();
		}
		MapVisualisationComponent.populateVisualisation(mapVis, sim);
		return mapVis;
	}
	
	public static int getRobotNumber(){
		int numOfRobots = robotArray.length;
		return numOfRobots;
	}
	
	public static RobotInfo[] getRobotInfos(RobotsManager robots){
		return robots.getRobotInfos();
	}
	
	public static int getPosX(int robot){
		Point pos = robotArray[robot].getPosition();
		return pos.x;
	}
	
	public static int getPosY(int robot){
		Point pos = robotArray[robot].getPosition();
		return pos.y;
	}
}