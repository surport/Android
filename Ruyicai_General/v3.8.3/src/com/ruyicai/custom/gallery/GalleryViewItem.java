package com.ruyicai.custom.gallery;

import com.ruyicai.json.miss.MissJson;
import com.ruyicai.pojo.AreaNum;

import android.content.Context;
import android.view.View;

public abstract class GalleryViewItem extends View {
	public AreaNum areaNums[];

	public GalleryViewItem(Context context) {
		super(context);
	}

	public abstract View createView();

	public abstract void updateView(MissJson missJson);
}
