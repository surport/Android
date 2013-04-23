package com.ruyicai.activity.introduce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.home.MainGroup;
import com.ruyicai.custom.gallery.FlingGallery;

public class PhotoActivity extends Activity{
	private FlingGallery mGallery;
	private String[] mLabelArray = {"1","2","3","4","5"};
	private int mLabel[] = {R.drawable.photo1,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4,R.drawable.photo5};
	private  Button imgBtn;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.introduce);
		mGallery = (FlingGallery) findViewById(R.id.introduce_activity_fling_gallery);
		initGallery();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGallery.onGalleryTouchEvent(event);
	}
	/**
	 * ≥ı ºªØª¨∂Ø
	 */
	public void initGallery() {
		mGallery.setIsGalleryCircular(false);
		mGallery.setPaddingWidth(10);
		mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, mLabelArray) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return new GalleryViewItem(getApplicationContext(), position);
			}
		});
	}
	private class GalleryViewItem extends TableLayout {

		public GalleryViewItem(Context context, int position) {
			super(context);
			if(position == mLabel.length){
				Intent in = new Intent(PhotoActivity.this,MainGroup.class);
				startActivity(in);
				PhotoActivity.this.finish();
			}else{
				LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = (LinearLayout) inflate.inflate(R.layout.introduce_photo, null);
				view.setBackgroundResource(mLabel[position]);
				if(position==mLabel.length-1){
					imgBtn = (Button) view.findViewById(R.id.introduce_img_btn);
					imgBtn.setVisibility(ImageButton.VISIBLE);
					imgBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent in = new Intent(PhotoActivity.this,MainGroup.class);
							startActivity(in);
							PhotoActivity.this.finish();
						}
					});
				}
				this.addView(view);
			}
		}
	}
}
