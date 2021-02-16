package trail;

public class Location {
	
	private String location;
	private int orderNum;
		
	Location(String location, int orderNum){
		this.location = location;
		this.orderNum = orderNum;
	}

    public String getName(){
        return location;
    }

    public int getOrderNum(){
        return orderNum;
    }

}
