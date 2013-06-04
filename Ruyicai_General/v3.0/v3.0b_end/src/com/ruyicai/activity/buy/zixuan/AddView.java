package com.ruyicai.activity.buy.zixuan;

import java.util.ArrayList;
import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.test.IsolatedContext;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

/**
 * 号码篮类
 * 
 * @author Administrator
 * 
 */
public class AddView {
	private List<CodeInfo> codeList = new ArrayList<CodeInfo>();
	private Context context;
	private String title;
	private AlertDialog dialog;
	private View view ;
	private ListView listView;
	private TextView textNum ;
	private AddListAdapter listAdapter;
	private TextView infoText;
	ZixuanActivity zXActivity;
	public AddView(TextView textNum,ZixuanActivity zixuan) {
		this.context = zixuan.getContext();
		this.zXActivity = zixuan;
		this.textNum = textNum;
		updateTextNum();
		
	}
	/**
	 * 刷新号码篮个数
	 */
	public void updateTextNum(){
		textNum.setText(""+codeList.size());
	}
	public void createDialog(String titleStr) {
		LayoutInflater factory = LayoutInflater.from(context);
		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		view = factory.inflate(R.layout.buy_add_dialog,null);
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		infoText = (TextView) view.findViewById(R.id.buy_add_dialog_text_info);
		title.setText(titleStr);
		initListView(true);
		Button xuanhao = (Button) view.findViewById(R.id.ok);
		Button touzhu = (Button) view.findViewById(R.id.canel);
		xuanhao.setText(context.getString(R.string.buy_add_dialog_xuanhao));
		touzhu.setText(context.getString(R.string.buy_add_dialog_touzhu));
		xuanhao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				updateTextNum();
			}
		});
		touzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				updateTextNum();
				if(codeList.size()>0){
					zXActivity.alert();
				}
			}
		});
		updateInfoText();
	}
	public void createCodeInfoDialog(){
		LayoutInflater factory = LayoutInflater.from(context);
		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		view = factory.inflate(R.layout.buy_add_dialog,null);
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		infoText = (TextView) view.findViewById(R.id.buy_add_dialog_text_info);
		title.setText(context.getString(R.string.buy_add_dialog_title));
		initListView(false);
		updateInfoText();
		Button xuanhao = (Button) view.findViewById(R.id.ok);
		Button touzhu = (Button) view.findViewById(R.id.canel);
		touzhu.setVisibility(Button.GONE);
		xuanhao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}
	/**
	 * 拼装投注时的注码串
	 */
	public String getTouzhuCode(int beishu,int amt){
		String code = "";
		for(int i=0;i<codeList.size();i++){
			CodeInfo codeInfo = codeList.get(i);
			code += codeInfo.getTouZhuCode(beishu,amt);
			if(i!=codeList.size()-1){
				code += "!";
			}
		}
		return code;
	}
	private void updateInfoText() {
		infoText.setText("共选择"+getAllZhu()+"注，"+"总金额"+getAllAmt()+"元");
	}
	public int getAllZhu(){
		int allZhu = 0;
		for(CodeInfo codeInfo:codeList){
			allZhu += codeInfo.getZhuShu();
		}
		return allZhu;
	}
	public int getAllAmt(){
		int allAmt = 0;
		for(CodeInfo codeInfo:codeList){
			allAmt += codeInfo.getAmt();
		}
		return allAmt;
	}
	private void initListView(boolean isDelet) {
		listView = (ListView) view.findViewById(R.id.buy_add_dialog_list); 
		listAdapter = new AddListAdapter(context,codeList,isDelet);
		listView.setAdapter(listAdapter);
	}
	public void updateListView(){
		listAdapter.notifyDataSetChanged();
	}
	public int getSize(){
		return codeList.size();
	}
	public CodeInfo initCodeInfo(int amt,int zhuShu){
		return new CodeInfo(amt, zhuShu);
	}
	public void addCodeInfo(CodeInfo codeInfo){
		codeList.add(codeInfo);
	}
	public List<CodeInfo> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<CodeInfo> codeList) {
		this.codeList = codeList;
	}
	public void showDialog(){
		dialog.show();
		dialog.getWindow().setContentView(view);
	}
	class AddListAdapter extends BaseAdapter{
		private boolean isDelet = true;
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<CodeInfo> codeInfos = new ArrayList<CodeInfo>();
        public AddListAdapter(Context context,List<CodeInfo> codeInfos,boolean isDelet){
        	mInflater = LayoutInflater.from(context);
        	this.codeInfos = codeInfos;
        	this.isDelet = isDelet;
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return codeInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return codeInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}  

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			CodeInfo codeInfo = codeInfos.get(position);
			convertView = mInflater.inflate(R.layout.buy_add_dialog_list_item,null);
			TextView textNum =  (TextView) convertView.findViewById(R.id.buy_add_list_item_text_num);
			TextView textCode =  (TextView) convertView.findViewById(R.id.buy_add_list_item_text_code);
			TextView textZhuShu =  (TextView) convertView.findViewById(R.id.buy_add_list_item_text_zhushu);
			TextView textAmt =  (TextView) convertView.findViewById(R.id.buy_add_list_item_text_amt);
			Button btnDelet = (Button)convertView.findViewById(R.id.buy_add_dialog_delet);
			btnDelet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						codeInfos.remove(position);
						updateListView();
						updateInfoText();
				}
			});
			textNum.setText(""+(position+1));
	        codeInfo.setTextCodeColor(textCode);
	        textZhuShu.setText(codeInfo.zhuShu+"注");
	        textAmt.setText(codeInfo.amt+"元");
	        if(isDelet){
	        	btnDelet.setVisibility(Button.VISIBLE);
	        }else{
	        	btnDelet.setVisibility(Button.GONE);
	        }
			return convertView;
		}

	}
	class CodeInfo{
		private String touZhuCode;//投注时的注码，传给后台的
		List<String> codes = new ArrayList<String>();
		List<Integer> colors = new ArrayList<Integer>();
		int amt;
		int zhuShu;
		
		public CodeInfo(int amt,int zhuShu){
			this.amt = amt;
			this.zhuShu =zhuShu;
		}
		/**
		 * 
		 * @param beishu
		 * @param amt 单注金额
		 * @return
		 */
		public String getTouZhuCode(int beishu,int amt) {
			return touZhuCode+"_"+PublicMethod.isTen(beishu)+"_"+amt+"_"+zhuShu*amt;
		}
		public String getTouZhuCode() {
			return touZhuCode;
		}
		public void setTouZhuCode(String touZhuCode) {
			this.touZhuCode = touZhuCode;
		}
		public int getAmt() {
			return amt;
		}
		public void setAmt(int amt) {
			this.amt = amt;
		}
		public int getZhuShu() {
			return zhuShu;
		}
		public void setZhuShu(int zhuShu) {
			this.zhuShu = zhuShu;
		}
		public void addAreaCode(String code,int color){
			codes.add(code);
			colors.add(color);
		}
		public List<String> getCodes(){
			return codes;
		}
		public List<Integer> getColors(){
			return colors;
		}
		public void setTextCodeColor(TextView textCode) {
			SpannableStringBuilder builder = new SpannableStringBuilder(); 
			int upLength = 0;
	        for(int i=0;i<getCodes().size();i++){
	        	String code = getCodes().get(i);
	            builder.append(code);
		        builder.setSpan(new ForegroundColorSpan(getColors().get(i)), upLength, code.length()+upLength, Spanned.SPAN_COMPOSING); 
		        if(i!=getCodes().size()-1){
		        	builder.append("|");
		        }
		        upLength = builder.length();
	        }
	        textCode.setText(builder, BufferType.EDITABLE);
		}
	}
}
