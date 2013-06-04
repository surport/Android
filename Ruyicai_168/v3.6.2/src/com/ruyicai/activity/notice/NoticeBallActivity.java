package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Administrator
 *
 */
public class NoticeBallActivity extends Activity{
	LinearLayout layout;
	NoticeBallView ballRedView;
	NoticeBallView ballBlueView;
	private boolean isRed;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_ball_main);
		layout = (LinearLayout) findViewById(R.id.notice_ball_main_linear);
		
	}
	/**
	 * 联网获取所有彩种开奖信息
	 */
	public void noticeAllNet(final boolean isRed){
		if(NoticeMainActivity.isFirstNotice){
		final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
		dialog.show();
		final Handler handler =  new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
//				JSONObject lotteryInfos = NoticeInterface.getInstance().getLotteryAllNotice();//开奖信息json对象
				//将获取到的开奖信息放到常量类中
//				NoticeActivityGroup.analysisLotteryNoticeJsonObject(lotteryInfos);
				dialog.cancel();
				handler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						addBallView(isRed);
					}
				});
			}
			
		}).start();
		}else{
			addBallView(isRed);
		}
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * @param listViewID     列表ID
	 */
	public void addBallView(boolean isRed) {
		this.isRed = isRed;
		List<JSONObject> list;
		switch (NoticeActivityGroup.LOTNO) {
		//广东11-5
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_GD115);
				ballRedView.initNoticeBall(list.size(),11, 1, list,true,"gd11-5",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			
			break;
		//广东快乐十分
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_ten);
			ballRedView.initNoticeBall(list.size(),18, 1, list,false,"gd-10",1*NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(),2, 19, list,true,"gd-10",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			
			break;
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			
			list = getSubInfoForListView(Constants.LOTNO_SSQ);
				ballRedView.initNoticeBall(list.size(),33, 1, list,true,"ssq",1*NoticeMainActivity.SCALE);
				ballBlueView.initNoticeBall(list.size(),16, 1, list,false,"ssq",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_FC3D);
			ballRedView.initNoticeBall(list.size(),10, 0, list,isRed,"fc3d",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_QLC);
				ballRedView.initNoticeBall(list.size(),30, 1, list,true,"qlc",1*NoticeMainActivity.SCALE);
				ballBlueView.initNoticeBall(list.size(),30, 1, list,false,"qlc",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			// zlm 排列三
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_PL3);
			ballRedView.initNoticeBall(list.size(),10, 0, list,isRed,"pl3",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			// zlm 排列五
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_PL5);
			ballRedView.initNoticeBall(list.size(),10, 0, list,isRed,"pl5",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			// zlm 七星彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_QXC);
			ballRedView.initNoticeBall(list.size(),10, 0, list,isRed,"qxc",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			// zlm 超级大乐透
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_DLT);
			ballRedView.initNoticeBall(list.size(),35, 1, list,true,"cjdlt",1*NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(),12, 1, list,false,"cjdlt",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			// zlm 时时彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_SSC);
			ballRedView.initNoticeBall(list.size(),10, 0, list,isRed,"ssc",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_11_5);
			ballRedView.initNoticeBall(list.size(),11, 1, list,isRed,"11-5",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_eleven);
			ballRedView.initNoticeBall(list.size(),11, 1, list,isRed,"11-ydj",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_22_5);
			ballRedView.initNoticeBall(list.size(),22, 1, list,isRed,"22-5",1*NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			break;
		case NoticeActivityGroup.ID_SUB_SFC_LISTVIEW:
			// zlm 胜负彩
		
			break;
		case NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW:
			// zlm 任选九

			break;
		case NoticeActivityGroup.ID_SUB_LCB_LISTVIEW:
			// zlm 六场半

			break;
		case NoticeActivityGroup.ID_SUB_JQC_LISTVIEW:
			// zlm 进球彩
	
			break;
		}
	}
	/**
	 * 子列表中相应的数据
	 */
	protected static List<JSONObject> getSubInfoForListView(String lotno) {
		
		if(lotno == null||lotno.equals("")){
			return null;
		}
		List<JSONObject> _list = new ArrayList<JSONObject>();
		if (lotno.equals(Constants.LOTNO_GD115)) {
			//广东11-5信息获取
			try {
				JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_GD115,"50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
			    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
			    	NoticeMainActivity.isFirstNotice = false;
			    	_list.clear();
			    	Constants.gd115List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.gd115List.add(_ssq);
					}
				}
			}catch(Exception e){
				//获取双色球数据出现异常
				e.printStackTrace();
			}finally{
				//判断是否已经从网络上获取到了数据
				if(_list==null||_list.size()==0){
					//没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {
							
						}
					}
					_list.add(tempObj);
					Constants.gd115List.add(tempObj);
				}
			}
		
		}else if (lotno.equals(Constants.LOTNO_ten)) {
			//广东快乐十分
			try {
				JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_ten,"50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
			    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
			    	NoticeMainActivity.isFirstNotice = false;
			    	_list.clear();
			    	Constants.gd10List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.gd10List.add(_ssq);
					}
				}
			}catch(Exception e){
				//获取双色球数据出现异常
				e.printStackTrace();
			}finally{
				//判断是否已经从网络上获取到了数据
				if(_list==null||_list.size()==0){
					//没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "0000000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {
							
						}
					}
					_list.add(tempObj);
					Constants.gd10List.add(tempObj);
				}
			}
		
		}
		else if(lotno.equals(Constants.LOTNO_SSQ)){
			//双色球信息获取
			try {
				JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_SSQ,"50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
			    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
			    	NoticeMainActivity.isFirstNotice = false;
			    	_list.clear();
			    	Constants.ssqNoticeList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.ssqNoticeList.add(_ssq);
					}
				}
			}catch(Exception e){
				//获取双色球数据出现异常
				e.printStackTrace();
			}finally{
				//判断是否已经从网络上获取到了数据
				if(_list==null||_list.size()==0){
					//没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {
							
						}
					}
					_list.add(tempObj);
					Constants.ssqNoticeList.add(tempObj);
				}
			}
		}else if(lotno.equals(Constants.LOTNO_DLT)){
				//大乐透信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_DLT,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.dltList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.dltList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取大乐透数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.dltList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_FC3D)){
				//福彩3D信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_FC3D,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.fc3dList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.fc3dList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取福彩3D数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
								tempObj.put("tryCode", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.fc3dList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_PL3)){
				//排列3信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_PL3,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.pl3List.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.pl3List.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取排列3数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.pl3List.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_PL5)){
				//排列5信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_PL5,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.pl5List.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.pl5List.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取排列5数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.pl5List.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_QXC)){
				//七星彩信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_QXC,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.qxcList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.qxcList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取七星彩数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.qxcList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_QLC)){
				//七乐彩信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_QLC,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.qlcList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.qlcList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取七乐彩数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.qlcList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_SSC)){
				//时时彩信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_SSC,"100");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.sscList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.sscList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取时时彩数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.sscList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_SFC)){
				//足彩胜负彩信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_SFC,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.sfcList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.sfcList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取足彩胜负彩数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.sfcList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_RX9)){
				//足彩任选9信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_RX9,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.rx9List.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.rx9List.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取足彩任选9数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.rx9List.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_LCB)){
				//足彩六场半信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_LCB,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.half6List.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.half6List.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取足彩六场半数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.half6List.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_JQC)){
				//进球彩信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_JQC,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.jqcList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.jqcList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取进球彩数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.jqcList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_11_5)){
				//11_5信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_11_5,"100");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.dlcList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.dlcList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取11_5数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.dlcList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_eleven)){
				//11运夺金信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_eleven,"100");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.ydjList.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.ydjList.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取11运夺金数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.ydjList.add(tempObj);
					}
				}
		}else if(lotno.equals(Constants.LOTNO_22_5)){
				//22_5信息获取
				try {
					JSONObject jsonObjectByLotno =getJSONByLotno(Constants.LOTNO_22_5,"50");
					JSONArray jsonArrayByLotno = jsonObjectByLotno.getJSONArray("result");
				    if(jsonArrayByLotno!=null&&jsonArrayByLotno.length()>0){
				    	NoticeMainActivity.isFirstNotice = false;
				    	_list.clear();
				    	Constants.twentylist.clear();
						for (int i = 0; i < jsonArrayByLotno.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArrayByLotno.get(i);
							_list.add(jsonObject);
							Constants.twentylist.add(jsonObject);
						}
					}
				}catch(Exception e){
					//获取22_5数据出现异常
					e.printStackTrace();
				}finally{
					//判断是否已经从网络上获取到了数据
					if(_list==null||_list.size()==0){
						//没数据,初始化点数居
						JSONObject tempObj = new JSONObject();
						for (int i = 0; i < 5; i++) {
							try {
								tempObj.put("lotno", "");
								tempObj.put("winno", "00000000000000");
								tempObj.put("date", "");
							} catch (JSONException e) {
								
							}
						}
						_list.add(tempObj);
						Constants.twentylist.add(tempObj);
					}
				}
			}	
		return _list;
	}
	private static JSONObject getJSONByLotno(String lotnoString , String maxresultString){
		JSONObject  jsonObjectByLotno =  PrizeInfoInterface.getInstance().getNoticePrizeInfo(lotnoString, "1", maxresultString);
		return jsonObjectByLotno;
	}

	/**
	 * 将获取到的开奖信息放到常量类中
	 */
	protected void onStart() {
		super.onStart();
	}	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
	}
	protected void onStop() {
		super.onStop();
	}
}