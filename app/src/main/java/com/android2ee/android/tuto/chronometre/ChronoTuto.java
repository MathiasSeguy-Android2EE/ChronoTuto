/**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br>
 * Produced by <strong>Dr. Mathias SEGUY</strong> with the smart contribution of <strong>Julien PAPUT</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 * Belongs to <strong>Mathias Seguy</strong></br>
 * ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br>
 * </br>
 * For any information (Advice, Expertise, J2EE or Android Training, Rates, Business):</br>
 * <em>mathias.seguy.it@gmail.com</em></br>
 * *****************************************************************************************************************</br>
 * Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 * Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br>
 * Sa propriété intellectuelle appartient à <strong>Mathias Séguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br>
 * </br>
 * Pour tous renseignements (Conseil, Expertise, Formations J2EE ou Android, Prestations, Forfaits):</br>
 * <em>mathias.seguy.it@gmail.com</em></br>
 * *****************************************************************************************************************</br>
 * Merci à vous d'avoir confiance en Android2EE les Ebooks de programmation Android.
 * N'hésitez pas à nous suivre sur twitter: http://fr.twitter.com/#!/android2ee
 * N'hésitez pas à suivre le blog Android2ee sur Developpez.com : http://blog.developpez.com/android2ee-mathias-seguy/
 * *****************************************************************************************************************</br>
 * com.android2ee.android.tuto</br>
 * 25 mars 2011</br>
 */
package com.android2ee.android.tuto.chronometre;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

/**
 * @author (Julien PAPUT sous la direction du Dr. Mathias Séguy)
 * @goals This class aims to:
 *        <ul>
 *        <li>This class use a chronometer</li>
 *        </ul>
 */
public class ChronoTuto extends Activity {

	/**
	 * The DeltaStopped String, witch permit to store the delta time
	 */
	private static final String DELTA_STOPPED = "deltaStopped";
	/**
	 * The IsRunning String, witch permit to store the IsRunning Boolean
	 */
	private static final String IS_RUNNING = "isRunning";
	/**
	 * The IsRunning String, witch permit to store the IsRunning Boolean
	 */
	private static final String IS_BACK_TO_DEATH = "isActivityDestroyed";
	/**
	 * The baseTime witch permit to store the basetime of the chronometer
	 */
	private static final String BASE_TIME = "baseTime";
	/**
	 * tag for the log
	 */
	private static final String tag = "ChronoTuto";
	/******************************************************************************************/
	/** Attributes **************************************************************************/
	/******************************************************************************************/
	/** The Chronometer Widget */
	Chronometer chronometer;
	/** This attribute store the */
	long baseTime = 0;
	/**
	 * set the pausing boolean to "false"
	 **/
	boolean pausing = false;
	/**
	 * Init the deltapaused to 0
	 */
	long deltaPaused = 0;
	/**
	 * THe IsRunning Boolean
	 */
	boolean isRunning = false;

	/**
	 * To know if the activity is recreated
	 */
	boolean activityRecreation = false;
	/******************************************************************************************/
	/** Define the Handler ********************************************************************/
	/******************************************************************************************/
	/**
	 * Define the Handler
	 */
	Handler handler = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(tag, "onCreate called");
		super.onCreate(savedInstanceState);
		if (null != savedInstanceState) {
			activityRecreation = true;
		}
		setContentView(R.layout.main);

		// initiate the chronometer
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		chronometer.stop();
		baseTime = SystemClock.elapsedRealtime();
		chronometer.setText(makeDisplayableTime(baseTime));
		/*
		 * 
		 * 
		 * 
		 * /****************************************************************************************
		 */
		/** Adding listeners *********************************************************************/
		/*****************************************************************************************/
		// listener on the start button
		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startChrono();
			}
		});

		// listener on the stop button
		Button btnStop = (Button) findViewById(R.id.btnPause);
		btnStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pauseChrono();
			}
		});

		// listener on the reset button
		Button btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetChrono();
			}
		});
		// tickchronometerListener
		chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
			/**
			 * onChronometerTick() //On each chronometer tick
			 */
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// display the time into the chronometer
				chronometer.setText(makeDisplayableTime(SystemClock.elapsedRealtime()));
			}
		});

	}

	/******************************************************************************************/
	/** Managing Chrono Actions (stop, pause, resume) **************************************************************************/
	/******************************************************************************************/

	/**
	 * startChrono() //start chronometer
	 */
	public void startChrono() {
		// set running ti true
		isRunning = true;
		// if pausing is false, set the chronometer basetime to the elapsed real time
		if (!pausing) {
			baseTime = SystemClock.elapsedRealtime();
		} else {
			// else set the chronometer basetime to the moment where you push the pause button
			deltaPaused = SystemClock.elapsedRealtime() - deltaPaused;
			baseTime = baseTime + deltaPaused;
		}
		// set pausing to false
		pausing = false;
		// start the chronometer
		chronometer.start();
	}

	/**
	 * stopChrono() //stop chronometer
	 */
	public void pauseChrono() {
		// stop the chronometer
		chronometer.stop();
		// if isRunning boolean is true
		if (isRunning) {
			// save the elapsedtime
			deltaPaused = SystemClock.elapsedRealtime();
		}
		// set pausing to true, and running to false
		pausing = true;
		isRunning = false;
	}

	/**
	 * resetChrono() //reset chronometer
	 */
	public void resetChrono() {
		// if pausing is fasle, set the chronometer's basetime to the elapsed real time
		if (!pausing) {
			baseTime = SystemClock.elapsedRealtime();
		} else {
			pausing = false;
		}
		// display time
		chronometer.setText(makeDisplayableTime(baseTime));
	}

	/**
	 * MakeDisplayableTime
	 * 
	 * @return The String time
	 */
	public String makeDisplayableTime(long SystemClockElapsedRealtime) {
		long minutes = ((SystemClockElapsedRealtime - baseTime) / 1000) / 60;
		long seconds = ((SystemClockElapsedRealtime - baseTime) / 1000) % 60;
		StringBuilder currentTimeStrB = new StringBuilder(minutes < 10 ? "0" + minutes : String.valueOf(minutes));
		currentTimeStrB.append(":");
		currentTimeStrB.append(seconds < 10 ? "0" + seconds : String.valueOf(seconds));
		return currentTimeStrB.toString();
	}

	/******************************************************************************************/
	/** Managing LifeCycle **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.v(tag, "onResume called, isRunning " + isRunning + " pausing " + pausing + " activityBackToPause "
				+ activityRecreation);
		// here is the method where you need to intialize your variable again
		super.onResume();

		if (isRunning) {
			if (activityRecreation) {
				// if isRunning is true and the activity is recreate, start the chronometer
				startChrono();
			}
		} else {
			// init the chronometers value
			baseTime = SystemClock.elapsedRealtime();
			chronometer.setText(makeDisplayableTime(baseTime));
		}
		// Set the recreation mode to false (the activity is running now)
		activityRecreation = false;
	}

    @Override
    protected void onPause() {
        super.onPause();
        chronometer.stop();
        isRunning=false;
    }

    @Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.v(tag, "onSaveInstanceState called");
		// Save instance some state
		outState.putLong(BASE_TIME, baseTime);
		outState.putBoolean(IS_RUNNING, isRunning);
		deltaPaused = SystemClock.elapsedRealtime();
		outState.putLong(DELTA_STOPPED, deltaPaused);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.v(tag, "onRestoreInstanceState called");
		// restore instance some state
		super.onRestoreInstanceState(savedInstanceState);
		pausing = true;
		baseTime = savedInstanceState.getLong(BASE_TIME);
		deltaPaused = savedInstanceState.getLong(DELTA_STOPPED);
		isRunning = savedInstanceState.getBoolean(IS_RUNNING);
	}

}