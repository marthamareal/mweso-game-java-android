package checkWin;

import java.util.Arrays;

public class Mmain {
	static int board []={1,1,1,0,0,0,2,2,2};	//holds current places of the players 
	
	public static void main(String [] args){
		int maxsize=100000;
		int array1 []={0,1,2};					//holds indexes of player1 on the board array
		int array2 []={6,7,8};					//holds indexes of player2 on the board array
		
		Players pl=new Players();
		CheckWin cw=new CheckWin();
		
		for (int count=3;count< maxsize;count ++)
		{
			if (count%2==0)
			{	System.out.println("player 2");
			System.out.println(Arrays.toString(array2));
				pl.play(array2,board,2);
				System.out.println(Arrays.toString(board));
				cw.check(array2, 2);			//checks if player has won
				}
			else {
				System.out.println("player 1");
				System.out.println(Arrays.toString(array1));
				pl.play(array1,board,1);
				System.out.println(Arrays.toString(board));
				cw.check(array1, 1);
				}}}}