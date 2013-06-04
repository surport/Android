/**
 * 
 */
package com.ruyicai.pojo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ruyicai.util.AreaInfo;

/**
 * 选区类
 * 
 * @author Administrator
 */
public class AreaNum {
	public AreaInfo info;
    public TableLayout tableLayout;
	public BallTable table;
	public TextView textTitle;
	public LinearLayout layout;

	public AreaNum(int tableLayoutId, int textViewId,View view) {
     tableLayout = (TableLayout) view.findViewById(tableLayoutId);
     textTitle = (TextView) view.findViewById(textViewId);
	}
	public AreaNum(int tableLayoutId, int textViewId,int linearViewId,View view) {
		 layout = (LinearLayout) view.findViewById(linearViewId);
		 layout.setVisibility(LinearLayout.VISIBLE);
	     tableLayout = (TableLayout) view.findViewById(tableLayoutId);
	     textTitle = (TextView) view.findViewById(textViewId);
	    
	    
	}
	public AreaNum(){

	}
	/**
	 * 初始化数据
	 */
	public void init(){
	  textTitle.setText(info.textTtitle);
	}
}
