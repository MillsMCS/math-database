package edu.mills.cs180a.mathdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper to create a database with a single table holding
 * integer values and their squares.
 * 
 * @author ellen.spertus@gmail.com (Ellen Spertus)
 */
public class MathDatabaseOpenHelper extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "math.db";
  private static final int DATABASE_VERSION = 1;

  static final String TABLE_SQUARES = "squares";
  static final String COLUMN_BASE = "base";
  static final String COLUMN_SQUARE = "square";

  private static final String DATABASE_CREATE = "create table "
          + TABLE_SQUARES + "("
          + COLUMN_BASE + " integer unique not null primary key, "
          + COLUMN_SQUARE + " integer not null);";

  public MathDatabaseOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SQUARES);
    onCreate(db);
  }
}
