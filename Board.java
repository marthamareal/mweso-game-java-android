package checkWin;

public class Board {
	
	public int [] Valid(int [] Array,int board [],int from,int to,int a){
		Mmain mn=new Mmain();
		if (board [to]==0){
			mn.board[from]=0;
			mn.board[to]=a;			//updates board
			updateA(Array, from, to);//updates the player array
			}
		else
		{System.out.println("invalid move");}
		return Array;
	} 
	
	public int [] updateA(int [] Array,int from, int to){
		for (int i=0;i<3;i++){
			if (Array[i]==from){
				Array[i]=to;}
			}
		return Array;}
}