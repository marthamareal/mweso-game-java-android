package checkWin;

import java.util.Scanner;

public class Players {

	public int [] play(int [] Array,int []board,int p)//tells player to select the position of the token and to where he is placing it
{
	System.out.println("please enter position from");
	Scanner from = new Scanner(System.in);
	System.out.println("please enter position to");
	Scanner to = new Scanner(System.in);
	
	Board bd = new Board();
	bd.Valid(Array,board, from.nextInt(), to.nextInt(), p);//checks if move is valid and if so changes the player array and position in board
	
	return Array;

}}