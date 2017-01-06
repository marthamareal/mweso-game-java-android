package com.example.fred.mwesogame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends AppCompatActivity {
	MediaPlayer player;
	int k;
	/* number of players */
	int num_players;


	enum Cell {
		X,
		O,
		E
	}
	enum Outcome {
		NONE,
		P1_WON,
		P2_WON,
		COMPUTER_WON
	}

	private Cell [] cells;

	public void setupGame( ) {
		cells = new Cell [9];
	}

	public boolean checkTriple(int c1, int c2, int c3, Cell value) {
		//to test whether the cells c1, c2, c3 contain the same value
		return (cells[c1] == cells[c2]) && (cells[c2] == cells[c3]) && (cells[c3] == value);
	}

	public Outcome checkGame( ) {
		if (checkTriple(0, 1, 2, Cell.X)){

		return Outcome.P1_WON;//if the cell(0, 1 and 2) all have same value X
	}
		else if(checkTriple(0, 1, 2, Cell.O)) {//else player two or computer wins

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;

		}
		if(checkTriple(3, 4, 5, Cell.X)){
			return Outcome.P1_WON;
		}

		else if(checkTriple(3, 4, 5, Cell.O)) {

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
 		}
		if(checkTriple(6, 7, 8, Cell.X)){
			return Outcome.P1_WON;
		}
		else if(checkTriple(6, 7, 8, Cell.O)) {

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
		}
		if(checkTriple(0, 3, 6, Cell.X)){
			return Outcome.P1_WON;}
		else if(checkTriple(0, 3, 6, Cell.O)) {

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
		}
		if(checkTriple(1, 4, 7, Cell.X)){

			return Outcome.P1_WON;
		}
		else if(checkTriple(1, 4, 7, Cell.O)) {

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
		}
		if(checkTriple(2, 5, 8, Cell.X)){
			return Outcome.P1_WON;
		}
		else if(checkTriple(2, 5, 8, Cell.O)) {

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
		}
		if(checkTriple(2, 4, 6, Cell.X)){

			return Outcome.P1_WON;
		}
		else if(checkTriple(2, 4, 6, Cell.O)) {
			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
		}
		if(checkTriple(0, 4, 8, Cell.X)){

			return Outcome.P1_WON;
		}
		else if(checkTriple(0, 4, 8, Cell.O)) {

			return (num_players == 1) ? Outcome.COMPUTER_WON : Outcome.P2_WON;
		}
		return Outcome.NONE;
	}

	public void finishGame(Outcome out, int call_no, String p1_name, String p2_name) {
		/* new status */
		final TextView status2 = (TextView) findViewById(R.id.TextView01);

		if(call_no == 1) {
			/* set the win animation */
//			getSupportActionBar().hide();
			setContentView(R.layout.win);
			status2.setText(status.getText( ));
			final ImageView img = (ImageView) findViewById(R.id.ImageView01);
			//noinspection ResourceType
			AnimationDrawable ad = (AnimationDrawable) getBaseContext( ).getResources( )
				.getDrawable(R.anim.fworks);
			img.setImageDrawable(ad);

			ad.start( );

			/* this dialog will call back with call_no = 2 and p1_name in place*/
			NameDialog diag = new NameDialog(this, out, 1,"", num_players);

			diag.show( );
			return;
		} else if(call_no == 2) {
			if(num_players == 2) {

				/* this dialog will call back with call_no = 3 and p1_name and p2_name in place*/
				NameDialog diag = new NameDialog(this, out, 2, p1_name, num_players);
				diag.show( );
				return;
							}
		}
		/* we have the names we need! */
		if(p2_name == "") p2_name = "Computer";
		/* set debug text */
		String winner = (out == Outcome.P1_WON) ? p1_name : p2_name;
		String loser = ((out == Outcome.P2_WON) || (out == Outcome.COMPUTER_WON)) ? p1_name : p2_name;
		/* open up the stats database */
		StatsDatabase db = new StatsDatabase(this);
		db.open( );

		/* record p1 stats */
		switch(out) {
			case P1_WON:

	db.recordWin(p1_name);
				break;

			case P2_WON:
			case COMPUTER_WON:
				db.recordLoss(p1_name);
				break;
		}

		if(num_players == 2) {
			/* record p2 stats */
			switch(out) {
				case P2_WON:
					db.recordWin(p2_name);
					break;
				case P1_WON:
					db.recordLoss(p2_name);
					break;
			}
		}

		/* close up the database */
		db.close();

		/* leave this activity */
		finish( );
	}


	public boolean canWin(int index, Cell player) {
		/* save old val */
		Cell old = cells[index];

		/* overwrite */
		cells[index] = player;

		/* does this result in win? */
		boolean can;
		Outcome out = checkGame( );
		can = (out != Outcome.NONE);

		/* restore value */
		cells[index] = old;

		return can;
	}

	public int rankMove(int index) {
		/* if the space is taken, it is a 0 */
        if(cells[index] != Cell.E)
            return 0;

		/* check if we can win here */
		if(canWin(index, Cell.O))
			return 100;

		/* check if opponent could win here */
		if(canWin(index, Cell.X))
			return 50;

		/* center square */
		if(index == 4)
			return 25;

		/* corner */
		if((index == 0) || (index == 2) || (index == 6) || (index == 8))
			return 10;

		/* meh */
		return 5;

	}


	public void setImage(int index){
		if((turn%2)==0)
			buttons[index].button.setImageResource(R.drawable.xbutton);
		else
			buttons[index].button.setImageResource(R.drawable.obutton);
	}





	public void Computer(){

		if(cells[8]==Cell.O || cells[5]==Cell.E || cells[4]==Cell.E || cells[7]==Cell.E) {
			int rankings[] = new int[9];

		/* get the rankings */
			for (int i = 0; i < 9; i++) {
				if (cells[i] == Cell.E) {
					rankings[i] = rankMove(i);
				}
			}

		/* choose best ranking */
			int best_ranking = 0;
			for (int i = 0; i < 9; i++) {
				if (rankings[i] > rankings[best_ranking])
					best_ranking = i;
			}


			buttons[best_ranking].button.performClick();

		}
		/* if(cells[0]==Cell.O || cells[1]==Cell.E || cells[4]==Cell.E ||cells[3]==Cell.E)
			go with the best ranking
		for (int i=0;i<9;i++)
			buttons[i].button.performClick( );

		if(cells[1]==Cell.O || cells[0]==Cell.E || cells[4]==Cell.E || cells[2]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[2]==Cell.O || cells[1]==Cell.E || cells[4]==Cell.E || cells[5]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[3]==Cell.O || cells[0]==Cell.E && cells[4]==Cell.E && cells[6]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[5]==Cell.O || cells[2]==Cell.E || cells[4]==Cell.E || cells[8]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[6]==Cell.O || cells[3]==Cell.E || cells[4]==Cell.E || cells[7]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[7]==Cell.O || cells[6]==Cell.E || cells[4]==Cell.E || cells[8]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[8]==Cell.O || cells[5]==Cell.E || cells[4]==Cell.E || cells[7]==Cell.E)
			for (int i=0;i<9;i++)
				buttons[i].button.performClick( );

		if(cells[4]==Cell.O && cells[1]==Cell.E && cells[2]==Cell.E && cells[0]==Cell.E && cells[3]==Cell.E
				&& cells[5]==Cell.E && cells[6]==Cell.E && cells[7]==Cell.E && cells[8]==Cell.E)
			setImage(1);*/


	}

/*	public void doAi( ) {
		int rankings [] = new int[9];

	//	 get the rankings
		for(int i = 0; i < 9; i++) {
			if(cells[i] == Cell.E) {
                rankings[i] = rankMove(i);
            }
		}

// /* choose best ranking
		int best_ranking = 0;
		for(int i = 0; i < 9; i++) {
			if(rankings[i] > rankings[best_ranking])
				best_ranking = i;
		}
//		 go with the best ranking
		buttons[best_ranking].button.performClick( );
	} */

	/* called when a button is clicked */
	//cell = integer for cell number i.e ( 0 to 8 )
	//turn = integer for identifying player's turn
	public void update(int cell, int turn) {
		/* set the cell */
		cells[cell] = ((turn % 2) == 0) ? Cell.X : Cell.O;
		/* check for a winner */
		Outcome o;
			switch(o = checkGame( )) {
				case P1_WON:
					if(turn>=6) {
						status.setText("Player 1 Won!!");
						finishGame(o, 1, "", "");
					}else{
						status.setText(((turn % 2) == 0) ? "O's Turn" : "X's Turn");
					}
					break;
				case P2_WON:
					if(turn>=6) {
						status.setText("Player 2 Won!!");
						finishGame(o, 1, "", "");
					}else{
						status.setText(((turn % 2) == 0) ? "O's Turn" : "X's Turn");
					}
					break;
				case COMPUTER_WON:
					if(turn>=6) {
						status.setText("You Have Lost!!");
						finishGame(o, 1, "", "");
					}else{
						status.setText(((turn % 2) == 0) ? "O's Turn" : "X's Turn");
					}
					break;
				case NONE:
					status.setText(((turn % 2) == 0) ? "O's Turn" : "X's Turn");
					break;
			}
		// time to do computer move if needed
		if((num_players == 1) && status.getText().toString().equalsIgnoreCase("O's Turn")){
			Computer();
		}
	}

	/* our buttons */
	private AnimatedButton buttons [] = new AnimatedButton [9];


	/* text view for debugging */
	public TextView status;

	/* setup buttons and interface */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);
		 player = MediaPlayer.create(Game.this, R.raw.music);
		player.start();

        setupGame();

        status = (TextView) findViewById(R.id.TextView01);

        /* set number of players */
        if(getIntent( ).getType( ).equalsIgnoreCase("1"))
        	num_players = 1;
        else
        	num_players = 2;

        status.setText("X's Turn");


		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toastred,
				(ViewGroup) findViewById(R.id.ToastRoot));

		TextView text = (TextView) layout.findViewById(R.id.textToast);
		text.setText("Winning starts after six moves..\n\nPlayer with green starts first\n\n\nWish you success!!!!!");
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();

        /* setup buttons */
        int button_ids [] = {R.id.ImageButton01, R.id.ImageButton02, R.id.ImageButton03,
        					 R.id.ImageButton04, R.id.ImageButton05, R.id.ImageButton06,
        					 R.id.ImageButton07, R.id.ImageButton08, R.id.ImageButton09};

        for(int i=0; i<9; i++) {
        	buttons[i] = new AnimatedButton((ImageButton) findViewById(button_ids[i]), this, i);
        }
		cells[0]=Cell.X;cells[1]=Cell.X;cells[2]=Cell.X;cells[6]=Cell.O;cells[7]=Cell.O;cells[8]=Cell.O;
		cells[3]=Cell.E;cells[4]=Cell.E;cells[5]=Cell.E;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(player.isPlaying())
		{
			player.pause();
			k=player.getCurrentPosition();}
		else
		{
		player.start();
		}
		switch (item.getItemId()){
			case R.id.pause:

					final Dialog dialog = new Dialog(this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.pause_layout);
					dialog.show();
					dialog.setCancelable(false);

					TextView resume = (TextView)dialog.findViewById(R.id.resume);
					resume.setOnClickListener(new View.OnClickListener() {
						@Override
					public void onClick(View v) {
						dialog.dismiss();
							player.seekTo(k);
							player.start();
					}
				});
					TextView viewStats = (TextView)dialog.findViewById(R.id.cash);

					viewStats.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(Game.this, StatsView.class);
							startActivity(i);
						}
					});
					TextView restart = (TextView)dialog.findViewById(R.id.restart);
				restart.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						recreate();
						dialog.dismiss();
					}
				});
				TextView help = (TextView)dialog.findViewById(R.id.help);
				help.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(Game.this, Help.class);
						startActivity(i);
					}
				});
				TextView levels = (TextView)dialog.findViewById(R.id.settings);
				levels.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(Game.this, Levels.class);
						startActivity(i);
					}
				});
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(Game.this);
		alert.setMessage("Do you really want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Game.this.finish();player.pause();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alert.create();
		alertDialog.show();

	}

	int c0c1c3c4,c1c2c0c4,c3c0c4c6,c4c0c1c2c3c5c6c7c8,c2c1c4c5,c5c2c4c8,c6c3c4c7,c7c6c4c8,c8c5c7c4;

	int global_turn = 0,turn,dont;boolean save;

	class AnimatedButton {

		public ImageButton button;		/* which button we control */
		Game the_game;			/* refernce to the game object */
		int cell_number;		/* which cell do I refer to */

		/* X goes first, so we know which turn is which */

		public void setImag(){
			if((turn%2)==0)
				button.setImageResource(R.drawable.xbutton);
			else
				button.setImageResource(R.drawable.obutton);
		}


		public void setBefore(){
			String string = the_game.status.getText().toString();
			if((string.equalsIgnoreCase("X's Turn") && cells[cell_number]==Cell.X) || (string.equalsIgnoreCase("O's Turn") && cells[cell_number]==Cell.O)){
				button.setImageResource(android.R.color.transparent);
				the_game.status.setText("Choose another cell");
				cells[cell_number]=Cell.E;
				save = true;
				dont=1;
			}else{
				dont=0;
			}
		}

		public void setAfter(){
			if(dont==1){
				turn = global_turn;//turn is equal to zero which is the even number that corresponds to X
				global_turn++;//global turn is incremented to one which is odd number that corresponds to O
				setImag();
				save = false;
				if((turn % 2) == 0) cells[cell_number] = Cell.X; else cells[cell_number] = Cell.O;
				the_game.update(cell_number, turn);
			}
		}

		public void gameRules(int index){
			if(index==0){
				if((cells[index]!=Cell.E)&&(!save)){
					if(cells[1]==Cell.E||cells[4]==Cell.E||cells[3]==Cell.E){
						c0c1c3c4 = 1;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c1c2c0c4==2 || c3c0c4c6==4 || c4c0c1c2c3c5c6c7c8==5) {
						setAfter();
						c1c2c0c4=0;c3c0c4c6=0;c4c0c1c2c3c5c6c7c8=0;
					}
				}
			}else if(index==1){
				if((cells[index]!=Cell.E) && (!save)){
					if(cells[0]==Cell.E||cells[4]==Cell.E||cells[2]==Cell.E) {
						c1c2c0c4 = 2;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c0c1c3c4==1 || c2c1c4c5==3 || c4c0c1c2c3c5c6c7c8==5){
						setAfter();
						c0c1c3c4=0;c2c1c4c5=0;c4c0c1c2c3c5c6c7c8=0;
					}
				}
			}else if(index==2){
				if((cells[index]!=Cell.E) && (!save)){
					if(cells[1]==Cell.E||cells[4]==Cell.E||cells[5]==Cell.E){
						c2c1c4c5 = 3;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c1c2c0c4==2 || c4c0c1c2c3c5c6c7c8==5 || c5c2c4c8==6) {
						setAfter();
						c1c2c0c4=0;c4c0c1c2c3c5c6c7c8=0;c5c2c4c8=0;
					}
				}
			}else if(index==3) {
				if((cells[index]!=Cell.E) && (!save)){
					if(cells[0]==Cell.E||cells[4]==Cell.E||cells[6]==Cell.E){
						c3c0c4c6 = 4;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c0c1c3c4 == 1 || c4c0c1c2c3c5c6c7c8==5 || c6c3c4c7 == 7) {
						setAfter();
						c0c1c3c4=0;c4c0c1c2c3c5c6c7c8=0;c6c3c4c7=0;
					}
				}
			}else if(index==4){
				if ((cells[index]!=Cell.E) && (!save)) {
					c4c0c1c2c3c5c6c7c8 = 5;
					setBefore();
				}else if(cells[index]==Cell.E){
					if(c0c1c3c4==1 || c1c2c0c4==2 || c3c0c4c6==4 || c2c1c4c5==3 || c5c2c4c8==6 || c6c3c4c7==7 || c7c6c4c8==8 || c8c5c7c4==9){
						setAfter();
						c0c1c3c4=0;c1c2c0c4=0;c3c0c4c6=0;c2c1c4c5=0;c5c2c4c8=0;c6c3c4c7=0;c7c6c4c8=0;c8c5c7c4=0;
					}
				}
			}else if(index==5){
				if ((cells[index]!=Cell.E) && (!save)){
					if(cells[2]==Cell.E||cells[4]==Cell.E||cells[8]==Cell.E) {
						c5c2c4c8 = 6;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c2c1c4c5==3 || c4c0c1c2c3c5c6c7c8==5 || c8c5c7c4==9){
						setAfter();
						c2c1c4c5=0;c4c0c1c2c3c5c6c7c8=0;c8c5c7c4=0;
					}
				}
			}else if(index==6){
				if ((cells[index]!=Cell.E) && (!save)) {
					if(cells[3]==Cell.E||cells[4]==Cell.E||cells[7]==Cell.E){
						c6c3c4c7 = 7;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c3c0c4c6==4 || c4c0c1c2c3c5c6c7c8==5 || c7c6c4c8==8){
						setAfter();
						c3c0c4c6=0;c4c0c1c2c3c5c6c7c8=0;c7c6c4c8=0;
					}
				}
			}else if(index==7){
				if ((cells[index]!=Cell.E) && (!save)) {
					if(cells[6]==Cell.E||cells[4]==Cell.E||cells[8]==Cell.E){
						c7c6c4c8 = 8;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c6c3c4c7==7 || c4c0c1c2c3c5c6c7c8==5 || c8c5c7c4==9){
						setAfter();
						c6c3c4c7=0;c4c0c1c2c3c5c6c7c8=0;c8c5c7c4=0;
					}
				}
			}else if(index==8){
				if ((cells[index]!=Cell.E) && (!save)) {
					if(cells[7]==Cell.E||cells[4]==Cell.E||cells[5]==Cell.E) {
						c8c5c7c4 = 9;
						setBefore();
					}
				}else if(cells[index]==Cell.E){
					if(c5c2c4c8==6 || c4c0c1c2c3c5c6c7c8==5 || c7c6c4c8==8){
						setAfter();
						c5c2c4c8=0;c4c0c1c2c3c5c6c7c8=0;c7c6c4c8=0;
					}
				}
			}
		}

		AnimatedButton(ImageButton b, Game g, final int cell) {

        button = b;
        the_game = g;
        cell_number = cell;
        global_turn = 0;

		/* event handler */
			button.setOnClickListener(new Button.OnClickListener( ) {
				public void onClick(View v) {
					gameRules(cell_number);
        	  }
			});
		}
	}
}













