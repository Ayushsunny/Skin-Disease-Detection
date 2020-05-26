package info.androidhive.camerafileupload;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class showresults extends Activity {
	
	String filepath ; 
	private ImageView imgPreview;
	LinearLayout percentbar , dmain ;
	String disease[] ;
	String probability_of_disease[] ;
	boolean flag = false ;
	
	/** Called when the activity is first created. */
	public int dpToPx(float indp){
		
		float d = getApplicationContext().getResources().getDisplayMetrics().density;
		return (int)(indp * d);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.testresult);
		imgPreview = (ImageView) findViewById(R.id.imgp);
		
		Intent j = getIntent() ;
		String message = j.getStringExtra("message") ;
		filepath = j.getStringExtra("filepath");
		String affectedRegions = j.getStringExtra("affectedRegions");
		String complexion = j.getStringExtra("complexion");
		String duration = j.getStringExtra("duration");
		String age = j.getStringExtra("age");
		String gender = j.getStringExtra("gender");
		System.out.println(filepath);
		if( filepath != null ){
			
			imgPreview.setVisibility(View.VISIBLE);
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// down sizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);

			imgPreview.setImageBitmap(bitmap);
			
		}
		
		String s[] = message.split(" ") ;
		disease = new String[ s.length/2 ] ;
		probability_of_disease = new String[ s.length/2 ] ;
		System.out.print(probability_of_disease);
		for(int i = 0 ; i<s.length ; i++){
			
			disease[ i/2 ] = s[i] ;
			i++ ;
			probability_of_disease[i/2] = "" + s[i].substring(0,5) ; //+"%" ;			
			
		}
		
		
		
		dmain = (LinearLayout) findViewById( R.id.dmain );
			
		
		//for( int i = 0 ; i<disease.length ; i++){
		for( int i = 0 ; i<1 ; i++){
			LinearLayout sddmain = new LinearLayout(this);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dpToPx(75) );
			llp.setMargins(0, dpToPx(40), 0, 0); // llp.setMargins(left, top, right, bottom);
		    sddmain.setLayoutParams(llp);
		    sddmain.setPadding(dpToPx(10), dpToPx(10), dpToPx(10),dpToPx(10) );
		    sddmain.setOrientation(LinearLayout.HORIZONTAL);
			sddmain.setBackgroundColor(Color.parseColor("#e2e2e2"));
			sddmain.setGravity(Gravity.CENTER_VERTICAL);	
			dmain.addView( sddmain );
			
			TextView dname = new TextView(this);
			dname.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			dname.setGravity(Gravity.LEFT);
			dname.setTextColor(Color.parseColor("#000000"));
			dname.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);			
			dname.setText(disease[ i ]);		
			sddmain.addView(dname);
			
			TextView dprob = new TextView(this);
			dprob.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			dprob.setGravity(Gravity.RIGHT);
			dprob.setTextSize(TypedValue.COMPLEX_UNIT_PX,50);
			dprob.setText(probability_of_disease[ i ] + "%");		
			sddmain.addView(dprob);	
			
			percentbar = new LinearLayout(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(30) );
			llp.setMargins(0 ,100,0, 0); // llp.setMargins(left, top, right, bottom);
		    percentbar.setLayoutParams(lp);
		    percentbar.setOrientation(LinearLayout.HORIZONTAL);
		    percentbar.setBackgroundColor(Color.parseColor("#888888"));
		    percentbar.setGravity(Gravity.CENTER_VERTICAL);	
			percentbar.setId(51 + i);
			dmain.addView(percentbar);
			
			
			TextView ddesc = new TextView(this);
			ddesc.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			ddesc.setGravity(Gravity.LEFT);
			ddesc.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
			ddesc.setBackgroundColor(Color.parseColor("#f5f5f5"));
			ddesc.setTextSize(TypedValue.COMPLEX_UNIT_PX,20);
			
//			if( disease[i].equalsIgnoreCase("Psoriasis") ){
//
//				ddesc.setText("Psoriasis is a common skin condition that changes the life cycle of skin cells. Psoriasis causes cells to build up rapidly on the surface of the skin. The extra skin cells form thick, silvery scales and itchy, dry, red patches that are sometimes painful.\n\nPsoriasis is a persistent, long-lasting (chronic) disease. There may be times when your psoriasis symptoms get better alternating with times your psoriasis worsens.\n\nThe primary goal of treatment is to stop the skin cells from growing so quickly. While there isn't a cure, psoriasis treatments may offer significant relief. Lifestyle measures, such as using a nonprescription cortisone cream and exposing your skin to small amounts of natural sunlight, also may improve your psoriasis symptoms.");
//
//			}
//			else if( disease[i].equalsIgnoreCase("Furr  uncle") ){
//
//				ddesc.setText("Describes furruncle, one of  the type of skin disease");
//
//			}else if( disease[i].equalsIgnoreCase("eczema") ){
//
//				ddesc.setText("Eczema is inflammation of the skin. It is characterized by itchy, erythematous, vesicular, weeping, and crusting patches. The term eczema is broadly applied to a range of persistent skin conditions. These include dryness and recurring skin rashes that are characterized by one or more of these symptoms: redness, skin swelling, itching and dryness, crusting, flaking, blistering, cracking, oozing, or bleeding.\n\nAreas of temporary skin discoloration may appear and are sometimes due to healed injuries. Scratching open a healing lesion may result in scarring and may enlarge the rash.\nTreatment is typically with moisturizers and steroid creams. If these are not effective, creams based on calcineurin inhibitors may be used. The disease was estimated as of 2010 to affect 230 million people globally (3.5% of the population).\n\nWhile eczema is not life-threatening, a number of other illnesses have been linked to the condition, including osteoporosis, depression, and heart disease.");
//
//			}else if( disease[i].equalsIgnoreCase("ringworms") ){
//
//				ddesc.setText("Dermatophytosis, more commonly known as as ringworm, is a fungal infection of the skin. Ringworm is a misnomer. The infection is not caused by a worm at all, but by a fungus.\n\nThe infection can affect both humans and animals. The fungi that are known to cause dermatophytosis may be present in soil. The infection initially presents itself with red patches on affected areas of the skin and later spreads to other parts of the body.\n\nThe infection may affect the skin of the scalp, feet, groin, beard, or other areas. Anyone may develop ringworm, regardless of age. However, the infection is very common among children and people who own cats. You may be more likely to develop dermatophytosis if you are wet or if you have minor skin injuries or abrasions.");
//
//			}else{
//
//				ddesc.setText("Scabies is an extremely itchy skin disorder that can be passed from one person to another. It is caused by an infestation with a parasite - the scabies mite. The mite lives on the skin and burrows into it.\n\nScabies is spread to others through close skin-to-skin contact.\nIt is easily treated with an insecticide lotion applied to the skin. Two applications of treatment are needed, one week apart.");
//
//			}

			ddesc.setText("Eczema is inflammation of the skin. It is characterized by itchy, erythematous, vesicular, weeping, and crusting patches. The term eczema is broadly applied to a range of persistent skin conditions. These include dryness and recurring skin rashes that are characterized by one or more of these symptoms: redness, skin swelling, itching and dryness, crusting, flaking, blistering, cracking, oozing, or bleeding.\n\nAreas of temporary skin discoloration may appear and are sometimes due to healed injuries. Scratching open a healing lesion may result in scarring and may enlarge the rash.\nTreatment is typically with moisturizers and steroid creams. If these are not effective, creams based on calcineurin inhibitors may be used. The disease was estimated as of 2010 to affect 230 million people globally (3.5% of the population).\n\nWhile eczema is not life-threatening, a number of other illnesses have been linked to the condition, including osteoporosis, depression, and heart disease.");
			
				
			dmain.addView(ddesc);	
			
		}

		//Text Report:
		LinearLayout sddmain = new LinearLayout(this);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dpToPx(75) );
		llp.setMargins(0, dpToPx(40), 0, 0); // llp.setMargins(left, top, right, bottom);
		sddmain.setLayoutParams(llp);
		sddmain.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
		sddmain.setOrientation(LinearLayout.HORIZONTAL);
		sddmain.setBackgroundColor(Color.parseColor("#e2e2e2"));
		sddmain.setGravity(Gravity.CENTER_VERTICAL);
		dmain.addView(sddmain);

		TextView dname = new TextView(this);
		dname.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dname.setGravity(Gravity.LEFT);
		dname.setTextColor(Color.parseColor("#000000"));
		dname.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
		dname.setText("REPORT");
		sddmain.addView(dname);

		percentbar = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(30) );
		llp.setMargins(0 ,100,0, 0); // llp.setMargins(left, top, right, bottom);
		percentbar.setLayoutParams(lp);
		percentbar.setOrientation(LinearLayout.HORIZONTAL);
		percentbar.setBackgroundColor(Color.parseColor("#888888"));
		percentbar.setGravity(Gravity.CENTER_VERTICAL);
		percentbar.setId(51 + disease.length);
		dmain.addView(percentbar);


		TextView ddesc = new TextView(this);
		ddesc.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		ddesc.setGravity(Gravity.LEFT);
		ddesc.setPadding(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
		ddesc.setBackgroundColor(Color.parseColor("#f5f5f5"));
		ddesc.setTextSize(TypedValue.COMPLEX_UNIT_PX,20);

		ddesc.setText(affectedRegions+"\n"+complexion+"\n"+duration+"\n"+age+"\n"+gender);

		dmain.addView(ddesc);
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if( !flag ){
		
			for( int i = 0 ; i< probability_of_disease.length ; i++ ){
			
				percentbar = (LinearLayout) findViewById(51+i) ;		
				View view = new View(this);
				view.setBackgroundColor(Color.parseColor("#00FF60"));
				System.out.println(percentbar.getWidth() + "--->P " + (int)((Float.parseFloat( probability_of_disease[ i ] )/100)*( percentbar.getWidth() - dpToPx( 0 ) ) ) );
				view.setLayoutParams(new LayoutParams( (int)((Float.parseFloat( probability_of_disease[ i ] )/100)*( percentbar.getWidth() - dpToPx( 0 ) ) ) , LayoutParams.MATCH_PARENT ));
				percentbar.addView(view);
				
			}
			flag = true ; 
			
		}
	
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
}
