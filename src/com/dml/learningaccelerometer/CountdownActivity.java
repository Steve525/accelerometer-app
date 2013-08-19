package com.dml.learningaccelerometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

public class CountdownActivity extends Activity {
	
	private CountDownTimer countDownTimer;
	public TextView tv;
	private final long startTime = 6 * 1000;
	private final long interval = 1 * 1000 - 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		
		tv = (TextView) findViewById(R.id.timer);
		countDownTimer = new CountDownTimer(startTime, interval) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				tv.setText(String.valueOf(millisUntilFinished/1000));
			}
			
			@Override
			public void onFinish() {
				Intent intent = new Intent(getBaseContext(), AccelerometerActivity.class);
				startActivity(intent);
				finish();
			}
		};
		countDownTimer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.countdown, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			countDownTimer.cancel();
			finish();
			return true;
		case KeyEvent.KEYCODE_HOME:
			countDownTimer.cancel();
			finish();
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

}
