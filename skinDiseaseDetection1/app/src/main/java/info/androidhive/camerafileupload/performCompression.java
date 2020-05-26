package info.androidhive.camerafileupload;

import info.androidhive.camerafileupload.R.string;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Toast;

public class performCompression extends Activity {

	final int PIC_CROP = 1;
	Uri fileuri ;
	String filepath ;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        Intent i = getIntent() ;
        
        fileuri = i.getParcelableExtra("filepath");
        filepath = fileuri.getPath() ;
       
        
        performCrop( fileuri );

	}
	private void performCrop(Uri picUri) {
	    try {

	        Intent cropIntent = new Intent("com.android.camera.action.CROP");
	        // indicate image type and Uri
	        cropIntent.setDataAndType(picUri, "image/*");
	        // set crop properties
	        cropIntent.putExtra("crop", "true");
	        // indicate aspect of desired crop
	        cropIntent.putExtra("aspectX", 1);
	        cropIntent.putExtra("aspectY", 1);
	        // indicate output X and Y
	        cropIntent.putExtra("outputX", 128);
	        cropIntent.putExtra("outputY", 128);
	        // retrieve data on return
	        cropIntent.putExtra("return-data", true);
	        // start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, PIC_CROP);
	    }
	    // respond to users whose devices do not support the crop action
	    catch (ActivityNotFoundException anfe) {
	        // display an error message
	        String errorMessage = "Whoops - your device doesn't support the crop action!";
	        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
	        toast.show();
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == PIC_CROP) {
	        if (data != null) {
	            // get the returned data
	            Bundle extras = data.getExtras();
	            // get the cropped bitmap
	            Bitmap selectedBitmap = extras.getParcelable("data");
	            storeImage(selectedBitmap);
	            
	            Intent i = new Intent(performCompression.this, UploadActivity.class);
	            i.putExtra("filePath", fileuri.getPath());
	            i.putExtra("isImage", true);
	            startActivity(i);
	            
	        }
	    }

	}
	
	private void storeImage(Bitmap image) {
	    
	    try {
	        FileOutputStream fos = new FileOutputStream(filepath);
	        image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
	        fos.close();
	    } catch (FileNotFoundException e) {
	        
	    } catch (IOException e) {
	       
	    }  
	}
	
	
}
