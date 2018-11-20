package checkWin;

public class CheckWin {
	
	
	public String check(int [] Array,int a){
		String status ="continue";
		if (Array[0]==0 && Array[1]==4 && Array[2]==8){
			status ="win";
			System.out.println("you have won");
		}
		else if (Array[0]==1 && Array[1]==4 && Array[2]==7){
			status ="win";
			System.out.println("you have won");
		}
		else if (Array[0]==2 && Array[1]==4 && Array[2]==6){
			status ="win";
			System.out.println("you have won");
		}
		else if (Array[0]==3 && Array[1]==4 && Array[2]==5){
			status ="win";
			System.out.println("you have won");
		}
		else status="continue";
		return status;
		}
}
