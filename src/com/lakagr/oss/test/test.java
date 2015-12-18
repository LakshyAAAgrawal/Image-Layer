package com.lakagr.oss.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.Toast;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.FileOutputStream;
import android.content.Context;
import android.widget.EditText;
import java.lang.Integer;
import android.text.TextWatcher;
import android.text.Editable;

public class test extends Activity
{
    	/** Called when the activity is first created. */
    	private static final int PICK_IMAGE_REQUEST =1;
    	Uri uri1=null,uri2=null;
    	Bitmap bitmap1=null, bitmap2=null;
    	boolean img1_selected=false,img2_selected=false,changed1=false,changed2=false;
    	int weight1, weight2;
    	
    	
    	public static int hcf(int a,int b){
       while(a!=b){
       if(a>b){
           a-=b;
       }else
       if(b>a){
           b-=a;
       }else
           return a;
       }
       return a;
   }
   	@Override
    	public void onCreate(Bundle savedInstanceState)
    	{
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.main2);
        	final EditText et1=(EditText)findViewById(R.id.weight1);
    		final EditText et2=(EditText)findViewById(R.id.weight2);
       		Button btn1=(Button) findViewById(R.id.btn1);
       		btn1.setOnClickListener(new OnClickListener(){
       		 	@Override
        		public void onClick(View arg0){
        			Intent intent = new Intent();
				// Show only images, no videos or anything else
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				if(intent.resolveActivity(getPackageManager())!=null){
					// Always show the chooser (if there are multiple options available)
					startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
				}else{
					Toast.makeText(getApplicationContext(), "No app installed for choosing Image\nPlease download one",Toast.LENGTH_LONG).show();
				}
        		}
        	});
        	Button btn2=(Button)findViewById(R.id.btn2);
        	btn2.setOnClickListener(new OnClickListener(){
       		 	@Override
        		public void onClick(View arg0){
        			Intent intent = new Intent();
				// Show only images, no videos or anything else
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				if(intent.resolveActivity(getPackageManager())!=null){
					// Always show the chooser (if there are multiple options available)
					startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST+1);
				}else{
					Toast.makeText(getApplicationContext(), "No app installed for choosing Image\nPlease download one",Toast.LENGTH_LONG).show();
				}
        		}
        	});
        	
        	et1.addTextChangedListener(new TextWatcher(){
    		public void onTextChanged(CharSequence s, int start, int before, int count) {
    			if(changed1){
    				changed1=false;
    			}else{
    				if(count==0){
    					weight1=-10;
    					weight2=-10;
    					changed2=true;
    					et2.setText(et1.getText().toString());
    					
    				}else{
    					weight1=Integer.parseInt(et1.getText().toString());
    					weight2=100-weight1;
    					changed2=true;
    					et2.setText(new Integer(weight2).toString());
    					
   				}
   			}
   		}
   		public void afterTextChanged(Editable s) {}
   		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    	});
    		et2.addTextChangedListener(new TextWatcher(){
    		public void onTextChanged(CharSequence s, int start, int before, int count) {
      			if(changed2){
    				changed2=false;
    			}else{
    				if(count==0){
    					weight1=-10;
    					weight2=-10;
    					changed1=true;
    					et1.setText(et2.getText().toString());
    					
    				}else{
    					weight2=Integer.parseInt(et2.getText().toString());
    					weight1=100-weight2;
    					changed1=true;
    					et1.setText(new Integer(weight1).toString());
    					
   				}
   			}
   		}
   		public void afterTextChanged(Editable s) {}
   		public void beforeTextChanged(CharSequence s, int start,
     int count, int after) {}
    	});
        	Button btngen=(Button)findViewById(R.id.btn_gen);
        	btngen.setOnClickListener(new OnClickListener(){
        		@Override
        		public void onClick(View arg0){
        			
        		
        			
        			
        			if(!(img1_selected&img2_selected)){
        				Toast.makeText(getApplicationContext(), "Please select Both the Images",Toast.LENGTH_LONG).show();
        				return;
        			}
        			else{
        				if(bitmap1.getHeight()!=bitmap2.getHeight()||bitmap1.getWidth()!=bitmap2.getWidth()){
        				Toast.makeText(getApplicationContext(), "Both the Images should have the same dimesnsions",Toast.LENGTH_LONG).show();
        				return;
        			}
        				Bitmap out=Bitmap.createBitmap(bitmap1.getWidth(),bitmap1.getHeight(),Bitmap.Config.ARGB_8888);
        				boolean a=out.getWidth()%2==0;
        				boolean alt=true;
        				for(int w=0;w<out.getWidth();w++){
        					for(int h=0;h<out.getHeight();h++){
        						if(alt){
        							out.setPixel(w,h,bitmap1.getPixel(w,h));
        							alt=!alt;
        						}else{
        							out.setPixel(w,h,bitmap2.getPixel(w,h));
        							alt=!alt;
        						}
        					}
        					if(a)
        						alt=!alt;
        				}
        				Intent sendIntent=null;
        				try{
        					FileOutputStream stream=openFileOutput("bit-tmp.png",Context.MODE_PRIVATE);
        					out.compress(Bitmap.CompressFormat.PNG,100, stream);
        					
        					
        					stream.close();
        					out.recycle();
        					
        					sendIntent=new Intent(getApplicationContext(),display.class);
        					sendIntent.putExtra("img", "bit-tmp.png");
        					
        				}catch(Exception e){}
        				startActivity(sendIntent);
        				
        			}
        		}
        	});
    	}
    	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
			uri1 = data.getData();
			Toast.makeText(getApplicationContext(), "Image 1: \n"+uri1.toString()+" Selected",Toast.LENGTH_LONG).show();
			try{
        			bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
        		}
        		catch(Exception e){
        			Toast.makeText(getApplicationContext(), "Image 1:\nPlease choose a valid image file",Toast.LENGTH_LONG).show();
        		}
        		((ImageView)findViewById(R.id.liv)).setImageBitmap(bitmap1);
        		((ImageView)findViewById(R.id.liv)).setVisibility(View.VISIBLE);
        		((EditText)findViewById(R.id.weight1)).setVisibility(View.VISIBLE);
        		img1_selected=true;
		}else
		if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
			uri2 = data.getData();
			Toast.makeText(getApplicationContext(), "Image 2: \n"+uri2.toString()+" Selected",Toast.LENGTH_LONG).show();
			try{
        			bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
        		}catch(Exception e){
        			Toast.makeText(getApplicationContext(), "Image 2:\nPlease choose a valid image file",Toast.LENGTH_LONG).show();
        		}
        		((ImageView)findViewById(R.id.riv)).setImageBitmap(bitmap2);
        		((ImageView)findViewById(R.id.riv)).setVisibility(View.VISIBLE);
        		((EditText)findViewById(R.id.weight2)).setVisibility(View.VISIBLE);
        		img2_selected=true;
		}else{
			Toast.makeText(getApplicationContext(), "Please Choose an Image",Toast.LENGTH_LONG).show();
		}
	}
}
