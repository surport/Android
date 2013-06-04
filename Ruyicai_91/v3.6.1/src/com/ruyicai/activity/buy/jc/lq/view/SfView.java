package com.ruyicai.activity.buy.jc.lq.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.jc.JcMainView;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.code.jc.lq.BasketSF;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.QueryJcInfoInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
/**
 * 胜负类
 * @author Administrator
 *
 */
public class SfView extends JcMainView{
	private final int MAX_TEAM = 8;
	protected BasketSF basketSf;
	protected JcInfoAdapter adapter;
    protected static final int B_SF = 0;
    public static final int B_RF = 1;
    public static final int B_DX = 2;
	public SfView(Context context,BetAndGiftPojo betAndGift,Handler handler,LinearLayout layout,String type,boolean isdanguan,List<String> checkTeam) {
		super(context, betAndGift, handler, layout,type,isdanguan,checkTeam);
		basketSf = new BasketSF(context);
	}
	@Override
	public String getLotno() {
		// TODO Auto-generated method stub
		return Constants.LOTNO_JCLQ;
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
		return context.getString(R.string.jclq_sf_danguan_title).toString();	
		}else{
		return context.getString(R.string.jclq_sf_guoguan_title).toString();
		}
	}
	@Override
	public String getTypeTitle() {
		// TODO Auto-generated method stub
		return context.getString(R.string.jclq_dialog_sf_guoguan_title).toString();
	}
	@Override
	public String getCode(String key, List<Info> listInfo) {
		// TODO Auto-generated method stub
		return basketSf.getCode(key, listInfo);
	}
	@Override
	public String getAlertCode(List<Info> listInfo) {
    	String codeStr ="";
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum>0) {
             codeStr +=info.getAway()+" vs "+info.getHome()+"：";
             if(info.isWin()){
            	 codeStr+="胜";
             }
             if(info.isFail()){
            	 codeStr+="负";
             }
             if(info.isDan()){
            	 codeStr+="(胆)";
             }
     			 codeStr+="\n\n";
			}
			
		}
		return codeStr;
	}
	@Override
	public int getTeamNum() {
		// TODO Auto-generated method stub
		return MAX_TEAM;
	}

    /**
     * 初始化列表
     */
	public void initListView(ListView listview,Context context,List<List> listInfo) {
		// TODO Auto-generated method stub
		adapter = new JcInfoAdapter(context,listInfo,B_SF);
		listview.setAdapter(adapter);
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;
		private int type;

		public JcInfoAdapter(Context context, List<List> list,int type) {
			mInflater = LayoutInflater.from(context);
			mList = list;
            this.type = type;
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
			convertView = mInflater.inflate(R.layout.buy_lq_main_listview_item,null);
			final ViewHolder holder = new ViewHolder();
			TextView time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			TextView team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			TextView home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			TextView away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			TextView score = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_score);
			TextView timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			final Button btnFail = (Button) convertView.findViewById(R.id.jc_main_list_item_button1);
			final Button btnWin = (Button) convertView.findViewById(R.id.jc_main_list_item_button3);
			final Button btnDan = (Button) convertView.findViewById(R.id.jc_main_list_item_button_dan);
			final Button btnXi = (Button) convertView.findViewById(R.id.buy_jc_main_list_item_btn_xi);

			time.setText(info.getTime()+"  "+context.getString(R.string.jc_main_team_id_title)+info.getTeamId());
			team.setText(info.getTeam());
			home.setText(info.getAway()+"(客)");
			away.setText(info.getHome()+"(主)");
			timeEnd.setText(info.getTimeEnd());
			if(type==B_RF){
				score.setText(info.getLetPoint());
				btnFail.setText("主负"+info.getLetFail());
				btnWin.setText("主胜"+info.getLetWin());
			}else if(type==B_DX){
				btnFail.setText("大"+info.getBig());
				btnWin.setText("小"+info.getSmall());
				score.setText(info.getBasePoint());
			}else{
				btnFail.setText("主负"+info.getFail());
				btnWin.setText("主胜"+info.getWin());
			}
			if (info.isFail()) {
				btnFail.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btnFail.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isWin()) {
				btnWin.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				btnWin.setBackgroundResource(R.drawable.jc_btn);
			}
			btnFail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.onclikNum>0||isCheckTeam()){
						info.setFail(!info.isFail());
						if (info.isFail()) {
							info.onclikNum++;
							btnFail.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
							btnFail.setBackgroundResource(R.drawable.jc_btn);
						}
						isNoDan(info,btnDan);
						setTeamNum();
					}
				}
			});
			btnWin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.onclikNum>0||isCheckTeam()){
						info.setWin(!info.isWin());
						if (info.isWin()) {
							info.onclikNum++;
							btnWin.setBackgroundResource(R.drawable.jc_btn_b);
						} else {
							info.onclikNum--;
							btnWin.setBackgroundResource(R.drawable.jc_btn);
						}
						isNoDan(info,btnDan);
						setTeamNum();
					}
				}
			});
			if(isDanguan){
				btnDan.setVisibility(Button.GONE);
			}else{
				btnDan.setVisibility(Button.VISIBLE);
				btnDan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(info.isDan()){
							info.setDan(false);
							btnDan.setBackgroundResource(R.drawable.jc_btn);
						}else if(info.onclikNum>0&&isDanCheckTeam()&&isDanCheck()){
							info.setDan(true);
							btnDan.setBackgroundResource(R.drawable.jc_btn_b);
						}
					}
				});
			}
//			btnXi.setVisibility(Button.VISIBLE);
			btnXi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					trunExplain(getEvent(Constants.JCBASKET, info));
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
			return "J00005_0";
		}else{
			return "J00005_1";
		}
	}
	@Override
	public List<double[]> getOdds(List<Info> listInfo) {
		// TODO Auto-generated method stub
		return basketSf.getOddsList(listInfo,B_SF);
	}


}
