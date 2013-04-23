package com.ruyicai.activity.introduce;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.home.MainGroup;
import com.umeng.analytics.MobclickAgent;
public class PhotoActivity extends Activity{
	private int mLabel[] = {R.drawable.photo1,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4,R.drawable.photo5};
	private  Button imgBtn;
		// hesiming 20120709 begin for replace FlingGallery with ViewPager.
		// private FlingGallery mGallery;
		// 其中ViewPager为多页显示控件，PagerTitleStrip用于显示当前页面的标题
		private ViewPager viewPagerContainer;
		// 缓存需要左右滑动的视图群的列表容器
		private List<View> viewsBufList;
		// hesiming 20120709 end
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.introduce);
		// hesiming 20120709 begin for replace FlingGallery with ViewPager.
		//mGallery = (FlingGallery)
		// findViewById(R.id.buy_activity_fling_gallery);
		viewPagerContainer = (ViewPager) findViewById(R.id.viewpager);
		// hesiming 20120709 end
		initGallery();
	}
	// hesiming 20120709 begin for replace FlingGallery with ViewPager.
		// @Override
		// public boolean onTouchEvent(MotionEvent event) {
		// return mGallery.onGalleryTouchEvent(event);
		// }
		// hesiming 20120709 end
	/**
	 * 初始化滑动
	 */
	public void initGallery() {
		/*
		 hesiming 20120708 begin for replace FlingGallery with ViewPager.
		 mGallery.setIsGalleryCircular(false);
		 mGallery.setPaddingWidth(10);
		mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, mLabelArray) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return new GalleryViewItem(getApplicationContext(), position);
			}
		});
		 */
				// 添加需要左右划屏效果的视图到缓存容器中
				viewsBufList = new ArrayList<View>();
				viewsBufList.add(new GalleryViewItem(getApplicationContext(), 0));
				viewsBufList.add(new GalleryViewItem(getApplicationContext(), 1));
				viewsBufList.add(new GalleryViewItem(getApplicationContext(), 2));
				viewsBufList.add(new GalleryViewItem(getApplicationContext(), 3));
				viewsBufList.add(new GalleryViewItem(getApplicationContext(), 4));
				// 设置 ViewPager 的 Adapter
				viewPagerContainer.setAdapter(new MainViewPagerAdapter());
				// 设置第一显示页面是 第2个View
				viewPagerContainer.setCurrentItem(0);
				// hesiming 20120709 end
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
							MobclickAgent.onEvent(PhotoActivity.this, "jihuo");//BY贺思明 2012-6-29
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
	// hesiming 20120708 begin for replace FlingGallery with ViewPager.
		private class MainViewPagerAdapter extends PagerAdapter {

			public MainViewPagerAdapter() {

			}

			@Override
			public int getCount() {

				// Log.d(TAG, "PagerAdapter:getCount");
				/*
				 * 返回提供给ViewPager的视图总数. 一般我们会把View群先插入一个List<View>中缓存,
				 * 然后在这里就返回这个List<View>.size()即可.
				 */
				return viewsBufList.size();
			}

			@Override
			public void startUpdate(ViewGroup container) {
				// Log.d(TAG, "PagerAdapter:startUpdate");
			}
			
			@Override
			public int getItemPosition(Object object) {
				// Log.d(TAG, "PagerAdapter:getItemPosition");
				return POSITION_UNCHANGED;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				// Log.d(TAG, "PagerAdapter:destroyItem");
				container.removeView(viewsBufList.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				// Log.d(TAG, "PagerAdapter:getPageTitle");
				// 设置每个tab view的标题
				// return viewsTitleBufList.get(position);
				return null;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// Log.d(TAG, "PagerAdapter:instantiateItem");
				container.addView(viewsBufList.get(position), 0);
				return viewsBufList.get(position);
			}

			@Override
			public void finishUpdate(ViewGroup container) {
				// Log.d(TAG, "PagerAdapter:finishUpdate");
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// Log.d(TAG, "PagerAdapter:isViewFromObject");
				return arg0 == (arg1);

			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// Log.d(TAG, "PagerAdapter:restoreState");
			}

			@Override
			public Parcelable saveState() {
				// Log.d(TAG, "PagerAdapter:saveState");
				return null;

			}

			@Override
			public void notifyDataSetChanged() {
				// Log.d(TAG, "PagerAdapter:notifyDataSetChanged");
			}

			@Override
			public void setPrimaryItem(ViewGroup container, int position,
					Object object) {
				// Log.d(TAG, "PagerAdapter:setPrimaryItem");
			}
		}
		// hesiming 20120708 end
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onResume(this);//BY贺思明 2012-7-23
		}
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPause(this);//BY贺思明 2012-7-23
		};
		
}
