package edu.mills.cs180a.mathdatabase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Insert a range of rows into the {@link MathDatabaseOpenHelper#TABLE_SQUARES}
 * table of the database created in {@link MathDatabaseOpenHelper}.
 * 
 * @author ellen.spertus@gmail.com (Ellen Spertus)
 */
public class InsertSquaresAsyncTask extends AsyncTask<Void, Integer, Void> {
  private static final String TAG = "InsertSquaresAsyncTask";
  private int mFrom, mTo;
  private Button mAddDataButton, mCancelAddButton;
  private SQLiteDatabase mDb;
  private ProgressBar mProgressBar;

  /**
   * Construct an asynchronous task to write a range of squares
   * to the given database.
   * 
   * @param db a writable database
   * @param addDataButton a button to hide while the task is in progress
   * @param cancelAddButton a button to show while the task is in progress
   * @param progressBar a view on which progress can be shown
   * @param from the start of the range of values that should be inserted
   * @param to the end of the range of values that should be inserted
   */
  InsertSquaresAsyncTask(SQLiteDatabase db, Button addDataButton, Button cancelAddButton,
          ProgressBar progressBar, int from, int to) {
    mDb = db;
    mAddDataButton = addDataButton;
    mCancelAddButton = cancelAddButton;
    mProgressBar = progressBar;
    mFrom = from;
    mTo = to;
  }

  @Override
  protected void onPreExecute() {
    // Change button visibility.
    if (mAddDataButton != null) {
      mAddDataButton.setVisibility(View.GONE);
    }
    if (mCancelAddButton != null) {
      mCancelAddButton.setVisibility(View.VISIBLE);
    }

    // Set up progress bar.
    if (mProgressBar != null) {
      mProgressBar.setVisibility(View.VISIBLE);
      mProgressBar.setMax(mTo - mFrom);
    }
  }

  @Override
  protected Void doInBackground(Void... params) {
    ContentValues values = new ContentValues();
    mDb.beginTransaction();
    try {
      for (int i = mFrom; i < mTo; i++) {
        values.put(MathDatabaseOpenHelper.COLUMN_BASE, i);
        values.put(MathDatabaseOpenHelper.COLUMN_SQUARE, i * i);
        mDb.insert(MathDatabaseOpenHelper.TABLE_SQUARES, null, values);

        // Publish an update every 100 numbers.
        if (i % 100 == 0) {
          Log.d(TAG, "Publishing progress: " + (i - mFrom));
          publishProgress(i - mFrom);
        }
        if (isCancelled()) {
          return null;
        }
      }
      mDb.setTransactionSuccessful();
      return null;
    } finally {
      mDb.endTransaction();
    }
  }

  @Override
  protected void onProgressUpdate(Integer... progress) {
    if (progress == null || progress.length != 1) {
      throw new IllegalArgumentException("Exactly one Integer was expected.");
    }
    if (mProgressBar != null) {
      mProgressBar.setProgress(progress[0]);
    }
  }

  private void onDone() {
    // Change button visibility.
    if (mAddDataButton != null) {
      mAddDataButton.setVisibility(View.VISIBLE);
    }
    if (mCancelAddButton != null) {
      mCancelAddButton.setVisibility(View.GONE);
    }

    // Hide progress bar.
    if (mProgressBar != null) {
      mProgressBar.setVisibility(View.GONE);
    }
  } 

  @Override
  protected void onPostExecute(Void result) {
    onDone();
  }

  @Override
  protected void onCancelled() {
    onDone();
  }
}
