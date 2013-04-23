package com.ruyicai.activity.buy.jc.zq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.zq.FootSpf;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.Constants;
/**
 * 胜负类
 * @author Administrator
 *
 */
public class SPfView extends JcMainView{
	private final int MAX_TEAM = 8;
	JcInfoAdapter adapter;
	FootSpf  footSpfCode;
	public SPfView(Context context,BetAndGiftPojo betAndGift,Handler handler,LinearLayout layout,String type,boolean isdanguan,List<String> checkTeam) {
		super(context, betAndGift, handler, layout,type,isdanguan,checkTeam);
		footSpfCode = new FootSpf(context);
	}
	@Override
	public int getTeamNum() {
		// TODO Auto-generated method stub
		return MAX_TEAM;
	}
	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCZQ;
	}
	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		if(isDanguan){
		return context.getString(R.string.jczq_sf_danguan_title).toString();
		}else{
		return context.getString(R.string.jczq_sf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jczq_dialog_sf_guoguan_title).toString();
	}
	public void setDifferValue(JSONObject jsonItem,Info itemInfo) throws JSONException{
		itemInfo.setLevel(jsonItem.getString("v1"));
	}

	 /**
     * 
     * 获取注码
     * 
     */
    public String getCode(String key,List<Info> listInfo){
		return footSpfCode.getCode(key, listInfo);
    }
	 /**
     * 
     * 获取注码
     * 
     */
    public String getAlertCode(List<Info> listInfo){
    	String codeStr ="";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum>0) {
             codeStr += info.getHome()+" vs "+info.getAway()+"：";
             if(info.isWin()){
            	 codeStr+="胜";
             }
             if(info.isLevel()){	
            	 codeStr+="平";
             }
             if(info.isFail()){
            	 codeStr+="负";
             }
     			 codeStr+="\n\n";
			 }
			
		}
		return codeStr;
    }

    /**
     * 初始化列表
     */
	public void initListView(ListView listview,Context context,List<List> listInfo) {
		// TODO Auto-generated method stub
	    adapter = new JcInfoAdapter(context,listInfo);
		listview.setAdapter(adapter);
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;

		public JcInfoAdapter(Context context, List<List> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		int index;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			index = position;
		    final ArrayList<Info> list = (ArrayList<Info>) mList.get(position); 
			convertView = mInflater.inflate(R.layout.buy_jc_main_view_list_item,null);
			final ViewHolder holder = new ViewHolder();
			holder.btn = (Button) convertView.findViewById(R.id.buy_jc_main_view_list_item_btn);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);
			holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			if(list.size() == 0){
				holder.btn.setVisibility(Button.GONE);
			}else{
				isOpen(list, holder);
				holder.btn.setText(list.get(0).getTime()+"  "+list.size()+context.getString(R.string.jc_main_btn_text));
				holder.btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						list.get(0).isOpen = !list.get(0).isOpen;
						isOpen(list, holder);
					}
				});
				for(Info info:list){
					holder.layout.addView(addLayout(info));
				}
			}
			return convertView;
		}

		private void isOpen(final ArrayList<Info> list, final ViewHolder holder) {
			if(list.get(0).isOpen){
				holder.layout.setVisibility(LinearLayout.VISIBLE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_open);
			}else{
				holder.layout.setVisibility(LinearLayout.GONE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			}
		}

		private View addLayout(final Info info) {
			View convertView;
			convertView = mInflater.inflate(R.layout.buy_jc_main_listview_item,null);
			TextView time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			TextView team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			TextView home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			TextView away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			TextView score = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_vs);
			TextView timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			final Button btn1 = (Button) convertView.findViewById(R.id.jc_main_list_item_button1);
			final Button btn2 = (Button) convertView.findViewById(R.id.jc_main_list_item_button2);
			final Button btn3 = (Button) convertView.findViewById(R.id.jc_main_list_item_button3);
			time.setText(info.getTime()+"  "+context.getString(R.string.jc_main_team_id_title)+info.getTeamId());
			team.setText(info.getTeam());
			score.setText(info.getLetPoint());
			score.setTextColor(Color.BLUE);
			home.setText(info.getHome()+"(主)");
			away.setText(info.getAway()+"(客)");
			timeEnd.setText(info.getTimeEnd());
			btn1.setText("胜"+info.getWin());
			btn2.setText("平"+info.getLevel());
			btn3.setText("负"+info.getFail());
			if (info.isFail()) {
				btn3.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btn3.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isWin()) {
				btn1.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btn1.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isLevel()) {
				btn2.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btn2.setBackgroundResource(R.drawable.jc_btn);
			}
	        btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.onclikNum>0||isCheckTeam()){
						info.setWin(!info.isWin());
						if (info.isWin()) {
							info.onclikNum++;
							btn1.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
							btn1.setBackgroundResource(R.drawable.jc_btn);
						}
						setTeamNum();
					}
				}
			});
			btn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.onclikNum>0||isCheckTeam()){
						info.setLevel(!info.isLevel());
						if (info.isLevel()) {
							info.onclikNum++;
							btn2.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
						    btn2.setBackgroundResource(R.drawable.jc_btn);
						}
						setTeamNum();
					}
				}
			});
			btn3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.onclikNum>0||isCheckTeam()){
						info.setFail(!info.isFail());
						if (info.isFail()) {
							info.onclikNum++;
							btn3.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
							btn3.setBackgroundResource(R.drawable.jc_btn);
						}
						setTeamNum();
					}
				}
			});
			return convertView;
		}



		class ViewHolder {
			TextView time;
			TextView team;
			TextView home;
			TextView away;
			TextView score;
			TextView timeEnd;
			Button btn1;
			Button btn3;
			Button btn;
			LinearLayout layout;

		}
	}

}
