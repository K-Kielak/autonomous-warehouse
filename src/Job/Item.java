package Job;

import java.awt.Point;

public class Item {
	
	private final char code; //for sure code has only one character? I know that on slides it has, 
													 //but if you are not 100% sure I would use String instead of char.
	private final float weight;
	private final float reward;
	private Point position;

	public Item(char _code, float _weight, float _reward){
		this.code = _code;
		this.weight = _weight;
		this.reward = _reward;
		this.position = new Point();
	}
	
	public void setPosition(int x, int y){ //is position of item changing during simulation? 
																				 //if not it should be moved to constructor 
																				 //either way it shouldn't take x and y as parameters but Point object
																				 //ok, if x and y as parameters would be really convinient then ok
																				 //but still there should be method just with Point object as parameter
		this.position.x = x;
		this.position.y = y;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public int getPositionX(){ //We have getPosition() method already, I don't think we need this one.
		return position.x;
	}
	
	public int getPositionY(){ //We have getPosition() method already, I don't think we need this one.
		return position.y;
	}
	
	public float getWeight(){
		return weight;
	}
	
	public float getReward(){
		return reward;
	}

	//add hashCode() method (because this class is used as a key in hash table inside of Order class)

	//add equals() methods (because this class is used as a key in hash table inside of Order class)
}
