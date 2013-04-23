package com.ruyicai.activity.buy.high;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.pojo.AreaNum;

public class HighItemView {
	private View view;
	private AreaNum areaNum[];
	public List<List> missList = new ArrayList<List>();
	public AddView addView;
	private ViewPager mGallery = null;
	public List<String> ZHMissList;
	public boolean isMissNet = true;
	public boolean isZMissNet = true;
	protected EditText editZhuma;
	List<BuyViewItemMiss> itemViewArray;

	public HighItemView(View view, AreaNum areaNum[], AddView addView,
			List<BuyViewItemMiss> itemViewArray, EditText editZhuma) {
		this.view = view;
		this.areaNum = areaNum;
		this.addView = addView;
		this.itemViewArray = itemViewArray;
		this.editZhuma = editZhuma;
	}

	public List<String> getZHMissList() {
		return ZHMissList;
	}

	public void setZHMissList(List<String> zHMissList) {
		ZHMissList = zHMissList;
	}

	public List<BuyViewItemMiss> getItemViewArray() {
		return itemViewArray;
	}

	public void setItemViewArray(List<BuyViewItemMiss> itemViewArray) {
		this.itemViewArray = itemViewArray;
	}

	public ViewPager getmGallery() {
		return mGallery;
	}

	public void setmGallery(ViewPager mGallery) {
		this.mGallery = mGallery;
	}

	public AddView getAddView() {
		return addView;
	}

	public void setAddView(AddView addView) {
		this.addView = addView;
	}

	public List<List> getMissList() {
		return missList;
	}

	public void setMissList(List<List> missList) {
		this.missList = missList;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public AreaNum[] getAreaNum() {
		return areaNum;
	}

	public void setAreaNum(AreaNum[] areaNum) {
		this.areaNum = areaNum;
	}
}
