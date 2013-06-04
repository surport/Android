package com.ruyicai.pojo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid91.R;

public class Lights {
	private Context context;
	private int[] images;
	private LinearLayout layout;
	private List<ImageView> lights = new ArrayList<ImageView>();
	private int ISLIGHT = 1;
	private int index = 0;
    private int[] animationId = {R.anim.shake,R.anim.push_up_in,R.anim.push_up_out,R.anim.slide_top_to_bottom,
    		                     R.anim.layout_animation_image};
	public Lights(Context context) {
		this.context = context;

	}

	public void createViews(int num, int[] images, LinearLayout layout) {
		this.images = images;
		this.layout = layout;
		for (int i = 0; i < num; i++) {
			ImageView view = new ImageView(context);
			view.setImageResource(images[0]);
			if (i != num - 1) {
				view.setPadding(0, 0, 20, 0);
			}
			this.layout.addView(view);
			lights.add(view);
		}
	}

	public void isDefault(int position) {
		index = position;
		ImageView view = lights.get(position);
		view.setImageResource(images[ISLIGHT]);
	}

	public void isLight(int position) {
		if (index != position) {
			clearAll();
			index = position;
			ImageView view = lights.get(position);
			view.setImageResource(images[ISLIGHT]);
			startAnim(view);
		}

	}

	public void startAnim(View view) {
//		int redNum[] = PublicMethod.getRandomsWithoutCollision(1, 0, animationId.length-1);
//		Log.e("redNum[]===",""+redNum[0]);
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		view.startAnimation(anim);

	}

	public void clearAll() {
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).setImageResource(images[0]);
		}
	}
}
