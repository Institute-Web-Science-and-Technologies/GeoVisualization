package com.example.guiprototype;



import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_USER="com.example.guiprototype";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void swipeScreen (View view){
    	Intent intent = new Intent (this, SwipeScreen.class);
    	EditText editText= (EditText) findViewById(R.id.editUsername);
    	String username= editText.getText().toString();
    	intent.putExtra(EXTRA_USER, username);
    	startActivity(intent);
    	
    }
    public void start (View view){
    	Intent intent = new Intent (this, MapScreen.class);
    	EditText editText= (EditText) findViewById(R.id.editUsername);
    	String username= editText.getText().toString();
    	intent.putExtra(EXTRA_USER, username);
    	startActivity(intent);
    }
}
