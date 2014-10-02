/**
 * File: $URL: $
 *
 * Abstract: Activity to display Settings UI
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 16th Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android;

import android.app.Activity;

import com.forside.android.Hades9780759519732HA.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * This activity is to handle Uniberg Settings.
 * 
 * @author ScrollMotion
 * 
 */
public class UnibergSettings extends Activity {

	private static final int MIN_FONT = 20; // minimum font size value
	private static final int MAX_FONT = 50; //maximum font size value.
	private WebView text; // reference to the sample text view in the Settings
	// screen
	private UIManager mUIManager = UIManager.getInstance(); // reference to
	// UIManager object
	private Button done_button;
	private SeekBar seekBar;
	private int previousProgress;
	private ToggleButton toggle;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);
		mUIManager = UIManager.getInstance();
		done_button = (Button) findViewById(R.id.done);
		done_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				done();
			}
		});
		text = (WebView) findViewById(R.id.sampleText);
		String data = "<html><body>Sample Text Size</body></html>";
		text.getSettings().setMinimumFontSize(MIN_FONT);
		text.loadData(data, "text/html", "utf-8");
		text.getSettings().setSupportZoom(false);

		seekBar = (SeekBar) findViewById(R.id.intensitySlider);
		seekBar.setKeyProgressIncrement(1);

		seekBar.setMax(MAX_FONT - MIN_FONT);
		SharedPreferences myPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (myPrefs.getBoolean("orientationChange", false)) {
			seekBar.setProgress(myPrefs.getInt("progress", 0));
			Editor edit = myPrefs.edit();
			edit.putBoolean("orChange", false);
			edit.commit();
		}

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				text.getSettings().setDefaultFontSize(
						text.getSettings().getDefaultFontSize() + progress
								- previousProgress);

				previousProgress = progress;

			}
		});
		toggle = (ToggleButton) findViewById(R.id.ToggleButton01);
		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					seekBar.setEnabled(false);
				else
					seekBar.setEnabled(true);
			}
		});
		/**
		 * Commenting for time being, since functionality not clear
		 */
		/*
		 * TableRow row1 = (TableRow) findViewById(R.id.instructions); TableRow
		 * row2 = (TableRow) findViewById(R.id.aboutForSide);
		 * row1.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * Toast.makeText(UnibergSettings.this, "Not yet implemented.",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * } }); row2.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * Toast.makeText(UnibergSettings.this, "Not yet implemented.",
		 * Toast.LENGTH_LONG).show();
		 * 
		 * } });
		 */
	}

	/**
	 * This method gets called when the Done button is pressed.
	 */
	private void done() {
		mUIManager.setDefaultFontSize(text.getSettings().getDefaultFontSize());
		mUIManager.setMinimumFontSize(MIN_FONT);
		mUIManager.setMaximumFontSize(MAX_FONT);
		text.getSettings().setSupportZoom(true);
		UnibergSettings.this.finish();

	}

	/**
	 * This method gets called when the Settings activity comes to front.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		seekBar.setProgress((int) (mUIManager.getDefaultFontSize() - MIN_FONT));
		// seekBar.setProgress((int) (mUIManager.getDefaultFontSize()));
		text.getSettings().setDefaultFontSize(
				UIManager.getInstance().getCurrentWebView().getSettings()
						.getDefaultFontSize());
		text.setInitialScale(UIManager.getInstance().getScale());
		SharedPreferences myPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		toggle.setChecked(myPrefs.getBoolean("lock", false));
	}

	/**
	 * This method gets called when the orientation of device changes.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Checks the orientation of the screen
		SharedPreferences myPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor edit = myPrefs.edit();
		edit.putBoolean("orientationChange", true);
		edit.putInt("progress", seekBar.getProgress());
		edit.commit();
		super.onConfigurationChanged(newConfig);

	}

	/**
	 * This method gets called when the Settings activity goes to background.
	 */
	@Override
	protected void onPause() {
		SharedPreferences myPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor edit = myPrefs.edit();
		edit.putBoolean("lock", toggle.isChecked());
		edit.commit();
		text.getSettings().setSupportZoom(true);
		super.onPause();
	}
}
