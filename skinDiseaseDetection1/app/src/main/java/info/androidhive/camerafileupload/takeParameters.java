package info.androidhive.camerafileupload;

import info.androidhive.camerafileupload.AndroidMultiPartEntity.ProgressListener;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class takeParameters extends Activity {
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private ProgressBar progressBar;
	private TextView txtPercentage;
	private ImageView imgPreview;
	private VideoView vidPreview;
	private ProgressDialog waitbar ;
	private Button proceed ;

	/*
	blood
	puss
	watering
	pain
	swell
	itch
	redness
	no. of spots
	scaling/peeling
	skin elevation
	*/
	String blood;
	String puss;
	String watering;
	String pain;
	String swelling;
	String itchiness;
	String redness;
	String spots;
	String scaling;
	String elevation;

	Boolean affectedHand,
			affectedFeet,
			affectedArms,
			affectedForearms,
			affectedThighs,
			affectedGenitalia,
			affectedArmpits,
			affectedFingerwebs,
			affectedWrists,
			affectedNavel;

	String complexion,
		duration,
		age,
		gender;


	String session_code;
	String filepath ;

	long totalSize = 0;
	TextView bloodText,
			pussText,
			wateringText,
			painText,
			swellText,
			itchText,
			rednessText,
			spotsText,
			scalingText,
			elevationText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_parameters);
		waitbar = new ProgressDialog(this);
		txtPercentage = (TextView) findViewById(R.id.txtPercentageParameter);
		proceed = (Button) findViewById(R.id.proceed);
		progressBar = (ProgressBar) findViewById(R.id.progressBarParameter);
		/*imgPreview = (ImageView) findViewById(R.id.imgPreview);
		vidPreview = (VideoView) findViewById(R.id.videoPreview);*/


		final String PREFS_NAME = "MyPrefsFile";

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


		if (settings.getBoolean("my_first_time", true)) {

			Toast t = Toast.makeText(getApplicationContext(), "First run help wizard" , Toast.LENGTH_SHORT ) ;
			t.show() ;
			showMessageOnThisPage();
			settings.edit().putBoolean("my_first_time", false).commit();

		}


		waitbar.setCancelable(true);
		waitbar.setProgressStyle(ProgressDialog.STYLE_SPINNER) ;
		waitbar.setMessage("Work in Progress ...");
		waitbar.setCanceledOnTouchOutside(false);

		Intent i = getIntent();

		// image or video path that is captured in previous activity
		session_code = i.getStringExtra("session_code");

		filepath = i.getStringExtra("filepath");

		// Changing action bar background color
		try{
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));
		} catch(Exception e){

		}

		// Receiving the data from previous activity
		bloodText = (TextView)findViewById(R.id.bloodDisplay);
		pussText = (TextView)findViewById(R.id.pussDisplay);
		wateringText = (TextView)findViewById(R.id.wateringDisplay);
		painText = (TextView)findViewById(R.id.painDisplay);
		swellText = (TextView)findViewById(R.id.swellingDisplay);
		itchText = (TextView)findViewById(R.id.itchDisplay);
		rednessText = (TextView)findViewById(R.id.rednessDisplay);
		spotsText = (TextView)findViewById(R.id.spotsDisplay);
		scalingText = (TextView)findViewById(R.id.scalingDisplay);
		elevationText = (TextView)findViewById(R.id.elevationDisplay);

		SeekBar bloodSeekbar=(SeekBar)findViewById(R.id.blood);
		SeekBar pussSeekbar=(SeekBar)findViewById(R.id.puss);
		SeekBar wateringSeekbar=(SeekBar)findViewById(R.id.watering);
		SeekBar painSeekbar=(SeekBar)findViewById(R.id.pain);
		SeekBar swellSeekbar=(SeekBar)findViewById(R.id.swelling);
		SeekBar itchSeekbar=(SeekBar)findViewById(R.id.itch);
		SeekBar rednessSeekbar=(SeekBar)findViewById(R.id.redness);
		SeekBar spotsSeekbar=(SeekBar)findViewById(R.id.spots);
		SeekBar scalingSeekbar=(SeekBar)findViewById(R.id.scaling);
		SeekBar elevationSeekbar=(SeekBar)findViewById(R.id.elevation);

		affectedHand = false;
		affectedFeet = false;
		affectedArms = false;
		affectedForearms = false;
		affectedThighs = false;
		affectedGenitalia = false;
		affectedArmpits = false;
		affectedFingerwebs = false;
		affectedWrists = false;
		affectedNavel = false;

		complexion = "Fair";
		duration = "none";
		age = "none";
		gender = "Male";

		bloodSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				bloodText.setText(String.valueOf(arg1));
				blood = bloodText.getText().toString();
			}
		});

		pussSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				pussText.setText(String.valueOf(arg1));
				puss = pussText.getText().toString();

			}
		});

		wateringSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				wateringText.setText(String.valueOf(arg1));
				watering = wateringText.getText().toString();

			}
		});

		painSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				painText.setText(String.valueOf(arg1));
				pain = painText.getText().toString();
			}
		});

		swellSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				swellText.setText(String.valueOf(arg1));
				swelling = swellText.getText().toString();
			}
		});

		itchSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				itchText.setText(String.valueOf(arg1));
				itchiness = itchText.getText().toString();
			}
		});

		rednessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub



			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int arg1,
										  boolean fromUser) {
				// TODO Auto-generated method stub
				//setText
				rednessText.setText(String.valueOf(arg1));
				redness = rednessText.getText().toString();
			}
		});

		spotsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				spotsText.setText(String.valueOf(arg1));
				spots = spotsText.getText().toString();
			}
		});

		scalingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				scalingText.setText(String.valueOf(arg1));
				scaling = scalingText.getText().toString();

			}
		});

		elevationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

				//setText
				elevationText.setText(String.valueOf(arg1));
				elevation = elevationText.getText().toString();

			}
		});

		CheckBox cbHands = (CheckBox) findViewById(R.id.affectedHands);
		CheckBox cbFeet = (CheckBox) findViewById(R.id.affectedFeet);
		CheckBox cbArms = (CheckBox) findViewById(R.id.affectedArms);
		CheckBox cbThighs = (CheckBox) findViewById(R.id.affectedThighs);
		CheckBox cbGenitalia = (CheckBox) findViewById(R.id.affectedGenitalia);
		CheckBox cbArmpits = (CheckBox) findViewById(R.id.affectedArmpits);
		CheckBox cbFingerwebs = (CheckBox) findViewById(R.id.affectedFingerwebs);
		CheckBox cbWrists = (CheckBox) findViewById(R.id.affectedWrists);
		CheckBox cbNavel = (CheckBox) findViewById(R.id.affectedNavel);

		cbHands.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedHand = isChecked;
					}
				}
		);

		cbFeet.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedFeet = isChecked;
					}
				}
		);

		cbArms.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedArms = isChecked;
					}
				}
		);

		cbThighs.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedThighs = isChecked;
					}
				}
		);

		cbGenitalia.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedGenitalia = isChecked;
					}
				}
		);

		cbArmpits.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedArmpits = isChecked;
					}
				}
		);

		cbFingerwebs.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedFingerwebs = isChecked;
					}
				}
		);

		cbWrists.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedWrists = isChecked;
					}
				}
		);

		cbNavel.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						affectedNavel = isChecked;
					}
				}
		);

		final RadioGroup rgComplexion = (RadioGroup) findViewById(R.id.radioComplexion);
		rgComplexion.check(R.id.complexionFair);

		rgComplexion.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton rb = (RadioButton) findViewById(checkedId);
						complexion = rb.getText().toString();
					}
				}
		);

		final RadioGroup rgGender = (RadioGroup) findViewById(R.id.radioID);
		rgGender.check(R.id.genderMale);

		rgGender.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton rb = (RadioButton) findViewById(checkedId);
						gender = rb.getText().toString();
					}
				}
		);

		rgComplexion.check(R.id.complexionFair);

		rgComplexion.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton rb = (RadioButton)findViewById(checkedId);
						complexion = rb.getText().toString();
					}
				}
		);

		ScrollView parentScroll, scrollViewAffectedRegion, scrollViewComplexion;
		parentScroll = (ScrollView) findViewById(R.id.parentScroll);
		scrollViewAffectedRegion = (ScrollView) findViewById(R.id.scrollViewAffectedRegion);
		scrollViewComplexion = (ScrollView) findViewById(R.id.scrollViewComplexion);

		parentScroll.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.v(TAG,"PARENT TOUCH");
				findViewById(R.id.scrollViewAffectedRegion).getParent().requestDisallowInterceptTouchEvent(false);
				findViewById(R.id.scrollViewComplexion).getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});

		scrollViewAffectedRegion.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event)
			{
				Log.v(TAG, "CHILD TOUCH");
				// Disallow the touch request for parent scroll on touch of child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		scrollViewComplexion.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event)
			{
				Log.v(TAG,"CHILD TOUCH");
				// Disallow the touch request for parent scroll on touch of child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});


		blood = "0";
		puss = "0" ;
		watering = "0";
		pain = "0" ;
		swelling = "0" ;
		itchiness = "0" ;
		redness = "0";
		spots = "0" ;
		scaling = "0";
		elevation = "0";




		proceed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// uploading the file to server
				new UploadFileToServer().execute();
			}
		});



	}

	/**
	 * Uploading the file to server
	 * */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			//progressBar.setProgress(0);
			waitbar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

			waitbar.show();
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Making progress bar visible
			progressBar.setVisibility(View.VISIBLE);

			// updating progress bar value
			progressBar.setProgress(progress[0]);

			// updating percentage value
			txtPercentage.setText(String.valueOf(progress[0]) + "%");
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Config.FILE_URL + "takeparameters.php");

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								//publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});

				//File sourceFile = new File(filePath);

				// Adding file data to http body
				entity.addPart("session_code",new StringBody(session_code));
				entity.addPart("blood", new StringBody(blood));
				entity.addPart("puss", new StringBody(puss));
				entity.addPart("watering", new StringBody(watering));
				entity.addPart("pain", new StringBody(pain));
				entity.addPart("swell", new StringBody(swelling));
				entity.addPart("itch", new StringBody(itchiness));
				entity.addPart("redness", new StringBody(redness));
				entity.addPart("spots", new StringBody(spots));
				entity.addPart("scaling", new StringBody(scaling));
				entity.addPart("elevation", new StringBody(elevation));

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);
				Log.i("Entity", "session_code:"+session_code+
				";blood:"+blood+
				";puss:"+puss+
				";watering:"+watering+
				";pain:"+pain+
				";swell:"+swelling+
				";itch:"+itchiness+
				";redness:"+redness+
				";spot:"+spots+
				";scaling:"+scaling+
				";elevation:"+elevation);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				Log.i("Response:", response.toString());

				int statusCode = response.getStatusLine().getStatusCode();


				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode + "false" ;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString() + "false" ;
			} catch (IOException e) {
				responseString = e.toString() + "false" ;
			}


			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Response from server: " + result);

			// showing the server response in an alert dialog

			showAlert(result);

			super.onPostExecute(result);
		}

	}

	/**
	 * Method to show alert dialog
	 * */
	private void showAlert(final String message) {
		waitbar.dismiss();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		String result = new String( message.substring( message.length() - 5 , message.length() ) ) ;
		String reply = "" ;
		boolean flag = false ;
		if( !result.equals("false") ){

			flag = true;
			reply = "Your skin has been analyzed and the results are ready. Please click Ok to get your results." ;

		}
		else{

			reply = "There was a problem uploading your image please check your internet connection or retry after some time." ;

		}
		final boolean goToNextPage = flag ;

		builder.setMessage(reply).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
						if( goToNextPage ){

							String saffRegions, affRegions = "Affected regions:";
							saffRegions = affRegions;
							if(affectedArmpits){
								affRegions+="Armpits";
							}
							if(affectedArms){
								if(affRegions.equals(saffRegions)){
									affRegions+="Arms";
								} else {
									affRegions+=",Arms";
								}
							}
							if(affectedFeet){
								if(affRegions.equals(saffRegions)){
									affRegions+="Feet";
								} else {
									affRegions+=",Feet";
								}
							}
							if(affectedFingerwebs){
								if(affRegions.equals(saffRegions)){
									affRegions+="Fingerwebs";
								} else {
									affRegions+=",Fingerwebs";
								}
							}
							if(affectedForearms){
								if(affRegions.equals(saffRegions)){
									affRegions+="Fore arms";
								} else {
									affRegions+=",Fore arms";
								}
							}
							if(affectedGenitalia){
								if(affRegions.equals(saffRegions)){
									affRegions+="Genitalia";
								} else {
									affRegions+=",Genitalia";
								}
							}
							if(affectedHand){
								if(affRegions.equals(saffRegions)){
									affRegions+="Hand";
								} else {
									affRegions+=",Hand";
								}
							}
							if(affectedNavel){
								if(affRegions.equals(saffRegions)){
									affRegions+="Navel";
								} else {
									affRegions+=",Navel";
								}
							}
							if(affectedThighs){
								if(affRegions.equals(saffRegions)){
									affRegions+="Thighs";
								} else {
									affRegions+=",Thighs";
								}
							}
							if(affectedWrists){
								if(affRegions.equals(saffRegions)){
									affRegions+="Wrists";
								} else {
									affRegions+=",Wrists";
								}
							}
							if(affRegions.equals(saffRegions)){
								affRegions+="none";
							}

							EditText etDuration = (EditText) findViewById(R.id.durationText);
							duration = etDuration.getText().toString();

							EditText etAge = (EditText) findViewById(R.id.ageText);
							age = etAge.getText().toString();

							Intent j = new Intent( takeParameters.this , showresults.class ) ;
							j.putExtra("message",message) ;
							j.putExtra("filepath", filepath);
							j.putExtra("affectedRegions", affRegions);
							j.putExtra("complexion", "Complexion:"+complexion);
							j.putExtra("duration", "Duration:"+duration);
							j.putExtra("age", "Age:"+age);
							j.putExtra("gender", "Gender:"+gender);


							startActivity(j) ;
							finish();

						}
						else{

							Intent i = new Intent(takeParameters.this , MainActivity.class) ;
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity( i );
							finish();

						}

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showMessageOnThisPage() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
		String message = "Please provide some valuable details on the area affected which might allow us to identify your disease more effectively." ;
		builder.setMessage(message).setTitle("Help Box")
				.setCancelable(false)
				.setPositiveButton("Got it", new DialogInterface.OnClickListener(){

					public void onClick( DialogInterface dialog , int id ){}

				});
		AlertDialog alert = builder.create() ;
		alert.show();

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

//		Intent i = new Intent( this, MainActivity.class ) ;
//		startActivity(i);
		finish();

	}

}