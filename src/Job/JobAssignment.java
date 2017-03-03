package Job;

public class JobAssignment {
	
	private final JobSelection selection;

	public JobAssignment(JobSelection selection){
		this.selection = selection;
	}
	
	public Order getNextOrder(){
		Order nextOrder = selection.take();
		//this.orderItem
		return nextOrder;
	}
	
	
}
