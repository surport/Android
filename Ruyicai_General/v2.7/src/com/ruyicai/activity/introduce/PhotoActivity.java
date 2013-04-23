package com.ruyicai.activity.introduce;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivity;
import com.ruyicai.activity.home.HomeActivity;
import com.ruyicai.activity.home.MainGroup;
import com.ruyicai.util.Constants;
import com.ruyicai.util.FlingGallery;

public class PhotoActivity extends Activity{
	private FlingGallery mGallery;
	private String[] mLabelArray = {"1","2","3"};
	private int mLabel[] = {R.drawable.photo1,R.drawable.photo2,R.drawable.photo3};
	private ImageButton imgBtn;
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
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = (LinearLayout) inflate.inflate(R.layout.introduce_photo, null);
			view.setBackgroundResource(mLabel[position]);
			if(position==mLabel.length-1){
				imgBtn = (ImageButton) view.findViewById(R.id.introduce_img_btn);
				imgBtn.setVisibility(ImageButton.VISIBLE);
				imgBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent in = new Intent(PhotoActivity.this,MainGroup.class);
						in.putExtra(Constants.NOTIFICATION_MARKS, HomeActivity.notificationMark);
						startActivity(in);
						PhotoActivity.this.finish();
					}
				});
			}
			this.addView(view);
		}
	}
}
