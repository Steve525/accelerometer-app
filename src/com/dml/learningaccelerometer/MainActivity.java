package com.dml.learningaccelerometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accelerometer, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public void recordBite(View view) {
		Intent intent = new Intent(this, CountdownActivity.class);
		intent.putExtra("is_bite", true);
		startActivity(intent);
	}
	
	public void recordNonBite(View view) {
		Intent intent = new Intent(this, CountdownActivity.class);
		intent.putExtra("is_bite", false);
		startActivity(intent);
	}

}
