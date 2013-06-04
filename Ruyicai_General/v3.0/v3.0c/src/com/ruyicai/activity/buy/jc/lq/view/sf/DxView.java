package com.ruyicai.activity.buy.jc.lq.view.sf;

import java.util.ArrayList;
import java.util.List;

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
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.Constants;

/**
 * 大小分
 * @author Administrator
 *
 */
public class DxView extends LqMainView{
	public DxView(Context context,BetAndGiftPojo betAndGift,Handler handler,LinearLayout layout) {
		super(context, betAndGift, handler, layout);
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCLQ_DXF;
	}
	 /**
     * 
     * 获取注码1大，2小
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
             if(info.isBig()){
            	 code+="1";
            	 codeStr+="大";
             }
             if(info.isSmall()){
            	 code+="2";
            	 codeStr+="小";
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
		JcInfoAdapter adapter = new JcInfoAdapter(context,listInfo);
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

		int index;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			index = position;
			final Info info = (Info) mList.get(position);
			convertView = mInflater.inflate(R.layout.buy_lq_main_listview_item,null);
			final ViewHolder holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			holder.team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			holder.home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			holder.away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			holder.score = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_score);
			holder.timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			holder.btn1 = (Button) convertView.findViewById(R.id.jc_main_list_item_button1);
			holder.btn3 = (Button) convertView.findViewById(R.id.jc_main_list_item_button3);
			convertView.setTag(holder);
			holder.time.setText(info.getTime());
			holder.team.setText(info.getTeam());
			holder.home.setText(info.getAway());
			holder.away.setText(info.getHome());
			holder.timeEnd.setText(info.getTimeEnd());
			holder.btn1.setText("大"+info.getBig());
			holder.btn3.setText("小"+info.getSmall());
			holder.score.setText(info.getBasePoint());
			if (info.isBig()) {
				holder.btn1.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				holder.btn1.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isSmall()) {
				holder.btn3.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				holder.btn3.setBackgroundResource(R.drawable.jc_btn);
			}
			holder.btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.setBig(!info.isBig());
					if (info.isBig()) {
						info.onclikNum++;
						holder.btn1.setBackgroundResource(R.drawable.jc_btn_b);
					} else {
						info.onclikNum--;
						holder.btn1.setBackgroundResource(R.drawable.jc_btn);
					}
					info.onclikBtn();
				}
			});
			holder.btn3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.setSmall(!info.isSmall());
					if (info.isSmall()) {
						info.onclikNum++;
						holder.btn3.setBackgroundResource(R.drawable.jc_btn_b);
					} else {
						info.onclikNum--;
						holder.btn3.setBackgroundResource(R.drawable.jc_btn);
					}
					info.onclikBtn();
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

		}
	}


}
