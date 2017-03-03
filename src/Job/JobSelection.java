package Job;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobSelection {
	
	private BlockingQueue<Order> queue = new LinkedBlockingQueue<Order>();
	private Collection<Item> itemList;
	private Collection<Point> dropLocation;
	
	public JobSelection(String path){
		
		ReadData reader = new ReadData();
		itemList = reader.readItemData(path);
		dropLocation = reader.readDropData(path);
		
		Collection<Order> orderList = reader.readOrderData(path);
		setQueue(orderList);
	}
	
	public Collection<Point> getDropLocation(){
		return dropLocation;
	}

	public Order take(){

		while (true) {
			try {
				return (queue.take());
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void setQueue(Collection<Order> orderList) {
		
		Comparator<Order> comparator = new Comparator<Order>() {
			   @Override
			   public int compare(Order o1, Order o2) {
			       return o1.compareTo(o2);
			   }
			};

		
		Order[] list = orderList.toArray(new Order[orderList.size()]);
		Arrays.sort(list, comparator);
		
		for(Order order: list)
			queue.offer(order);
	}

}
