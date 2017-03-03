package Job;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Order implements Comparable<Order> {
	
	//do you prefer another type of map?
	private ConcurrentMap<Item, Integer> orderTable;
	private int id;
	private int cancelationNumb;
	private float totalReward = 0f;
	
	public Order(int _id, ConcurrentHashMap<Item, Integer> ot){
		orderTable = ot;
		id = _id;
		cancelationNumb = 0;
		setTotalReward();
	}
	
	public void setCancelation(int i){
		cancelationNumb = 0;
	}
	
	public int getCancelationNumb(){
		return cancelationNumb;
	}
	
	public int getId(){
		return id;
	}
	
	public int getQuantity(Item j){
		return orderTable.get(j);
	}
	
	public Collection getOrderList(){
		return orderTable.keySet();
	}
	
	public ConcurrentMap getOrderTable(){
		return orderTable;
	}
	
	public float getTotalReward(){
		return totalReward;
	}
	
	private void setTotalReward(){
		for(Item e: orderTable.keySet()){
			totalReward += e.getReward() * orderTable.get(e);
		}
	}

	@Override
	public int compareTo(Order compareOrder) {
		float compareReward = compareOrder.getTotalReward();
		
		if(this.totalReward - compareReward > 0) return 1;
		else if(this.totalReward - compareReward == 0) return 0;
		else return -1;
	}
}
