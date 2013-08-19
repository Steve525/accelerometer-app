package com.dml.learningaccelerometer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccelerometerActivity extends Activity implements SensorEventListener {

	private static final String LOG_TAG = "Bite recording and file saving testing";
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	TextView tvSuccess;
	List<Entry> entries;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accelerometer);
		
		tvSuccess = (TextView) findViewById(R.id.success);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		entries = new ArrayList<Entry>();
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
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if the accuracy changes.
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}
//		float[] values = event.values;
//		String x = String.format(Locale.US, "%.4f", values[0]);
//		String y = String.format(Locale.US, "%.4f", values[1]);
//		String z = String.format(Locale.US, "%.4f", values[2]);
//		StringBuilder sb = new StringBuilder();
//		sb.append("X-axis: " + x).append("\n").append("Y-axis: " + y).append("\n").append("Z-axis: " + z + "\n");
//		tvSuccess.setText(sb.toString());
//		
//		Entry entry = new Entry(x, y, z, event.timestamp);
//		entries.add(entry);
		
		// debugging movement and understanding how it works...
		float x = event.values[0];
	    float y = event.values[1];
	    float z = event.values[2];
		float mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
		float mAccel = 0;
		mAccel = mAccel * 0.9f + mAccelCurrent * 0.1f;
		String out = System.currentTimeMillis() + ", " + mAccelCurrent;
		Log.d("onSensorChanged", out);
	}
	
	private void writeEntriesToXml() {
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement;
			if (getIntent().getBooleanExtra("is_bite", false))
				rootElement = doc.createElement("bite");
			else
				rootElement = doc.createElement("non_bite");
			doc.appendChild(rootElement);
	 
			for (Entry entry : entries) {
				Element event = doc.createElement("movement_event");
				rootElement.appendChild(event);
				
				Element x = doc.createElement("x-axis");
				x.appendChild(doc.createTextNode(entry.getX()));
				event.appendChild(x);
				
				Element y = doc.createElement("y-axis");
				y.appendChild(doc.createTextNode(entry.getY()));
				event.appendChild(y);
				
				Element z = doc.createElement("z-axis");
				z.appendChild(doc.createTextNode(entry.getZ()));
				event.appendChild(z);
				
				Element timestamp = doc.createElement("timestamp");
				timestamp.appendChild(doc.createTextNode(String.valueOf(entry.getTimeStamp())));
				event.appendChild(timestamp);
			}
			
			File file = new File(getBaseContext().getFilesDir(), "test.xml");
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public void finishRecording(View view) {
//		Toast.makeText(this, entries.size() + "", Toast.LENGTH_LONG).show();
//		writeEntriesToXml();
		finish();
	}
	
	private class Entry {
		private String x;
		private String y;
		private String z;
		private long timeStamp;
		
		public Entry(String x, String y, String z, long timeStamp) {
			this.setX(x);
			this.setY(y);
			this.setZ(z);
			this.setTimeStamp(timeStamp);
		}

		public String getX() {
			return x;
		}

		public void setX(String x) {
			this.x = x;
		}

		public String getY() {
			return y;
		}

		public void setY(String y) {
			this.y = y;
		}

		public String getZ() {
			return z;
		}

		public void setZ(String z) {
			this.z = z;
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
	}
	
}
