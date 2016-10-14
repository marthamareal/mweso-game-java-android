package mareal.mweso;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;


public class Scoreview extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);

        /* open the database */
      Statistics db = new Statistics(this);

        /* container for stats */
        LinearLayout container = (LinearLayout) findViewById(R.id.StatList);

        /* make the headings */
        LinearLayout headings = new LinearLayout(this);

        TextView heading_name = new TextView(this);
        heading_name.setTextColor(Color.BLACK);
        heading_name.setTextSize(20.0f);
        heading_name.setText("Name");
        heading_name.setWidth(140);
        headings.addView(heading_name);

        TextView heading_wins = new TextView(this);
        heading_wins.setTextColor(Color.BLACK);
        heading_wins.setTextSize(20.0f);
        heading_wins.setText("Win");
        heading_wins.setWidth(100);
        headings.addView(heading_wins);

        TextView heading_losses = new TextView(this);
        heading_losses.setTextColor(Color.BLACK);
        heading_losses.setTextSize(20.0f);
        heading_losses.setText("Loss");
        heading_losses.setWidth(100);
        headings.addView(heading_losses);

        TextView heading_pct = new TextView(this);
        heading_pct.setTextColor(Color.BLACK);
        heading_pct.setTextSize(20.0f);
        heading_pct.setText("%");
        heading_pct.setWidth(100);
        headings.addView(heading_pct);

        TextView heading_blank = new TextView(this);
        heading_blank.setText("");
        heading_blank.setWidth(50);
        headings.addView(heading_blank);

        container.addView(headings);

        /* loop through the stats */
        db.open( );
        Cursor c = db.getAllStats( );
        if (c.moveToFirst( ))
        {
            do {
            		/* make a linear layout for this row */
                LinearLayout row = new LinearLayout(this);

            		/* make a textview for each column */
                TextView name = new TextView(this);
                name.setText(c.getString(1));
                name.setTextColor(Color.DKGRAY);
                name.setWidth(140);
                row.addView(name);

                TextView wins = new TextView(this);
                wins.setText(c.getString(2));
                wins.setTextColor(Color.DKGRAY);
                wins.setWidth(100);
                row.addView(wins);

                TextView losses = new TextView(this);
                losses.setText(c.getString(3));
                losses.setTextColor(Color.DKGRAY);
                losses.setWidth(100);
                row.addView(losses);

                TextView pct = new TextView(this);
                double wins_num = (double) Integer.parseInt(c.getString(2));
                double losses_num = (double) Integer.parseInt(c.getString(3));
                double ties_num = (double) Integer.parseInt(c.getString(4));
                double percent = (wins_num + (ties_num / 2.0)) / (wins_num + losses_num + ties_num);
                NumberFormat nf = NumberFormat.getInstance( );
                nf.setMaximumFractionDigits(3);
                pct.setText(nf.format(percent));
                pct.setTextColor(Color.DKGRAY);
                pct.setWidth(100);
                row.addView(pct);

                TextView blank = new TextView(this);
                blank.setText("");
                blank.setWidth(50);
                row.addView(blank);

            		/* put the row in the layout */
                container.addView(row);
            } while (c.moveToNext( ));
        }
        db.close( );
    }
}

