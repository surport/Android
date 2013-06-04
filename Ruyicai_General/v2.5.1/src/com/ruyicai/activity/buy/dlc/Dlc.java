/**
 * 
 */
package com.ruyicai.activity.buy.dlc;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.code.dlc.DlcCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.DlcQxBalls;
import com.ruyicai.jixuan.DlcRxBalls;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

/**
 * 
 * 多乐彩（11选5）
 * @author Administrator
 *
 */
public class Dlc extends ZixuanAndJiXuan implements BuyImplement{
	private Thread threed;
	private String type[] = {"R1","R2","R3","R4","R5","R6","R7","R8","Q2","Q3","Z2","Z3"};//类型
	private int nums[] = {1,2,3,4,5,6,7,8,2,3,2,3};//单式机选个数
	private int maxs[] = {6,3,4,7,10,8,9,8,11,11,8,9};//选区最大小球数
	private String titles[] = {"任选一","任选二","任选三","任选四","任选五","任选六","任选七","任选八","选前二","选前三","选前二组选","选前三组选"};//标题组
	public static String state = "";//当前类型
    private int num = 1;//当前单式机选个数
    private int max = 6;//选区最大小球数
    private Spinner typeSpinner;
    private BallTable  oneBallTable;
    private BallTable  twoBallTable;
    private BallTable  thirdBallTable;
    private boolean isJiXuan = false;
	private TextView title;//标题
	private TextView issue;//期号
	private TextView time;//截止时间
	private Button imgRetrun;//返回购彩大厅按钮
	public  static String batchCode;//期号
	private Timer  timer;//计时器
	private int    lesstime;//剩余时间
	private Handler handler = new Handler();
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_dlc_main);
		sscCode = new DlcCode();
		initTop();
		initSpinner();
		initGroup();
		setIssue();
		highttype = "DLC";
	}

	/**
	 * 初始化组件
	 */
	private void initTop(){
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);

	    //ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});		
	}
	/**
	 * 初始化spinner组件
	 */
	public void initSpinner(){
		typeSpinner = (Spinner) findViewById(R.id.buy_dlc_spinner);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int position = typeSpinner.getSelectedItemPosition();
				PublicMethod.myOutLog("========", "===="+position);
			    action(position);
              
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		typeSpinner.setSelection(1);
	}
	/**
	 * 初始化group
	 */
	public void initGroup(){
		if(state.equals("Q2")||state.equals("Q3")){
			childtype= new String[]{"直选","机选"};
		}else if(state.equals("Z2")||state.equals("Z3")){
			childtype= new String[]{"组选","机选"};
		}else{
			childtype= new String[]{"直选","机选"};
		}
		init();
	    group.setOnCheckedChangeListener(this);
	    group.check(0);   
	}
	
	
	public void setIssue(){
		issue.setText("期号获取中....");
		time.setText("获取中...");
		threed = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				String re = "";
				String message="";
					re = GetLotNohighFrequency.getInstance().getInfo(Constants.LOTNO_11_5);
					if (!re.equalsIgnoreCase("")) {
						try {
							JSONObject obj = new JSONObject(re);
							message = obj.getString("message");
					        error_code = obj.getString("error_code");
					        lesstime = Integer.valueOf(obj.getString("time_remaining"));
							batchCode = obj.getString("batchcode");	
							handler.post(new Runnable(){
								public void run() {
								issue.setText("第" + batchCode + "期");
								}});
							TimerTask task = new TimerTask(){
							public void run() {
							handler.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//issue.setText("第" + "00000000" + "期");
									
									time.setText("剩余时间:" + PublicMethod.isTen(lesstime/60)+":"+PublicMethod.isTen(lesstime%60));
									lesstime--;
									if(lesstime==0){
										timer.cancel();timer=null;
									new AlertDialog.Builder(Dlc.this).setTitle("提示").setMessage("11选5第"+batchCode+"期已经结束,是否转入下一期")
									.setNegativeButton("转入下一期", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											setIssue();
										}
									}).setNeutralButton("返回主页面", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
										 Dlc.this.finish();
										}
									}).create().show();
									}
								}
							});							
			             	}};
							timer = new Timer(true);
						    timer.schedule(task, 0, 1000);
						}catch (Exception e) {
							handler.post(new Runnable() {
								public void run() {
									issue.setText("获取期号失败");
						} });
						}
					} else {
						
					}
			}
		});
		threed.start();
	}

	/**
	 * 单选框切换直选，机选
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){		
		case 0:
			isJiXuan = false;
			createViewZx();
		break;
		case 1:	
			isJiXuan = true;
			createViewJx();
		break;	
		}
			
	}
	/**
	 * 初始化自选选区
	 */
	public void createViewZx(){
		iProgressBeishu = 1;iProgressQishu = 1;
		initArea();
		createView(areaInfos, sscCode,this);
	}
	/**
	 * 初始化机选选区
	 */
	public void createViewJx(){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if(state.equals("Q2")||state.equals("Q3")){
			DlcQxBalls dlcb= new DlcQxBalls(num);
			createviewmechine(dlcb,this);
		}else{
			DlcRxBalls dlcb = new DlcRxBalls(num);
			createviewmechine(dlcb,this);
		}
	
	}
	/**
	 * 初始化选区
	 */
	public void initArea() {
		String wantitle = "万位区";
		String qiantitle = "千位区";
		String baititle = "百位区";
		if(state.equals("Q2")){
			areaInfos = new AreaInfo[2];
			areaInfos[0] = new AreaInfo(11, max, BallResId, 0, 1,Color.RED, wantitle);
			areaInfos[1] = new AreaInfo(11, max, BallResId, 0, 1,Color.RED, qiantitle);
		}else if(state.equals("Q3")){
			areaInfos = new AreaInfo[3];
			areaInfos[0] = new AreaInfo(11, max, BallResId, 0, 1,Color.RED, wantitle);
			areaInfos[1] = new AreaInfo(11, max, BallResId, 0, 1,Color.RED, qiantitle);
			areaInfos[2] = new AreaInfo(11, max, BallResId, 0, 1,Color.RED, baititle);
		}else{
			areaInfos = new AreaInfo[1];
	        String title = "请选择投注号码" ;
			areaInfos[0] = new AreaInfo(11, max, BallResId, 0, 1,Color.RED, title);
		}

	}
	/**
	 * spinner处理事件
	 */
	public void action(int position){
	    state = type[position];
        num = nums[position];
        max = maxs[position];
		initGroup();
        title.setText(titles[position]);
	}
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		for(int i=0;i<areaNums.length;i++){
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].info.areaNum;
			if(iBallId<0){
				  if(state.equals("Q2")){
						if(i==0){
							 int isHighLight = areaNums[0].table.changeBallState(areaNums[0].info.chosenBallSum, nBallId);
							 if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[1].table.clearOnBallHighlight(nBallId);
							 }
						}else{
							int isHighLight = areaNums[1].table.changeBallState(areaNums[1].info.chosenBallSum, nBallId);
							if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[0].table.clearOnBallHighlight(nBallId);
							}
						}			  
				  }else if(state.equals("Q3")){
						if(i==0){
							 int isHighLight = areaNums[0].table.changeBallState(areaNums[0].info.chosenBallSum, nBallId);
							 if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[1].table.clearOnBallHighlight(nBallId);
								areaNums[2].table.clearOnBallHighlight(nBallId);
							 }
						}else if(i==1){
							int isHighLight = areaNums[1].table.changeBallState(areaNums[1].info.chosenBallSum, nBallId);
							if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[0].table.clearOnBallHighlight(nBallId);
								areaNums[2].table.clearOnBallHighlight(nBallId);
							}
						}else{
							int isHighLight = areaNums[2].table.changeBallState(areaNums[2].info.chosenBallSum, nBallId);
							if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
								areaNums[0].table.clearOnBallHighlight(nBallId);
								areaNums[1].table.clearOnBallHighlight(nBallId);
							}
						}	
				  }else{
					  areaNums[i].table.changeBallState(areaNums[i].info.chosenBallSum, nBallId);
				  }
				  break;
			}

	     }

	}
	
	 /**
	    * 点击小球提示金额
	    * @param areaNum
	    * @param iProgressBeishu
	    * @return
	    */
	   public String textSumMoney(AreaNum areaNum[],int iProgressBeishu){
		   String textSum = "";
		   int iZhuShu = getZhuShu();
	       if(state.equals("Q2")){//求排序
	    	   int oneNum = areaNum[0].table.getHighlightBallNums();
	    	   int twoNum = areaNum[1].table.getHighlightBallNums();
	    	   if(oneNum==0){
	    		   textSum = "万位还需要1个小球";
	    	   }else if(twoNum==0){
	    		   textSum = "千位还需要1个小球";
	    	   }else{
				   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
	    	   }
		   }else if(state.equals("Q3")){
			   int oneNum = areaNum[0].table.getHighlightBallNums();
			   int twoNum = areaNum[1].table.getHighlightBallNums();
			   int thirdNum = areaNum[2].table.getHighlightBallNums();
			   if(oneNum==0){
	    		   textSum = "万位还需要1个小球";
	    	   }else if(twoNum==0){
	    		   textSum = "千位还需要1个小球";
	    	   }else if(thirdNum==0){
	    		   textSum = "百位还需要1个小球";
	    	   }else{
				   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
	    	   }
		   }else{//求组合	
			   int ballNums = areaNum[0].table.getHighlightBallNums();
			   int oneNum = num - ballNums;
			   if(oneNum>0){
				   textSum = "还需要"+oneNum+"个小球";
			   }else{
				   textSum = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			   }
			   
		   }
		return textSum ;
		   
	   };
	   /**
	    * 判断是否满足投注条件
	    */
	   public void isTouzhu(){
		  if(isJiXuan){
				if (balls.size() == 0) {
					alertInfo("请至少选择1注彩票");
				} else {
					String sTouzhuAlert = "";
					sTouzhuAlert = getTouzhuAlertJixuan();
					alertJX(sTouzhuAlert);
				}
		  }else{
			  int iZhuShu = getZhuShu();
		      if(state.equals("Q2")){

		    	   if(iZhuShu==0){
		    		    alertInfo("请在万位和千位至少选择一个球，再进行投注！");
		    	   }else if(iZhuShu * 2 > 20000) {
						dialogExcessive();
				   }else{
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlert();
						alertZX(sTouzhuAlert);
				   }
		      }else if(state.equals("Q3")){
		    	   if(iZhuShu==0){
		    		   alertInfo("请在万位、千位和百位至少选择一个球，再进行投注！");
		    	   }else if(iZhuShu * 2 > 20000) {
						dialogExcessive();
				   }else{
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlert();
						alertZX(sTouzhuAlert);
				   }
		      }else{  
		   	       int ballNums = areaNums[0].table.getHighlightBallNums();
				   int oneNum = num - ballNums;
				   if(oneNum>0){
					   alertInfo("请再选择"+oneNum+"球，再进行投注！");
				   }else if(iZhuShu * 2 > 20000) {
						dialogExcessive();
				   }else{
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlert();
						alertZX(sTouzhuAlert);
				   }
		      }
		  }
	   }
	   /**
	    * 投注提示信息
	    * @return
	    */
	   private String getZhuma(){
		   String zhuma="";
		   if(isJiXuan){
			zhuma = DlcRxBalls.getZhuma(balls, state);
		   }else {
		    zhuma = DlcCode.zhuma(areaNums, state);
		   }
		   return zhuma;
	   }
	   
	   
	   
	   private String getTouzhuAlert(){
		int iZhuShu = getZhuShu();
		String alertStr="";
		String zhuma = "";
		if(state.equals("Q2")){
			int[] one = areaNums[0].table.getHighlightBallNOs();
			int[] two = areaNums[1].table.getHighlightBallNOs();
			zhuma = "万位："+ PublicMethod.getStrZhuMa(one)
			+"\n千位："+ PublicMethod.getStrZhuMa(two);
			alertStr = alertStr(zhuma);
		}else if(state.equals("Q3")){
			int[] one = areaNums[0].table.getHighlightBallNOs();
			int[] two = areaNums[1].table.getHighlightBallNOs();
			int[] third = areaNums[2].table.getHighlightBallNOs();
			zhuma = "万位："+ PublicMethod.getStrZhuMa(one)
			+"\n千位："+ PublicMethod.getStrZhuMa(two)
			+"\n百位："+ PublicMethod.getStrZhuMa(third);
			alertStr=alertStr(zhuma);
		}else{
			int[] one = areaNums[0].table.getHighlightBallNOs();
			zhuma = PublicMethod.getStrZhuMa(one);
			alertStr= alertStr(zhuma);
		}
		codeStr = "注码：\n"+zhuma+"\n";
		return alertStr;   
	   }
	   public String alertStr(String zhuma){
			int iZhuShu = getZhuShu();
		return "第" +Dlc.batchCode + "期\n" 
		       + "注数："
		       + iZhuShu / mSeekBarBeishu.getProgress()+ "注"+ "\n"
		       + "倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
		       + mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
		       + (iZhuShu * 2) + "元" + "\n" + "冻结金额："
		       + (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+ "\n" 
		       +"注码：" + "\n" 
		       + zhuma
		       + "\n" + "确认支付吗？";
	   }
	   /**
	    * 获得总注数
	    * @return
	    */
	   public int getZhuShu(){
		   int zhushu = 0;
		   if(isJiXuan){
			   zhushu = balls.size()*iProgressBeishu;
//			   Log.v("Ballsize", balls.size()+"");
//			   Log.v("iProgressBeishu", iProgressBeishu+"");
		   }else {
			if(state.equals("Q2")){
		     	   int oneNum = areaNums[0].table.getHighlightBallNums();
		    	   int twoNum = areaNums[1].table.getHighlightBallNums();
		    	   zhushu = oneNum*twoNum*iProgressBeishu;
			}else if(state.equals("Q3")){
				   int oneNum = areaNums[0].table.getHighlightBallNums();
				   int twoNum = areaNums[1].table.getHighlightBallNums();
				   int thirdNum = areaNums[2].table.getHighlightBallNums();
		    	   zhushu = oneNum*twoNum*thirdNum*iProgressBeishu;
			}else{
				   int ballNums = areaNums[0].table.getHighlightBallNums();
				   zhushu = (int) PublicMethod.zuhe(num,ballNums)*iProgressBeishu;
			}
		   }
		return zhushu;
	   }
	   /**
	    * 投注联网
	    */
	   public void touzhuNet(){
			int zhuShu=getZhuShu();
			if(isJiXuan){
			betAndGift.setSellway("1")	;
			}else{
			betAndGift.setSellway("0");}//1代表机选 0代表自选
			betAndGift.setLotno(Constants.LOTNO_11_5);
			betAndGift.setBet_code(getZhuma());
			betAndGift.setAmount(""+zhuShu*200);
//			Log.v("zhushu", zhuShu+"");
		   
	   }

		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();

		}

		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
	       
		}

		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		
		}

		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
		}   
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
//			Log.v("DLC", "onDestroy");
			 if(timer!=null){
				 timer.cancel();
			 }
		}
}
