
package edu.mills.cs180a.mathdatabase;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * An activity that enables a user to add or delete data from
 * table {@link MathDatabaseOpenHelper#TABLE_SQUARES} of the
 * database opened by {@link MathDatabaseOpenHelper}.
 * 
 * @author ellen.spertus@gmail.com (Ellen Spertus)
 */
public class MainActivity extends ActionBarActivity {
  private static final String TAG = "MainActivity";
  private SQLiteDatabase mDb;
  private InsertSquaresAsyncTask mAsyncTask;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mDb = new MathDatabaseOpenHelper(this).getWritableDatabase();

    final Button addDataButton = (Button) this.findViewById(R.id.addDataButton);
    final Button cancelAddButton = (Button) this.findViewById(R.id.cancelAddButton);
    Button deleteDataButton = (Button) this.findViewById(R.id.deleteButton);
    final ProgressBar progressBar = 
            (ProgressBar) this.findViewById(R.id.addDataProgressBar);
    final EditText fromEditText = (EditText) this.findViewById(R.id.fromEditText);
    final EditText toEditText = (EditText) this.findViewById(R.id.toEditText);

    addDataButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: Validate input.
        int from = Integer.parseInt(fromEditText.getText().toString());
        int to = Integer.parseInt(toEditText.getText().toString());
        mAsyncTask = new InsertSquaresAsyncTask(mDb, addDataButton, 
                    cancelAddButton, progressBar, from, to);
        mAsyncTask.execute();
      }
    });

    cancelAddButton.setOnClickListener(new OnClickListener(){
      @Override
      public void onClick(View v) {
        mAsyncTask.cancel(true);
        Toast.makeText(MainActivity.this, "Add cancelled", Toast.LENGTH_LONG).show();
      }
    });
    
    deleteDataButton.setOnClickListener(new OnClickListener(){
      @Override
      public void onClick(View v) {
        deleteData();
        Toast.makeText(MainActivity.this, "Deleted data", Toast.LENGTH_LONG).show();
      }
    });
  }


  private void deleteData() {
    Log.d(TAG, "About to delete all data from table " + MathDatabaseOpenHelper.TABLE_SQUARES);
    mDb.delete(MathDatabaseOpenHelper.TABLE_SQUARES, null, null);
    Log.d(TAG, "Done deleting all data from table " + MathDatabaseOpenHelper.TABLE_SQUARES);
  }
}
