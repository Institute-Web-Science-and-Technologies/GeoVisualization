package com.example.guiprototype;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_USER="com.example.guiprototype",
			EXTRA_GAMEID="whatever";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void swipeScreen (View view){
    	Intent intent = new Intent (this, SwipeScreenSnake.class);
    	EditText editText= (EditText) findViewById(R.id.editUsername);
    	String username= editText.getText().toString();
    	intent.putExtra(EXTRA_USER, username);
    	intent.putExtra(EXTRA_GAMEID, "0");
    	startActivity(intent);
    	
    }
    public void start (View view){
    }
}
