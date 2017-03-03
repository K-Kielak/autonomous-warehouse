package Job;

import java.awt.Point;

public class Item {
	
	private final String code;
	
	private final float weight;
	private final float reward;
	private Point position;

	public Item(String _code, float _weight, float _reward){
		this.code = _code;
		this.weight = _weight;
		this.reward = _reward;
	}
	
	public void setPosition(Point p){
		this.position = p;
	}
	
	public String getCode(){
		return code;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public float getWeight(){
		return weight;
	}
	
	public float getReward(){
		return reward;
	}

	public int hashCode(){
		return code.hashCode();
	}
	
	public boolean equals(Item i){
		return ( this.code.equals(i.getCode()) );
	}
}
