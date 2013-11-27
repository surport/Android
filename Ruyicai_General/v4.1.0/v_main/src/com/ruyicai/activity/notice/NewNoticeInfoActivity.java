package com.ruyicai.activity.notice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.activity.notice.LotnoDetail.DLTDetailView;
import com.ruyicai.activity.notice.LotnoDetail.FC3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.LotnoDetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL5DetailView;
import com.ruyicai.activity.notice.LotnoDetail.QLCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.QXCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.SsqDetailView;
import com.ruyicai.activity.notice.LotnoDetail.TwentyDetailView;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv1.OAuthV1Client;
import com.tencent.weibo.webview.OAuthV1AuthorizeWebView;
import com.third.tencent.TencentShareActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
/**
 * 最新开奖页面
 *
 */
public class NewNoticeInfoActivity extends Activity {
	Activity context;
	public ProgressDialog progress;
	String tencent_token;
	String tencent_access_token_secret;
	private OAuthV1 tenoAuth;
	RWSharedPreferences shellRW;
	LotnoDetailView lotnoDetailView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progress = UserCenterDialog.onCreateDialog(this);
		context = this;
		shellRW = new RWSharedPreferences(context, "addInfo");
		initDialog(NoticeActivityGroup.LOTNO);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		lotnoDetailView.initfenxianglayout();
	}

	/**
	 * @param listViewID
	 *            列表ID
	 */
	private void initDialog(int listViewID) {
		switch (listViewID) {

		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			lotnoDetailView = new SsqDetailView(context,
					Constants.LOTNO_SSQ, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_SSQ);
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			lotnoDetailView = new FC3DetailView(context,
					Constants.LOTNO_FC3D, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_FC3D);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			lotnoDetailView = new QLCDetailView(context,
					Constants.LOTNO_QLC, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_QLC);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			lotnoDetailView = new PL3DetailView(context,
					Constants.LOTNO_PL3, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_PL3);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			lotnoDetailView = new DLTDetailView(context,
					Constants.LOTNO_DLT, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_DLT);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			lotnoDetailView = new PL5DetailView(context,
					Constants.LOTNO_PL5, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_PL5);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			lotnoDetailView = new QXCDetailView(context,
					Constants.LOTNO_QXC, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_QXC);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			lotnoDetailView = new TwentyDetailView(context,
					Constants.LOTNO_22_5, NoticeActivityGroup.ISSUE, progress,
					new Handler(), false);
			getSubInfoForListView(Constants.LOTNO_22_5);
			break;
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:

		}
	}

	private static JSONObject getJSONByLotno(String lotnoString,
			String maxresultString) {
		JSONObject jsonObjectByLotno = PrizeInfoInterface.getInstance()
				.getNoticePrizeInfo(lotnoString, "1", maxresultString);
		return jsonObjectByLotno;
	}

	/**
	 * 子列表中相应的数据
	 */
	private void getSubInfoForListView(String lotno) {
		if (lotno.equals(Constants.LOTNO_GD115)) {

			// 广东11-5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_GD115, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.gd115List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						Constants.gd115List.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.gd115List == null
						|| Constants.gd115List.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.gd115List.add(tempObj);
				}
			}

		} else if (lotno.equals(Constants.LOTNO_SSQ)) {
			// 双色球信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SSQ, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.ssqNoticeList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						Constants.ssqNoticeList.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.ssqNoticeList == null
						|| Constants.ssqNoticeList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.ssqNoticeList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_DLT)) {
			// 大乐透信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_DLT, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.dltList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.dltList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取大乐透数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.dltList == null || Constants.dltList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.dltList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_FC3D)) {
			// 福彩3D信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_FC3D, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.fc3dList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.fc3dList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取福彩3D数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.fc3dList == null
						|| Constants.fc3dList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.fc3dList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_PL3)) {
			// 排列3信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_PL3, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.pl3List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.pl3List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取排列3数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.pl3List == null || Constants.pl3List.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.pl3List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_PL5)) {
			// 排列5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_PL5, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.pl5List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.pl5List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取排列5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.pl5List == null || Constants.pl5List.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.pl5List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_QXC)) {
			// 七星彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_QXC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.qxcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.qxcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取七星彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.qxcList == null || Constants.qxcList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.qxcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_QLC)) {
			// 七乐彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_QLC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.qlcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.qlcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取七乐彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.qlcList == null || Constants.qlcList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.qlcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_SSC)) {
			// 时时彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SSC, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.sscList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.sscList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取时时彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.sscList == null || Constants.sscList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.sscList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_SFC)) {
			// 足彩胜负彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SFC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.sfcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.sfcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩胜负彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.sfcList == null || Constants.sfcList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.sfcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_RX9)) {
			// 足彩任选9信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_RX9, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.rx9List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.rx9List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩任选9数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.rx9List == null || Constants.rx9List.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.rx9List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_LCB)) {
			// 足彩六场半信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_LCB, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.half6List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.half6List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩六场半数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.half6List == null
						|| Constants.half6List.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.half6List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_JQC)) {
			// 进球彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_JQC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.jqcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.jqcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取进球彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.jqcList == null || Constants.jqcList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.jqcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_11_5)) {
			// 11_5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_11_5, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.dlcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.dlcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取11_5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.dlcList == null || Constants.dlcList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.dlcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_eleven)) {
			// 11运夺金信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_eleven, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.ydjList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.ydjList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取11运夺金数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.ydjList == null || Constants.ydjList.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.ydjList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_22_5)) {
			// 22_5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_DLT, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					Constants.twentylist.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						Constants.twentylist.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取22_5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (Constants.twentylist == null
						|| Constants.twentylist.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					Constants.twentylist.add(tempObj);
				}
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == OAuthV1AuthorizeWebView.RESULT_CODE) {
				// 从返回的Intent中获取验证码
				tenoAuth = (OAuthV1) data.getExtras().getSerializable("oauth");
				try {
					tenoAuth = OAuthV1Client.accessToken(tenoAuth);
					/*
					 * 注意：此时oauth中的Oauth_token和Oauth_token_secret将发生变化，用新获取到的
					 * 已授权的access_token和access_token_secret替换之前存储的未授权的request_token
					 * 和request_token_secret.
					 */
					tencent_token = tenoAuth.getOauthToken();
					tencent_access_token_secret = tenoAuth
							.getOauthTokenSecret();
					shellRW.putStringValue("tencent_token", tencent_token);
					shellRW.putStringValue("tencent_access_token_secret",
							tencent_access_token_secret);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(NewNoticeInfoActivity.this,
						TencentShareActivity.class);
				intent.putExtra("tencent", lotnoDetailView.getShareString());
				intent.putExtra("oauth", tenoAuth);
				startActivity(intent);
			}
		}

	}
}
