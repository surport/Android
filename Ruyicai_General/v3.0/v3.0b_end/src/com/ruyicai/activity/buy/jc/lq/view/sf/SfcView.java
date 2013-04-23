package com.ruyicai.activity.buy.jc.lq.view.sf;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.ruyicai.activity.buy.jc.lq.view.LqMainView;
import com.ruyicai.activity.buy.jc.lq.view.LqMainView.Info;
import com.ruyicai.activity.buy.jc.lq.view.sf.SfView.JcInfoAdapter;
import com.ruyicai.activity.buy.jc.lq.view.sf.SfView.JcInfoAdapter.ViewHolder;
import com.ruyicai.custom.checkbox.MyCheckBox;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.Constants;

/**
 * 胜分差类
 * @author Administrator
 *
 */
public class SfcView extends LqMainView{
	JcInfoAdapter adapter;
	public SfcView(Context context,BetAndGiftPojo betAndGift,Handler handler,LinearLayout layout) {
		super(context, betAndGift, handler, layout);
	}
	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCLQ_SFC;
	}
	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}	
	 /**
     * 
     * 获取注码
     * 
     */
    public String[] getCode(int type,List<Info> listInfo){
    	String codes[]=new String[2];
    	String code = "50"+type+"@";
    	String codeStr ="注码：\n";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum>0) {
             code +=info.getDay()+"|"+info.getWeek()+"|"+info.getTeamId()+"|";		
             codeStr += info.getAway()+" vs "+info.getHome()+"：";
			for(int j=0;j<info.check.length;j++){
				if(info.check[j].isChecked()){
					 code+=info.codes[j];
	            	 codeStr+= info.check[j].getChcekTitle()+" ";
				}
			}
			 code+="^";
 			 codeStr+="\n";
		 }
			
		}
		codes[0]=code;//投注注码
		codes[1]=codeStr.substring(0, codeStr.length()-1);//提示框注码
		return codes;
    }

    /**
     * 初始化列表
     */
	public void initListView(ListView listview,Context context,List<Info> listInfo) {
		// TODO Auto-generated method stub
		adapter = new JcInfoAdapter(context,listInfo);
		adapter.notifyDataSetChanged();
		listview.setAdapter(adapter);
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Info> mList;

		public JcInfoAdapter(Context context, List<Info> list) {
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

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final Info info = (Info) mList.get(position);
			ViewHolder holder;
			convertView = mInflater.inflate(R.layout.buy_lq_main_listview_item,null);
			LinearLayout layout1 = (LinearLayout) convertView.findViewById(R.id.lq_sf_layout_bottom1);
			LinearLayout layout2 = (LinearLayout) convertView.findViewById(R.id.lq_sf_layout_bottom2);
			layout1.setVisibility(LinearLayout.GONE);
			layout2.setVisibility(LinearLayout.VISIBLE);
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			holder.team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			holder.home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			holder.away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			holder.timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			holder.btn = (Button) convertView.findViewById(R.id.jc_main_list_item_button);
			convertView.setTag(holder);
			holder.time.setText(info.getTime());
			holder.team.setText(info.getTeam());
			holder.home.setText(info.getAway());
			holder.away.setText(info.getHome());
			holder.timeEnd.setText(info.getTimeEnd());
			if(!info.getBtnStr().equals("")){
				holder.btn.setText(info.getBtnStr());
			}
			holder.btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.createDialog();
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
			Button btn;
			public ViewHolder(){
			}
			
		}
	}





}
