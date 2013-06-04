package com.ruyicai.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.present.RuyiExpressFeelDltZixuan;
import com.ruyicai.activity.present.RuyiExpressFeelFc3dZixuan;
import com.ruyicai.activity.present.RuyiExpressFeelPl3Zixuan;
import com.ruyicai.activity.present.RuyiExpressFeelQlcZixuan;
import com.ruyicai.activity.present.RuyiExpressFeelSsqZixuan;
import com.ruyicai.activity.present.RuyiExpressFeelSuccess;


/**
 * 如意传情各个彩种的view
 * @author wangyl 8.11
 *
 */
public class RuyiExpressFeelView extends View{
	
	Spinner spinner;
	String spinner_str;
	CheckBox cb_jixuan;
	CheckBox cb_zixuan;
	Button btn_sure;
	RelativeLayout relativeLayout;
	int isJixuanOrZixuan = 1;// 1为机选，2为自选
	boolean ischeck = true;
	boolean ischeck_jixuan = false;
	boolean ischeck_zixuan = true;
	String successStr;
	String zhushu;
	String lotteryType;
	Context con;
	private static final String[] constrs = { "5", "4", "3", "2", "1" };
	private ArrayAdapter<String> adapter;
	
	public RuyiExpressFeelView(Context context,String str) {
		
		super(context);
		this.successStr = str+"Jixuan";//各个彩种的机选
		this.zhushu = str+"zhushu";//各个彩种机选的注数
		this.lotteryType = str;
		this.con = context;
		
	}
	
	public View getView(){
		LayoutInflater inflate = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflate.inflate(R.layout.ruyichuanqing_layout_sub_view, null);
		{
	
			relativeLayout = (RelativeLayout) layout.findViewById(R.id.ruyichuanqing_layout_zhushu);
			cb_jixuan = (CheckBox) layout.findViewById(R.id.ruyichuanqing_jixuan);
			cb_zixuan = (CheckBox) layout.findViewById(R.id.ruyichuanqing_zixuan);
			spinner = (Spinner) layout.findViewById(R.id.ruyichuanqing_spinner_zhushu);
			btn_sure = (Button) layout.findViewById(R.id.ruyichuanqing_sure);
			cb_jixuan.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
	
						@Override
						public void onCheckedChanged(
								CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								isJixuanOrZixuan = 1;
								ischeck_jixuan = false;
								cb_zixuan.setChecked(false);
								relativeLayout.setVisibility(View.VISIBLE);
							} else {
								ischeck_jixuan = true;
								relativeLayout.setVisibility(View.INVISIBLE);
							}
						}
	
					});
			cb_zixuan.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
	
						@Override
						public void onCheckedChanged(
								CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								isJixuanOrZixuan = 2;
								ischeck_zixuan = false;
								cb_jixuan.setChecked(false);
								relativeLayout.setVisibility(View.INVISIBLE);
							} else {
								ischeck_zixuan = true;
							}
						}
	
					});
	
			spinner = (Spinner) layout.findViewById(R.id.ruyichuanqing_spinner_zhushu);
			spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	
						@Override
						public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
							spinner_str = constrs[position];
							
						}
	
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
	
						}
	
					});
			adapter = new ArrayAdapter<String>(con,android.R.layout.simple_spinner_item, constrs);
			adapter.setDropDownViewResource(R.layout.myspinnerdropdown);
			spinner.setAdapter(adapter);
			btn_sure.setOnClickListener(new Button.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// 机选
					if (isJixuanOrZixuan == 1 && !ischeck_jixuan) {
						Intent intent = new Intent(con,RuyiExpressFeelSuccess.class);
						Bundle Bundle = new Bundle();
						Bundle.putString("success", successStr);
						Bundle.putString(zhushu, spinner_str);
						intent.putExtras(Bundle);
						con.startActivity(intent);
					}
					// 自选
					else if (isJixuanOrZixuan == 2&& !ischeck_zixuan) {
						if("ssq".equalsIgnoreCase(lotteryType)){
							Intent intent_zixuan = new Intent(con, RuyiExpressFeelSsqZixuan.class);
							con.startActivity(intent_zixuan);
						}
						if("fc3d".equalsIgnoreCase(lotteryType)){
							Intent intent_zixuan = new Intent(con, RuyiExpressFeelFc3dZixuan.class);
							con.startActivity(intent_zixuan);
						}
						if("qlc".equalsIgnoreCase(lotteryType)){
							Intent intent_zixuan = new Intent(con, RuyiExpressFeelQlcZixuan.class);
							con.startActivity(intent_zixuan);
						}
						if("dlt".equalsIgnoreCase(lotteryType)){
							Intent intent_zixuan = new Intent(con, RuyiExpressFeelDltZixuan.class);
							con.startActivity(intent_zixuan);
						}
						if("pl3".equalsIgnoreCase(lotteryType)){
							Intent intent_zixuan = new Intent(con, RuyiExpressFeelPl3Zixuan.class);
							con.startActivity(intent_zixuan);
						}
					} else if (ischeck_jixuan && ischeck_zixuan) {
						AlertDialog.Builder builder = new AlertDialog.Builder(con);
						builder.setTitle("请选择赠送方式");
						builder.setMessage("请选择赠送方式");
						// 确定
						builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog,int which) {
	
									}
	
								});
						builder.show();
	
					}
				}
			});
			
		}
		return layout;
	}
}
