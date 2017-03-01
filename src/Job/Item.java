package Job;

import java.awt.Point;

public class Item {
	
	private final char code;
	private final float weight;
	private final float reward;
	private Point position;

	public Item(char _code, float _weight, float _reward){
		this.code = _code;
		this.weight = _weight;
		this.reward = _reward;
		this.position = new Point();
	}
	
	public void setPosition(int x, int y){
		this.position.x = x;
		this.position.y = y;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public int getPositionX(){
		return position.x;
	}
	
	public int getPositionY(){
		return position.y;
	}
	
	public float getWeight(){
		return weight;
	}
	
	public float getReward(){
		return reward;
	}
}