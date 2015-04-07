package geovis14.awesomelocationaccuracytesterapp;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {
	File root;
	File dir;
    File file;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = android.os.Environment.getExternalStorageDirectory();
		dir = new File(root.getAbsolutePath() + "/gpsTestDaten");
		dir.mkdirs();
		file = new File(dir, "Experiment.txt");
		if (file.exists())file.delete();
		
        try {
			PrintWriter pw = new PrintWriter(new FileWriter(
					root.getAbsolutePath()
							+ "//gpsTestDaten//Experiment.txt",
					true));
			pw.append("\n Device: " + android.os.Build.DEVICE
						+ "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void locationClient (View view){
    	
    	Intent intent = new Intent (this, LocationClientMap.class);
    	EditText editLoc= (EditText) findViewById(R.id.editLocation);
    	EditText editWetter= (EditText) findViewById(R.id.editWetter);
    	try {
			PrintWriter pw = new PrintWriter(new FileWriter(
					root.getAbsolutePath()
							+ "//gpsTestDaten//Experiment.txt",
					true));
			pw.append("\n"+ editWetter.getText().toString() +
					  "\n"+ editLoc.getText().toString() + 
					  "\n LocationClient");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	startActivity(intent);
    	
    }
    
    public void locationManager (View view){
    	Intent intent = new Intent (this, LocationClientMap.class);
    	EditText editLoc= (EditText) findViewById(R.id.editLocation);
    	EditText editWetter= (EditText) findViewById(R.id.editWetter);
    	try {
			PrintWriter pw = new PrintWriter(new FileWriter(
					root.getAbsolutePath()
							+ "//gpsTestDaten//Experiment.txt",
					true));
			pw.append("\n"+ editWetter.getText().toString() +
					  "\n"+ editLoc.getText().toString() + 
					  "\n LocationManager");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	startActivity(intent);
    	
    }
}
