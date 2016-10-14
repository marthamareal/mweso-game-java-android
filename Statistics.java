package mareal.mweso;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.NumberFormat;

class Data {
    public long index;
    public String name;
    public int wins;
    public int losses;
}
public class Statistics {
    /*Values in our row*/
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_WINS = "wins";
    public static final String KEY_LOSSES = "losses";


    private static final String DATABASE_NAME = "stats4";
    private static final String DATABASE_TABLE = "scores";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table Scores (_id integer primary key autoincrement, "
                    + "name text not null, wins text not null, "
                    + "losses text not null);";

    private final Context context;
    private datacollector datac;
    private SQLiteDatabase db;

    public Statistics(Context ctx) {
        this.context = ctx;
        datac = new datacollector(context);
    }


    private static class datacollector extends SQLiteOpenHelper {
        datacollector(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

        }
    }
        /*opening the database*/

    public Statistics open() throws SQLException {
        db = datac.getWritableDatabase();
        return this;
    }

    /*on closing the  daatabase*/
    public void close() {
        datac.close();

    }

    /*inserting thinds in adatabase*/
    public long insert(String name, String wins, String losses) {
        ContentValues old = new ContentValues();
        //old.put(KEY_ROWID,name);
        old.put(KEY_NAME, name);
        old.put(KEY_WINS, wins);
        old.put(KEY_LOSSES, losses);
        return db.insert(DATABASE_TABLE, null, old);
    }

    /*  A method that gets all scores */
    public Cursor getAllStats() {
        return db.query(DATABASE_TABLE, new String[]{
                        KEY_ROWID,
                        KEY_NAME,
                        KEY_WINS,
                        KEY_LOSSES,
                },
                null,
                null,
                null,
                null,
                null);
    }

    /* A method that gets a particular score */
    public Cursor getStat(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{
                                KEY_ROWID,
                                KEY_NAME,
                                KEY_WINS,
                                KEY_LOSSES,

                        },
                        KEY_ROWID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /* updatingthe datbase*/
    public boolean updateStat(long rowId, String name, String wins, String losses) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_WINS, wins);
        args.put(KEY_LOSSES, losses);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Data findName(String name) {
        Data d = new Data();
        Cursor c = getAllStats();
        if (c.moveToFirst()) {
            do {
                if (c.getString(1).equals(name)) {
                    d.index = Long.parseLong(c.getString(0));
                    d.name = c.getString(1);
                    d.wins = Integer.parseInt(c.getString(2));
                    d.losses = Integer.parseInt(c.getString(3));

                    return d;
                }
            } while (c.moveToNext());
        }

        return null;
    }

    public void doEntry(String name, int plus_wins, int plus_losses) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);

        Data entry = findName(name);
        if (entry == null) {
    		/* make new record */
            insert(name,
                    nf.format(plus_wins),
                    nf.format(plus_losses)
            );

        } else {
    		/* update existing record */
            updateStat(entry.index, entry.name,
                    nf.format(entry.wins + plus_wins),
                    nf.format(entry.losses + plus_losses));

        }

    }
}

