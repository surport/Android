package com.ruyicai.activity.buy.jc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.Jc.Info;
import com.ruyicai.activity.join.JoinDetailActivity;
import com.ruyicai.activity.join.JoinHallActivity;
import com.ruyicai.activity.join.JoinInfoActivity;
import com.ruyicai.activity.join.JoinInfoActivity.JoinInfoAdapter;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class JcGuoGuan extends JcMain {
	private static final String[] strings = { "2串1", "3串1", "4串1", "5串1","6串1", "7串1", "8串1" };
	private List<Info> list;/* 列表适配器的数据源 */
	private final static String INFO = "INFO";
	private int INDEX = 0;
	private List<String> betcodes = new ArrayList<String>();
	private long iZhuShu ;
	private long iAmt ;
	private String alertStr;
	private String codeType ;
	private final String LOTNO = "J00001";
	private final int MAXAMT = 20000;
	private final int MAXINT = 2000000000;//int 最大值

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 initSpinner();
	}
    public void initSpinner(){
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int position = spinner.getSelectedItemPosition();
				INDEX = position;
				onclikSpinner(INDEX+2);
		
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
    }
    /**
     * 
     * 获取注码
     * 
     */
    public String[] getCode(){
    	String codes[]=new String[2];
    	String code = "50"+(INDEX+2)+"@";
    	String codeStr ="注码：\n";
		for (int i = 0; i < list.size(); i++) {
			Info info = (Info) list.get(i);
			if (info.onclikNum>0) {
             code +=info.getDay()+"|"+info.getWeek()+"|"+info.getTeamId()+"|";		
             codeStr += info.getHome()+" vs "+info.getAway()+"：";
             if(info.isWin()){
            	 code+="3";
            	 codeStr+="胜";
             }
             if(info.isLevel()){	
            	 code+="1";
            	 codeStr+="平";
             }
             if(info.isFail()){
            	 code+="0";
            	 codeStr+="负";
             }
            	 code+="^";
            	 codeStr+="\n";
			}
		}
		codes[0]=code;
		codes[1]=codeStr;
		return codes;
    }
	public void onclikSpinner(int position) {
		betcodes = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Info info = (Info) list.get(i);
			if (info.onclikNum>0) {
				betcodes.add(""+info.onclikNum);
			}
		}
		showEditText();
	}
	@Override
	public void isTouZhuNet() {		
		// TODO Auto-generated method stub
		if(iZhuShu>0){
			if(isAmtDialog()){
				alertInfo("单笔投注金额不能大于2万","温馨提示");
			}else{
				String codes[] =getCode();
				betAndGift.setSellway("0");//1代表机选 0代表自选
				betAndGift.setAmount(""+iAmt*100);
				betAndGift.setLotmulti(""+iZhuShu);
				betAndGift.setLotno(LOTNO);
				betAndGift.setBet_code(codes[0]);
				alert(getAlertStr(), codes[1]);
			}
		}else{
			alertInfo("请至少选择一注","请选择号码");
		}
	}
	/**
	 * 是否弹出温馨提示
	 * @return
	 */
	public boolean isAmtDialog(){
		if(iAmt>MAXAMT||iAmt<0){
			return true;
		}else{
			return false;
		}
	}
	public String getAlertStr(){
		String strs[] = alertStr.split(",");
		String returnStr = "注数："+iZhuShu+"注\n"+"倍数："+iProgressBeishu+"倍\n"+"金额："+iAmt+"元";
		return returnStr;
	}
    public void showEditText(){
    	iZhuShu = getAllAmt(betcodes, INDEX+2);
    	long beishu = iProgressBeishu;
		iAmt = iZhuShu*2*beishu;
		alertStr = "共"+iZhuShu+"注,"+beishu+"倍,"+"总金额"+iAmt+"元";
		showEditText(alertStr);
    }
	@Override
	public void initListView(ListView listview) {
		// TODO Auto-generated method stub
		// 数据源
		list = getListForJoinInfoAdapter();
		JcInfoAdapter adapter = new JcInfoAdapter(this, list);
		listview.setAdapter(adapter);
		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                       
			}

		};
		listview.setOnItemClickListener(clickListener);
	}

	/**
	 * 获得用户中心列表适配器的数据源
	 * 
	 * @return
	 */
	protected List<Info> getListForJoinInfoAdapter() {
		return Jc.infos;
	}

	public void initSpinner(int num,int index) {
		List allcountries = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			if (i > strings.length - 1) {
				break;
			} else {
				allcountries.add(strings[i]);
			}

		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		spinner.setAdapter(adapter);
		if(index<num){
			spinner.setSelection(index,false);
		}else{
			spinner.setSelection(allcountries.size()-1,false);
		}
	}
    /**
     * 返回总注数
     * @param betcodes
     * @param select
     * @return
     */
	protected  int getAllAmt(List<String> betcodes, int select) {
		// 初始化原始数据
		int[] a = new int[betcodes.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[select];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		combine(a, a.length, select, b, select, list);

		// 返回数据对象
		int resultInt = 0; 
		for (int[] result : list) {
			int itemNum = 1;
			for (int p : result) {
				itemNum *= Integer.parseInt(betcodes.get(p));
			}
			resultInt += itemNum;
		}

		return resultInt;
	}

	/**
	 * 组合的递归算法
	 * 
	 * @param a
	 *            原始数据
	 * @param n
	 *            原始数据个数
	 * @param m
	 *            选择数据个数
	 * @param b
	 *            存放被选择的数据
	 * @param M
	 *            常量，选择数据个数
	 * @param list
	 *            存放计算结果
	 */
	public static void combine(int a[], int n, int m, int b[], final int M,List<int[]> list) {
		for (int i = n; i >= m; i--) {
			b[m - 1] = i - 1;
			if (m > 1)
				combine(a, i - 1, m - 1, b, M, list);
			else {
				int[] result = new int[M];
				for (int j = M - 1; j >= 0; j--) {
					result[j] = a[b[j]];
				}
				list.add(result);
			}
		}
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			index = position;
			final Info info = (Info) mList.get(position);
			convertView = mInflater.inflate(R.layout.buy_jc_main_listview_item,
					null);
			final ViewHolder holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			holder.team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			holder.home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			holder.away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			holder.score = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_score);
			holder.timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			holder.btn1 = (Button) convertView.findViewById(R.id.jc_main_list_item_button1);
			holder.btn2 = (Button) convertView.findViewById(R.id.jc_main_list_item_button2);
			holder.btn3 = (Button) convertView.findViewById(R.id.jc_main_list_item_button3);
			convertView.setTag(holder);
			holder.time.setText(info.getTime());
			holder.team.setText(info.getTeam());
			holder.home.setText(info.getHome());
			holder.away.setText(info.getAway());
			holder.score.setText(info.getLetPoint());
			holder.timeEnd.setText(info.getTimeEnd());
			holder.btn1.setText("胜"+info.getWin());
			holder.btn2.setText("平"+info.getLevel());
			holder.btn3.setText("负"+info.getFail());
			if (info.isWin()) {
				holder.btn1.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				holder.btn1.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isLevel()) {
				holder.btn2.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				holder.btn2.setBackgroundResource(R.drawable.jc_btn);
			}
			if (info.isFail()) {
				holder.btn3.setBackgroundResource(R.drawable.jc_btn_b);
			} else {
				holder.btn3.setBackgroundResource(R.drawable.jc_btn);
			}
			holder.btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.setWin(!info.isWin());
					if (info.isWin()) {
						info.onclikNum++;
						holder.btn1.setBackgroundResource(R.drawable.jc_btn_b);
					} else {
						info.onclikNum--;
						holder.btn1.setBackgroundResource(R.drawable.jc_btn);
					}
					onclikBtn();
				}
			});
			holder.btn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.setLevel(!info.isLevel());
					if (info.isLevel()) {
						info.onclikNum++;
						holder.btn2.setBackgroundResource(R.drawable.jc_btn_b);
					} else {
						info.onclikNum--;
						holder.btn2.setBackgroundResource(R.drawable.jc_btn);
					}
					onclikBtn();
				}
			});
			holder.btn3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.setFail(!info.isFail());
					if (info.isFail()) {
						info.onclikNum++;
						holder.btn3.setBackgroundResource(R.drawable.jc_btn_b);
					} else {
						info.onclikNum--;
						holder.btn3.setBackgroundResource(R.drawable.jc_btn);
					}
					onclikBtn();
				}
			});
			return convertView;
		}

		public void onclikBtn() {
			int onclikNums = 0;
			betcodes = new ArrayList<String>();
			for (int i = 0; i < mList.size(); i++) {
				Info info = (Info) mList.get(i);
				if (info.onclikNum>0) {
					onclikNums++;
					betcodes.add(""+info.onclikNum);
				}
			}
			if (onclikNums < 9) {
				initSpinner(onclikNums - 1,INDEX);
			}
		   showEditText();
		}

		class ViewHolder {
			TextView time;
			TextView team;
			TextView home;
			TextView away;
			TextView score;
			TextView timeEnd;
			Button btn1;
			Button btn2;
			Button btn3;

		}
	}

}
