package com.ruyicai.activity.buy.jc.zq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.zq.FootBQC;
import com.ruyicai.code.jc.zq.FootSpf;
import com.ruyicai.code.jc.zq.FootZJQ;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

/**
 * 总进球
 * @author Administrator
 *
 */
public class ZJQView extends JcMainView{
	private String checkTitle[] = {"0","1","2","3","4","5","6","7+"};
	private final int MAX_TEAM = 6;
	FootZJQ  footZjqCode;
	JcInfoAdapter adapter;
	public ZJQView(Context context,BetAndGiftPojo betAndGift,Handler handler,LinearLayout layout,String type,boolean isdanguan,List<String> checkTeam) {
		super(context, betAndGift, handler, layout,type,isdanguan,checkTeam);
		footZjqCode = new FootZJQ(context);
	}
	public void setDifferValue(JSONObject jsonItem,Info itemInfo) throws JSONException{
		itemInfo.vStrs = new String[8];
		for(int j=0;j<8;j++){
			itemInfo.getVStrs()[j] = jsonItem.getString("goal_v"+j);
		}
	}
	@Override
	public int getTeamNum() {
		// TODO Auto-generated method stub
		return MAX_TEAM;
	}
	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}
	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCZQ_ZQJ;
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		if(isDanguan){
		return context.getString(R.string.jczq_rf_danguan_title).toString();	
		}else{
		return context.getString(R.string.jczq_rf_guoguan_title).toString();
		}
	}

	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jczq_dialog_rf_guoguan_title).toString();
	}
	 /**
     * 
     * 获取注码
     * 
     */
    public String getCode(String key,List<Info> listInfo){
		return footZjqCode.getCode(key, listInfo);
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
         	for(int j=0;j<info.check.length;j++){
				if(info.check[j].isChecked()){
	            	 codeStr+= info.check[j].getChcekTitle()+" ";
				}
			}
             codeStr.subSequence(0, codeStr.length()-1);
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
			LinearLayout layout1 = (LinearLayout) convertView.findViewById(R.id.lq_sf_layout_bottom1);
			LinearLayout layout2 = (LinearLayout) convertView.findViewById(R.id.lq_sf_layout_bottom2);
			layout1.setVisibility(LinearLayout.GONE);
			layout2.setVisibility(LinearLayout.VISIBLE);
			TextView time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			TextView team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			TextView home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			TextView away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			TextView timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			TextView score = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_score);
			TextView btn = (Button) convertView.findViewById(R.id.jc_main_list_item_button);
			score.setVisibility(TextView.GONE);
			time.setText(info.getTime()+"  "+context.getString(R.string.jc_main_team_id_title)+info.getTeamId());
			team.setText(info.getTeam());
			home.setText(info.getHome()+"(主)");
			away.setText(info.getAway()+"(客)");
			timeEnd.setText(info.getTimeEnd());
			
			if(!info.getBtnStr().equals("")){
				btn.setText(info.getBtnStr());
			}
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.onclikNum>0||isCheckTeam()){
						info.createDialog(checkTitle,false,info.getHome()+" VS "+info.getAway());
					}
				}
			});
			return convertView;
		}
		class ViewHolder {
			Button btn;
			LinearLayout layout;

		}
	}
	@Override
	public String getPlayType() {
		// TODO Auto-generated method stub
		if(isDanguan){
			return "J00003_0";
		}else{
			return "J00003_1";
		}
	}
	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return footZjqCode.getOddsList(listInfo);
	}

}
