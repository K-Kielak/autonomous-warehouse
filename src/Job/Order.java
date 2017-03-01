package Job;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Order {
	
	private ConcurrentMap<Item, Integer> orderTable;
	
	public Order(int _id){
		orderTable = new ConcurrentHashMap<Item, Integer>();
	}
	
	public void addItem(Item j, int q){
		orderTable.put(j, q);
	}
	
	public int getQuantity(Item j){
		return orderTable.get(j);
	}
	
	public ConcurrentMap getOrderList(){
		return orderTable;
	}
}
