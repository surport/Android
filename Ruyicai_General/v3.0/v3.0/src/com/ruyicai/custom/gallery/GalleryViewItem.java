package com.ruyicai.custom.gallery;

import android.content.Context;
import android.view.View;

public abstract class GalleryViewItem extends View{

	public GalleryViewItem(Context context) {
		super(context);
	}
	public abstract View createView();
	public abstract void updateView();
}
