package artint;

public class AI {
	 
	public static int sum(int [] Array){
		int total=0;
		for(int i =0;i<3;i++){
			total=total+ Array[i];
		}
		return total;
	}
	public static int difference(int total){
		int diff;
		if (total<12){
		diff =12-total;}
		else{diff=total-12;}
		return diff;
	}
	public int [] better(int [] array){
		int best=0;
		int [] K={0,0,0};
		for (int i=0;i<10;i++){
			//call AI method to play move which returns array of positions of player tokens
			K=array;
			int one =sum(array);
			one = difference(one);
		if (one < best){best=one;}
		//i want if new array is smaller return that array else return old array
		 }
		return K;
		
	}
	public static void main(String [] args){
		//int [] magic={6,2,3};
		// i want to call sum then get difference 
				//magnitude
		//System.out.println(best);
	}
}
