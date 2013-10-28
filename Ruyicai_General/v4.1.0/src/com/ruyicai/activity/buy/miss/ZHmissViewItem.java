package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.json.miss.MissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * 组合遗漏界面
 * 
 * @author Administrator
 * 
 */
public class ZHmissViewItem extends BuyViewItemMiss {
	public List<String> missList = new ArrayList<String>();
	public List<BtnArea> areaList = new ArrayList<BtnArea>();
	public MissJson missJson;
	public int LINE_NUM = 3;
	private int iScreenWidth;
	private int textSize = 1;// 按钮字符分隔长度
	LayoutInflater inflate;
	LinearLayout layoutMian;

	public ZHmissViewItem(BaseActivity zixuan, MissJson missJson, int lineNum,
			int textSize) {
		super(zixuan, null);
		this.LINE_NUM = lineNum;
		this.textSize = textSize;
		if (missJson != null) {
			this.missList = missJson.zMissList;
			this.missJson = missJson;
			iScreenWidth = PublicMethod.getDisplayWidth(context);
		}
		missBtnList = new ArrayList<MyButton>();
		isZHmiss = true;
	}

	/**
	 * 创建购彩界面
	 * 
	 * @param areaInfos
	 *            选球区
	 */
	public View createView() {
		inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.buy_zh_view, null);
		layoutMian = (LinearLayout) view.findViewById(R.id.buy_zh_view_layout);
		leftBtn(view);
		addLayout(layoutMian);
		return view;
	}

	public void updateView(MissJson missJson) {
		this.missList = missJson.zMissList;
		this.missJson = missJson;
		iScreenWidth = PublicMethod.getDisplayWidth(context);
		addLayout(layoutMian);
	}

	public void addLayout(LinearLayout layoutMian) {
		int line = missList.size() / LINE_NUM;
		int lastNum = missList.size() % LINE_NUM;
		for (int i = 0; i < line; i++) {
			addLayoutItem(layoutMian, i, LINE_NUM);
		}
		if (lastNum != 0) {
			addLayoutItem(layoutMian, line, lastNum);
		}
	}

	private void addLayoutItem(LinearLayout layoutMian, int line, int lineNum) {
		LinearLayout layoutItem = new LinearLayout(context);
		int height = PublicMethod.getPxInt(30, context);
		int top = PublicMethod.getPxInt(10, context);
		int padding = PublicMethod.getPxInt(4, context);
		int width = iScreenWidth / LINE_NUM - 2 * padding;
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		param.setMargins(0, top, 0, 0);
		layoutItem.setLayoutParams(param);
		BtnArea btnArea = new BtnArea(layoutItem, width, height, padding);
		areaList.add(btnArea);
		for (int j = 0; j < lineNum; j++) {
			layoutItem.addView(initItem(line, j, width, height, padding));
		}
		if (layoutItem != null) {
			layoutMian.addView(layoutItem, line);
		}
		this.height += height * 2;
	}

	private View initItem(int line, int j, int width, int height, int padding) {
		View viewItem = (LinearLayout) inflate.inflate(
				R.layout.buy_zh_view_item, null);
		final MyButton btn = (MyButton) viewItem
				.findViewById(R.id.buy_zh_view_btn1);
		TextView text = (TextView) viewItem
				.findViewById(R.id.buy_zh_view_text1);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width,
				height);
		param.setMargins(padding, 0, padding, 0);
		btn.setLayoutParams(param);
		int index = line * LINE_NUM + j;
		initText(text, index);
		initBtn(width, btn, index);
		missBtnList.add(btn);
		return viewItem;
	}

	private void initBtn(int width, final MyButton btn, int index) {
		String btnStr = missJson.getMissKey().get(index);
		btn.setCodeStr(btnStr);
		btn.setBtnText(addChar(btnStr, ",", textSize));
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn.onAction();
				zixuan.changeTextSumMoney();
			}
		});
		int size = 16;
		setTextValue(width, btn, btnStr, size);
		while (setTextValue(width, btn, btnStr, size) == false) {
			size--;
		}
	}

	private boolean setTextValue(int width, final MyButton btn, String btnStr,
			int sizeStr) {
		float wf = 11.5f / 16f;
		float tf = 21f / 16f;
		int size = PublicMethod.getPxInt(sizeStr, context);
		int strW = PublicMethod.getPxInt(sizeStr * wf, context);
		int top = PublicMethod.getPxInt(sizeStr * tf, context);
		int sX = width / 2 - btnStr.length() * strW / 2;
		if (sX > 0) {
			btn.setTextX(sX);
			btn.setTextSize(size);
			btn.setTextY(top);
			return true;
		} else {
			return false;
		}
	}

	private String addChar(String btnStr, String flag, int size) {
		char[] strs = btnStr.toCharArray();
		String newStr = "";
		int index = 0;
		for (int i = 0; i < strs.length; i++) {
			index++;
			char str = strs[i];
			newStr += str;
			if (index == size) {
				newStr += flag;
				index = 0;
			}
		}
		newStr = newStr.substring(0, newStr.length() - 1);
		return newStr;
	}

	private void initText(TextView text, int index) {
		String textStr = missJson.zMissList.get(index);
		String content = "遗漏" + textStr + "期";
		PublicMethod.setTextColor(text, content, 2, content.length() - 1,
				Color.RED);
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	/**
	 * 组合遗漏选区信息类
	 * 
	 * @author Administrator
	 * 
	 */
	public class BtnArea {
		private LinearLayout layoutArea;
		private int height;
		private int width;
		private int lineNum;
		private int padding;

		public BtnArea(LinearLayout layoutArea, int width, int height,
				int padding) {
			this.layoutArea = layoutArea;
			this.width = width;
			this.height = height;
			this.padding = padding;
		}

		public int getPadding() {
			return padding;
		}

		public void setPadding(int padding) {
			this.padding = padding;
		}

		public int getLineNum() {
			return lineNum;
		}

		public void setLineNum(int lineNum) {
			this.lineNum = lineNum;
		}

		public LinearLayout getLayoutArea() {
			return layoutArea;
		}

		public void setLayoutArea(LinearLayout layoutArea) {
			this.layoutArea = layoutArea;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

	}
}
