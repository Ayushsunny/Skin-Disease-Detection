package info.androidhive.camerafileupload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();
	
 
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
 
    private Uri fileUri; // file url to store image/video
    
    private Button btnCapturePicture, btnTakeFromGallery;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main             );
        
        ////////////////////////////////////////////////////////////
        
        
        	/*Intent tempint = new Intent(MainActivity.this, showresults.class);
            tempint.putExtra("filepath", "file path");
            tempint.putExtra("message", "psoriasis 100.0011 acne 50.51212 melanoma 95.4892121");
            startActivity(tempint);
        */
        
        
        
        
        
        
        /////////////////////////////////////////////////////////////
        
        
        
        // Changing action bar background color
        // These two lines are not needed
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));
 
        final String PREFS_NAME = "MyPrefsFile";

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		

		if (settings.getBoolean("my_first_time", true)) {
			
			Toast t = Toast.makeText(getApplicationContext(), "first time hi hai " , Toast.LENGTH_SHORT ) ;
			t.show() ;
			showMessageOnThisPage();
			
		}
		
		
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnTakeFromGallery = (Button) findViewById(R.id.btnTakeFromGallery);
 
        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // capture picture
            	
                captureImage();
            }
        });
 
        /**
         * take  image from button click event
         */
        btnTakeFromGallery.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // record video
                takeFromGallery();
            }
        });
 
        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }
 
    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
 
    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 
	        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	 
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	     // ******** code for crop image
	        //intent.putExtra("crop", "true");
//	        intent.putExtra("aspectX", 1);
//	        intent.putExtra("aspectY", 1);
//	        intent.putExtra("outputX", 500);
//	        intent.putExtra("outputY", 500);

        try {

        	intent.putExtra("return-data", true);
        	startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
        	// Do nothing for now
        }

        
        
    }
    
    /**
     * Launching camera app to record video
     */
    private void takeFromGallery() {
    	
    	 Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    	 fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
    	 
	     i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	     // ******** code for crop image
	     i.putExtra("crop", "true");
	     i.putExtra("aspectX", 1);
	     i.putExtra("aspectY", 1);
	     i.putExtra("outputX", 500);
	     i.putExtra("outputY", 500);

     try {

     	 i.putExtra("return-data", true);

         startActivityForResult(i, RESULT_LOAD_IMAGE);
         
     } catch (ActivityNotFoundException e) {
     	// Do nothing for now
     }

         
        
    }
 
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
 
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }
 
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
 
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
 
    
 
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                
            	// successfully captured the image
                // launching upload activity
            	performCrop(fileUri);
            	//launchUploadActivity(true);
            	
            	
            } else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        
        }else if( resultCode == 1 ){
        		
        		if( data != null ){
        			
        			launchUploadActivity(true);
        			
        		}
        		       
        }
        else if (requestCode == RESULT_LOAD_IMAGE ) {
            if (resultCode == RESULT_OK) {
                
            	// video successfully recorded
                // launching upload activity
            	/*Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                System.out.println(picturePath);
                */
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
            	i.putExtra("filePath", fileUri.getPath() );
            	i.putExtra("isImage", true);
            	startActivity(i);
            	
            
            } else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    
    private void launchUploadActivity(boolean isImage){
    	Intent i = new Intent(MainActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }
     
    /**
     * ------------ Helper Methods ---------------------- 
     * */
 
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
 
    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 1);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    
    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
 
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);
 
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
 
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
 
        return mediaFile;
    }
    
    
    private void showMessageOnThisPage() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
		String message = "In this page you should provide the effected part of your skin by either clicking a image or choosing it from the gallery." ;
		builder.setMessage(message).setTitle("Help Box")
				.setCancelable(false)
				.setPositiveButton("Got it", new DialogInterface.OnClickListener(){

					public void onClick( DialogInterface dialog , int id ){}
						
				});
		AlertDialog alert = builder.create() ;
		alert.show();
				
	}
	

    
}
