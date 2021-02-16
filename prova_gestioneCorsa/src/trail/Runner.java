package trail;

import java.util.ArrayList;
import java.util.List;

public class Runner {
	
	private String name;
	private String surname;
	private int bibNumber;
		
	public List<Long> checkPoints = new ArrayList<>();	
	
	Runner(String name, String surname, int bibNumber){
		this.name = name;
		this.surname = surname;
		this.bibNumber = bibNumber;	
	}
   
    public int getBibNumber(){
        return bibNumber;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }
     

}
