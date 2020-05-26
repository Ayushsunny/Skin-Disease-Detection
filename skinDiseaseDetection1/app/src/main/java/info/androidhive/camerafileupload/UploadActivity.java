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
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class UploadActivity extends Activity {
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private ProgressBar progressBar;
	private ProgressDialog waitbar ;

	private String filePath = null;
	private TextView txtPercentage;
	private ImageView imgPreview;
	private VideoView vidPreview;
	private Button btnUpload;
	long totalSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		waitbar = new ProgressDialog(this);
		txtPercentage = (TextView) findViewById(R.id.txtPercentage);
		btnUpload = (Button) findViewById(R.id.btnUpload);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		vidPreview = (VideoView) findViewById(R.id.videoPreview);
		
		
		final String PREFS_NAME = "MyPrefsFile";

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		

		if (settings.getBoolean("my_first_time", true)) {
		           
		   
			Toast t = Toast.makeText(getApplicationContext(), "first time hi hai " , Toast.LENGTH_SHORT ) ;
			t.show() ;
			showMessageOnThisPage() ; 
			
		    //settings.edit().putBoolean("my_first_time", false).commit(); 
		}
		
		
		
		waitbar.setCancelable(true);
		waitbar.setProgressStyle(ProgressDialog.STYLE_SPINNER) ; 
		waitbar.setMessage("Work in Progress ...");
		waitbar.setCanceledOnTouchOutside(false);

		// Changing action bar background color
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor(getResources().getString(
						R.color.action_bar))));

		// Receiving the data from previous activity
		Intent i = getIntent();

		// image or video path that is captured in previous activity
		filePath = i.getStringExtra("filePath");

		// boolean flag to identify the media type, image or video
		boolean isImage = i.getBooleanExtra("isImage", true);

		if (filePath != null) {
			// Displaying the image or video on the screen
			previewMedia(isImage);
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
		}

		btnUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// uploading the file to server
				new UploadFileToServer().execute();
				
			}
		});

	}

	

	/**
	 * Displaying captured image/video on the screen
	 * */
	private void previewMedia(boolean isImage) {
		// Checking whether captured media is image or video
		if (isImage) {
			imgPreview.setVisibility(View.VISIBLE);
			vidPreview.setVisibility(View.GONE);
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// down sizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			imgPreview.setImageBitmap(bitmap);
		} else {
			imgPreview.setVisibility(View.GONE);
			vidPreview.setVisibility(View.VISIBLE);
			vidPreview.setVideoPath(filePath);
			// start playing
			vidPreview.start();
		}
	}

	/**
	 * Uploading the file to server
	 * */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			progressBar.setProgress(0);
			/** Show the progress dialog window */
			WindowManager.LayoutParams lp = waitbar.getWindow().getAttributes();
			lp.dimAmount = 0f ;
			waitbar.getWindow().setAttributes(lp);
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
			HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});

				File sourceFile = new File(filePath);

				// Adding file data to http body
				entity.addPart("image", new FileBody(sourceFile));

				// Extra parameters if you want to pass to server
				
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode + "false" ;;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString() + "false" ;
			} catch (IOException e) {
				responseString = e.toString() + "false";
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
		boolean flag = false; 
		int len = message.length() ; 
		final String check_for_error = new String( message.substring( len - 5 , len ) ) ;
		
		String session_code_dup = "" ;
		String ack_message = "";
		if( !check_for_error.equals("false") ){
			
			session_code_dup = new String( message.substring(len - 8 , len ) ) ;
			ack_message = new String( message.substring( len-13-82 , len - 13 ) ) ;
			flag = true ;
		
		}
		else{
			
			session_code_dup = "00000000" ;
			ack_message = new String( "There was a problem uploading your image please check your internet connection or retry after some time." ) ;
			
			
		}
		final String session_code = session_code_dup ;
		final boolean goToNextPage = flag ;
		
		builder.setMessage(ack_message).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
						if( goToNextPage ){
						
							Intent i = new Intent( UploadActivity.this , takeParameters.class ) ;
							i.putExtra("session_code", session_code );
							i.putExtra("filepath", filePath);
							startActivity( i ) ; 
							finish();
							
						}
						else{
							
								Intent i = new Intent(UploadActivity.this , MainActivity.class) ;
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
		String message = "Now its time you provide/upload that image to us make sure you have properly crop the effected area." ;
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
//		// TODO Auto-generated method stub
//		Intent i = new Intent(UploadActivity.this , MainActivity.class) ;
//		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity( i );
		finish();
		
	}

}