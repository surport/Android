package com.ruyicai.activity.buy.nmk3;

import java.util.Vector;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.zixuan.JiXuanBtn;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.RuyicaiActivityManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;

/**
 * @author 秘青强
 * 
 *         2013.10.29 骰子移动动画
 */
public class NmkAnimation {

	private ImageView nmk_ShaiZi1;
	private ImageView nmk_ShaiZi2;
	private ImageView nmk_ShaiZi3;
	private ImageView nmk_MoveToView1,nmk_MoveToView2,nmk_MoveToView3;
	private ImageView nmk_ShaiZiHuaLan;
	private MediaPlayer mediaPlayer;
	private AnimationDrawable anim1, anim2, anim3; // 骰子滚动动画
	private int count; // 骰子滚动次数计数器
	private int point1, point2, point3; // 随机骰子点数
	private Activity activity;
	public static boolean flag = true;
	private JiXuanBtn jixuanbtn;
	private int i;
	private int[] iHighlightBallId;

	/**
	 * @param activity当前显示的Activity
	 * @param view1第一个骰子
	 * @param view2第二个骰子
	 * @param view3第三个骰子
	 * @param view4第一个骰子需要移动到的view
	 * @param view5第二个骰子需要移动到的view
	 * @param view6第三个骰子需要移动到的view
	 * @param view7骰子花篮
	 */
	public NmkAnimation(Activity activity, ImageView view1, ImageView view2,
			ImageView view3, ImageView view4, ImageView view5, ImageView view6,
			ImageView view7) {
		this.nmk_ShaiZi1=view1;
		this.nmk_ShaiZi2=view2;
		this.nmk_ShaiZi3=view3;
		this.nmk_MoveToView1=view4;
		this.nmk_MoveToView2=view5;
		this.nmk_MoveToView3=view6;
		this.nmk_ShaiZiHuaLan=view7;
		this.activity = activity;
		if(flag){
			flag=false;
		    initAnimation();
		}
	}
	
	/**
	 * @param jiXuanBtn 
	 * @param activity当前显示的Activity
	 * @param view1第一个骰子
	 * @param view2第二个骰子
	 * @param view3第三个骰子
	 * @param ballViewVector 需要移动到的view集合
	 * @param view7骰子花篮
	 */
	public NmkAnimation(Activity activity, JiXuanBtn jiXuanBtn, ImageView view1, ImageView view2,
			ImageView view3, Vector<OneBallView> ballViewVector,
			ImageView view4) {
		this.nmk_ShaiZi1=view1;
		this.nmk_ShaiZi2=view2;
		this.nmk_ShaiZi3=view3;
		this.jixuanbtn = jiXuanBtn;
		if(ballViewVector.size()==1){
			this.nmk_MoveToView1=ballViewVector.elementAt(0);
			this.nmk_MoveToView2=ballViewVector.elementAt(0);
			this.nmk_MoveToView3=ballViewVector.elementAt(0);
		}
		if(ballViewVector.size()==2){
			this.nmk_MoveToView1=ballViewVector.elementAt(0);
			this.nmk_MoveToView2=ballViewVector.elementAt(1);
			this.nmk_MoveToView3=ballViewVector.elementAt(1);
		}
		if(ballViewVector.size()==3){
			this.nmk_MoveToView1=ballViewVector.elementAt(0);
			this.nmk_MoveToView2=ballViewVector.elementAt(1);
			this.nmk_MoveToView3=ballViewVector.elementAt(2);
		}
		
		this.nmk_ShaiZiHuaLan=view4;
		this.activity = activity;
		((ZixuanAndJiXuan)activity).toTrans = true;
		Intent intent=new Intent(this.activity,TransParentActivity.class);
		activity.startActivityForResult(intent,1);
		
		if(flag){
			flag=false;
		    initAnimation();
		}
	}
	
	public NmkAnimation(Activity activity, JiXuanBtn jiXuanBtn, ImageView view1, ImageView view2,
			ImageView view3, Vector<OneBallView> ballViewVector,
			ImageView view4,int i,int[] iHighlightBallId) {
		this.nmk_ShaiZi1=view1;
		this.nmk_ShaiZi2=view2;
		this.nmk_ShaiZi3=view3;
		this.i = i;
		this.iHighlightBallId = iHighlightBallId;
		this.jixuanbtn = jiXuanBtn;
		if(ballViewVector.size()==1){
			this.nmk_MoveToView1=ballViewVector.elementAt(0);
			this.nmk_MoveToView2=ballViewVector.elementAt(0);
			this.nmk_MoveToView3=ballViewVector.elementAt(0);
		}
		if(ballViewVector.size()==2){
			if(((ZixuanAndJiXuan)activity).highttype.equals("NMK3-TWOSAME-DAN")){
				this.nmk_MoveToView1=ballViewVector.elementAt(0);
				this.nmk_MoveToView2=ballViewVector.elementAt(0);
				this.nmk_MoveToView3=ballViewVector.elementAt(1);
			}else{
				this.nmk_MoveToView1=ballViewVector.elementAt(0);
				this.nmk_MoveToView2=ballViewVector.elementAt(1);
				this.nmk_MoveToView3=ballViewVector.elementAt(1);
			}
		
		}
		if(ballViewVector.size()==3){
			this.nmk_MoveToView1=ballViewVector.elementAt(0);
			this.nmk_MoveToView2=ballViewVector.elementAt(1);
			this.nmk_MoveToView3=ballViewVector.elementAt(2);
		}
		
		this.nmk_ShaiZiHuaLan=view4;
		this.activity = activity;
		((ZixuanAndJiXuan)activity).toTrans = true;
		Intent intent=new Intent(this.activity,TransParentActivity.class);
		activity.startActivityForResult(intent,1);
		
		if(flag){
			flag=false;
		    initAnimation();
		}
	}

	/**
	 * 初始化空间和音乐资源
	 * 
	 * 骰子游戏开始
	 */
	private void initAnimation() {
		// 加载声音资源
		mediaPlayer = MediaPlayer.create(activity, R.raw.yaoshaizimusic);
		mediaPlayer.setLooping(true);
		// 计数器清零
		count = 0;
		// 加载帧动画
		creatAnimation();
		// 加载声音
		mediaPlay();
		// 骰子花篮抖动
		basketShake();
	}

	/**
	 * 骰子花篮抖动
	 */
	private void basketShake() {
		// nmk_ShaiZiHuaLan.setBackgroundResource(R.drawable.shaizihualan);
		Animation shake = AnimationUtils.loadAnimation(activity,
				R.anim.hualanshake);
		shake.reset();
		shake.setFillAfter(false);
		nmk_ShaiZiHuaLan.startAnimation(shake);
		shake.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				nmk_ShaiZiHuaLan.clearAnimation();
				nmk_ShaiZiHuaLan.setBackgroundColor(Color.TRANSPARENT);

				enlargeAnimation();
			}
		});
	}

	/**
	 * 骰子原地滚动动画
	 */
	private void creatAnimation() {
		anim1 = (AnimationDrawable) activity.getApplication().getResources()
				.getDrawable(R.drawable.frame);
		anim2 = (AnimationDrawable) activity.getApplication().getResources()
				.getDrawable(R.drawable.frame);
		anim3 = (AnimationDrawable) activity.getApplication().getResources()
				.getDrawable(R.drawable.frame);
	}

	/**
	 * 开启声音
	 */
	private void mediaPlay() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 骰子移动动画
	 * 
	 * @param p1
	 *            动画开始的点离当前View X坐标上的差值；
	 * @param p2
	 *            动画结束的点离当前View X坐标上的差值；
	 * @param p3
	 *            动画开始的点离当前View Y坐标上的差值；
	 * @param p4
	 *            动画开始的点离当前View Y坐标上的差值；
	 * @param view
	 *            需要移动的ImageView
	 */
	private void transView(final float p1, final float p2, final float p3,
			final float p4, final ImageView view) {
		// TranslateAnimation a=new TranslateAnimation(count, p4, count, p4,
		// count, p4, count, p4);
		TranslateAnimation animation = new TranslateAnimation(p1, p2, p3, p4);
		// 动画速率
		// animation.setInterpolator(new OvershootInterpolator());
		// 动画执行时间
		animation.setDuration(500);
		// 动画执行次数
		// animation.setStartOffset(1000);
		// 加载动画
		view.startAnimation(animation);
		// 动画监听
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int left = view.getLeft() + (int) (p2 - p1);
				int top = view.getTop() + (int) (p4 - p3);
				int width = view.getWidth();
				int height = view.getHeight();
				view.clearAnimation();
				view.layout(left, top, left + width, top + height);
				count++; // 计数器的功能保证骰子滚动一个来回
				int num;
				if(isTwoDiffActivity()){
					num=2;
				}else{
					num=3;
				}
				if (count <= num) {
					transView(-p1, -p2, -p3, -p4, view);
				} else {
					randomImageViewBG();
				}
			}
		});
	}

	/**
	 * 结束移动动画并随机出一个ImageView背景图片，缩小结束
	 */
	private void randomImageViewBG() {
		// anim1.stop();
		nmk_ShaiZi1.clearAnimation();
		// anim2.stop();
		nmk_ShaiZi2.clearAnimation();
		// anim3.stop();
		nmk_ShaiZi3.clearAnimation();
		int[] array = new int[] { R.drawable.p1, R.drawable.p2, R.drawable.p3,
				R.drawable.p4 };
		point1 = (int) (Math.random() * 4);
		point2 = (int) (Math.random() * 4);
		point3 = (int) (Math.random() * 4);
		nmk_ShaiZi1.setBackgroundResource(array[point1]);
		nmk_ShaiZi2.setBackgroundResource(array[point2]);
		if(!isTwoDiffActivity()){
			nmk_ShaiZi3.setBackgroundResource(array[point3]);
		}
		scaleAnimation();
		
	}

	/**
	 * 动画缩小移动组合动画
	 * 
	 * @param view
	 *            当前动画所处的ImageView
	 */
	private void reduceScaleAnimation(final ImageView view1,ImageView view2) {
		// 缩放动画
		Animation scaleAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f);
		scaleAnimation.setDuration(1000);
		// 移动动画

		int[] location = new int[2];
		view2.getLocationOnScreen(location);
		int[] test = new int[2];
		view1.getLocationOnScreen(test);
		Animation anim = new TranslateAnimation(0, -test[0] + location[0]
				+ view2.getWidth() / 2, 0, -test[1] + location[1]
				+ view2.getHeight() / 2);
		anim.setDuration(1000);
		// 组合动画
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scaleAnimation);
		set.addAnimation(anim);
		view1.startAnimation(set);

		scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				flag = true;
				nmk_ShaiZi1.setBackgroundColor(Color.TRANSPARENT);
				nmk_ShaiZi2.setBackgroundColor(Color.TRANSPARENT);
				nmk_ShaiZi3.setBackgroundColor(Color.TRANSPARENT);
				closeMediaPlayer();
				if(view1 == nmk_ShaiZi1){
					if (activity instanceof Nmk3TwoSameActivty
							&& ((Nmk3TwoSameActivty) activity).highttype
									.equals("NMK3-TWOSAME-DAN")) {
						activity.finishActivity(1);
						((BaseActivity) activity).setAllBall(0,
								iHighlightBallId);
						((BaseActivity) activity).setAllBall(1,
								iHighlightBallId);
						((ZixuanAndJiXuan) activity).toTrans = false;
					} else {
						activity.finishActivity(1);
						jixuanbtn.setSelectBall();
						((ZixuanAndJiXuan) activity).toTrans = false;
					}

				}
			
			}
		});
	}
	
	public void closeMediaPlayer(){
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	
	/**
	 * 骰子缩小移动后结束
	 */
	private void scaleAnimation() {
		// 缩放动画
		reduceScaleAnimation(nmk_ShaiZi1,nmk_MoveToView1);
		reduceScaleAnimation(nmk_ShaiZi2,nmk_MoveToView2);
		reduceScaleAnimation(nmk_ShaiZi3,nmk_MoveToView3);
	}

	/**
	 * 骰子放大飞出组合动画
	 * 
	 * @param view
	 *            需要添加动画的ImageView
	 */
	private void enlargeSclaeAnimation(ImageView view) {
		view.setBackgroundResource(R.drawable.s6);
		Animation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
		TranslateAnimation animation = new TranslateAnimation(
				(nmk_ShaiZiHuaLan.getLeft() - view.getLeft() + nmk_ShaiZiHuaLan
						.getWidth() / 2),
				0,
				(nmk_ShaiZiHuaLan.getTop() - view.getTop() + nmk_ShaiZiHuaLan
						.getHeight() / 2), 0);
		scaleAnimation.setDuration(500);
		animation.setDuration(500);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scaleAnimation);
		set.addAnimation(animation);
		view.startAnimation(set);

		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				nmk_ShaiZiHuaLan.setVisibility(View.GONE);
				nmk_ShaiZi1.setBackgroundDrawable(anim1);
				anim1.start();
				transView(0, (int) (100 * Math.random()), 0,
						(int) (200 * Math.random()), nmk_ShaiZi1);

				nmk_ShaiZi2.setBackgroundDrawable(anim2);
				anim2.start();
				transView(0, (int) ((-200) * Math.random()), 0,
						(int) ((100) * Math.random()), nmk_ShaiZi2);

				if(!isTwoDiffActivity()){
					nmk_ShaiZi3.setBackgroundDrawable(anim3);
					anim3.start();
					transView(0, (int) (100 * Math.random()), 0,
							(int) (-100 * Math.random()), nmk_ShaiZi3);
				}
				
			}
		});
	}

	/**
	 * 骰子从花篮中飞出
	 */
	private void enlargeAnimation() {
		
		if(isTwoDiffActivity()){
			enlargeSclaeAnimation(nmk_ShaiZi1);
			enlargeSclaeAnimation(nmk_ShaiZi2);
		}else{
			enlargeSclaeAnimation(nmk_ShaiZi1);
			enlargeSclaeAnimation(nmk_ShaiZi2);
			enlargeSclaeAnimation(nmk_ShaiZi3);
		}
		
	}
	
	/**
	 * 判断是不是二不同玩法
	 * @return
	 */
	private boolean isTwoDiffActivity(){
		if(activity instanceof Nmk3TwoDiffActivity){
			return true;
		}else{
			return false;
		}
	}

}
