package com.bestroboticsteam.localization;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class Localization {

	private double[][] locationLikelyhoods;
	private GridMap map;
	
	
	public Localization() {
		map = MapUtils.createRealWarehouse();
	}
	
	
	
}
