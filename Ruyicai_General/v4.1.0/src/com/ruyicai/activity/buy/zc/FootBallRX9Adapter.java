package com.ruyicai.activity.buy.zc;

import java.util.ArrayList;
import java.util.List;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FootBallRX9Adapter extends FootBallSFAdapter{
	
	public FootBallRX9Adapter(Context context) {
		super(context);
	}
	
	public FootBallRX9Adapter(Context context, List<TeamInfo> list) {
		super(context);
		this.mTeamList = list;
	}
	
	@Override
	public int getCount() {
		if (mTeamList == null) {
			return 0;
		}
		return mTeamList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final TeamInfo info = mTeamList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_listview_item_other, null);
			holder = new ViewHolder();
			holder.divider = (View) convertView
					.findViewById(R.id.jc_main_divider_up);
			holder.gameName = (TextView) convertView
					.findViewById(R.id.game_name);
			holder.gameNum = (TextView) convertView.findViewById(R.id.game_num);
			holder.gameDate = (TextView) convertView
					.findViewById(R.id.game_date);
			holder.gameTime = (TextView) convertView.findViewById(R.id.game_time);
			holder.gameTime.setVisibility(View.VISIBLE);
			holder.homeTeam = (TextView) convertView
					.findViewById(R.id.home_team_name);
			holder.homeOdds = (TextView) convertView
					.findViewById(R.id.home_team_odds);
			holder.textVS = (TextView) convertView
					.findViewById(R.id.game_vs);
			holder.textOdds = (TextView) convertView
					.findViewById(R.id.game_vs_odds);
			holder.guestTeam = (TextView) convertView
					.findViewById(R.id.guest_team_name);
			holder.guestOdds = (TextView) convertView
					.findViewById(R.id.guest_team_odds);
			holder.analysis = (TextView) convertView
					.findViewById(R.id.game_analysis);
			holder.btnDan = (Button) convertView
					.findViewById(R.id.game_dan);
			holder.homeLayout = (LinearLayout) convertView
					.findViewById(R.id.home_layout);
			holder.vsLayout = (LinearLayout) convertView
					.findViewById(R.id.vs_layout);
			holder.guestLayout = (LinearLayout) convertView
					.findViewById(R.id.guest_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ViewHolder copyHolder = holder;
		if (position == 0) {
			copyHolder.divider.setVisibility(View.VISIBLE);
		} else {
			copyHolder.divider.setVisibility(View.GONE);
		}
		copyHolder.gameName.setText(info.getLeagueName());
		String num = info.getTeamId();
		String date =PublicMethod.getTime(info.getDate());
		String time = PublicMethod.getEndTime(info.getDate()) + "(赛)";
		
//		String tiem = info.getTeamId() + "\n"
//				+ PublicMethod.getTime(info.getDate()) + "\n"
//				+ PublicMethod.getEndTime(info.getDate()) + " (赛)";
		copyHolder.gameNum.setText(num);
		copyHolder.gameDate.setText(date);
		copyHolder.gameTime.setText(time);
		copyHolder.homeTeam.setText(info.getHomeTeam());
		copyHolder.homeOdds.setText(info.getHomeOdds());
		copyHolder.textVS.setText("VS");
		copyHolder.textOdds.setText(info.getVsOdds());
		copyHolder.guestTeam.setText(info.getGuestTeam());
		copyHolder.guestOdds.setText(info.getGuestOdds());
		
		setViewStyle(copyHolder.homeLayout, copyHolder.homeTeam,
				copyHolder.homeOdds, info.isWin());
		
		setViewStyle(copyHolder.guestLayout, copyHolder.guestTeam,
				copyHolder.guestOdds, info.isFail());
		
		setViewStyle(copyHolder.vsLayout, copyHolder.textVS,
				copyHolder.textOdds, info.isLevel());
		
		if (info.isDan()) {
			copyHolder.btnDan.setBackgroundResource(R.drawable.jc_btn_b);
			copyHolder.btnDan.setTextColor(white);
		} else {
			copyHolder.btnDan.setBackgroundResource(android.R.color.transparent);
			copyHolder.btnDan.setTextColor(black);
		}
		if (!"5".equals(mIssueState)) {
			copyHolder.homeLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					info.setWin(!info.isWin());
					setViewStyle(copyHolder.homeLayout, copyHolder.homeTeam,
							copyHolder.homeOdds, info.isWin());
					int teamNum = getTeamNum(mTeamList);
					setClickNum(info.isWin(), info, teamNum);
					setDanState(info, copyHolder.btnDan, teamNum);
				}
			});

			copyHolder.guestLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					info.setFail(!info.isFail());
					setViewStyle(copyHolder.guestLayout, copyHolder.guestTeam,
							copyHolder.guestOdds, info.isFail());
					int teamNum = getTeamNum(mTeamList);
					setClickNum(info.isFail(), info, teamNum);
					setDanState(info, copyHolder.btnDan, teamNum);
				}
			});

			copyHolder.vsLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					info.setLevel(!info.isLevel());
					setViewStyle(copyHolder.vsLayout, copyHolder.textVS,
							copyHolder.textOdds, info.isLevel());
					int teamNum = getTeamNum(mTeamList);
					setClickNum(info.isLevel(), info, teamNum);
					setDanState(info, copyHolder.btnDan, teamNum);
				}
			});
			copyHolder.btnDan.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (getTeamNum(mTeamList) > 9) {
						if (info.isSelected() && getDanNums() < 8) {
							info.setDan(!info.isDan());
							if (info.isDan()) {
								copyHolder.btnDan.setBackgroundResource(R.drawable.jc_btn_b);
								copyHolder.btnDan.setTextColor(white);
								if (mContext instanceof FootBallMainActivity) {
									FootBallMainActivity activity = (FootBallMainActivity)mContext;
									activity.changeTextSumMoney(getZhuShu());
								}
							} else {
								copyHolder.btnDan.setBackgroundResource(android.R.color.transparent);
								copyHolder.btnDan.setTextColor(black);
							}
						} else if (getDanNums() == 8 && info.isDan()) {
							info.setDan(false);
							copyHolder.btnDan.setBackgroundResource(android.R.color.transparent);
							copyHolder.btnDan.setTextColor(black);
						} else {
							Toast.makeText(mContext, "最多只能设8个胆",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(mContext, "您不是复式投注，无法设胆",
								Toast.LENGTH_SHORT).show();
					}
					
				}
			});
		}
		
		copyHolder.analysis.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				turnAnalysis(Constants.LOTNO_RX9, info.getTeamId());
			}
		});
		return convertView;
	}
	
	public void clearSelected() {
		for (int i = 0; i < mTeamList.size(); i++) {
			mTeamList.get(i).reSet();
		}
		notifyDataSetChanged();
	}
	
	private void setDanState(TeamInfo info, Button btnDan, int count) {
		if (info.onClickNum == 0) {
			if (count > 9) {
				if (info.isDan()) {
					info.setDan(false);
					btnDan.setBackgroundResource(android.R.color.transparent);
					btnDan.setTextColor(black);
				}
			} else {
				for (int i = 0; i < mTeamList.size(); i++) {
					TeamInfo inInfo = mTeamList.get(i);
					if (inInfo.isDan()) {
						inInfo.setDan(false);
					}
				}
				notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * 获取选取胆球数目
	 * 
	 * @return 胆球数目
	 */
	private int getDanNums() {
		int count = 0;
		for (int i = 0; i < mTeamList.size(); i++) {
			if (mTeamList.get(i).isDan()) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	protected int getTeamNum(List<TeamInfo> list) {
		int teamNum = 0;
		for (int i = 0; i < list.size(); i++) {
			TeamInfo info = list.get(i);
			if (info.isSelected()) {
				teamNum++;
			}
		}
		return teamNum;
	}
	
	/**
	 * 计算任选九胆投投注注数
	 * 
	 * @return 投注注数
	 */
	public int getZhuShu() {
		int betNums = 0;
		int gameNums = mTeamList.size();
		List<Integer> danGamesList = new ArrayList<Integer>();
		List<Integer> notDanGamesList = new ArrayList<Integer>();

		for (int i = 0; i < gameNums; i++) {
			TeamInfo info = mTeamList.get(i);
			if (info.isDan()) {
				danGamesList.add(info.onClickNum);
			} else if (info.isSelected()) {
				notDanGamesList.add(info.onClickNum);
			}
		}
		if (danGamesList.size() + notDanGamesList.size() < 9) {
			return 0;
		} else {
			betNums = caculateZhuShu(
					listToInts(danGamesList),
					getGamesZhuHe(listToInts(notDanGamesList),
							9 - danGamesList.size()));
		}
		return betNums;
	}

	private int[] listToInts(List<Integer> list) {
		int[] result = new int[list.size()];

		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * 计算任九胆投注数：注数=（胆投数组元素1*元素2...）*（（非胆投数组1元素1*元素2...）+ （非胆投数组2元素1*元素2）+...）
	 * 
	 * @param danGames
	 *            胆投场次数组
	 * @param notDanGames
	 *            非胆投场次组合数组
	 * @return
	 */
	private static int caculateZhuShu(int[] danGames, int[][] notDanGames) {
		// 计算胆部组合数
		int danBetNums = 1;
		for (int dan_i = 0; dan_i < danGames.length; dan_i++) {
			danBetNums = danBetNums * danGames[dan_i];
		}

		// 计算非胆部组合数
		int noDanBetNums = 0;
		for (int nodan_i = 0; nodan_i < notDanGames.length; nodan_i++) {
			int[] notDanGame = notDanGames[nodan_i];
			int noDanBetNum = 1;

			for (int nodan_j = 0; nodan_j < notDanGame.length; nodan_j++) {
				noDanBetNum = noDanBetNum * notDanGame[nodan_j];
			}

			noDanBetNums = noDanBetNums + noDanBetNum;
		}

		// 返回注数
		return danBetNums * noDanBetNums;
	}

	/**
	 * 从数组sourceInt中取出num个组合：规定前num-1为，最后一位去子组合的分组和，然后将固定值和子组合合并 如：{1,2,3}取2位组合：
	 * 第1步：固定第1为1，取子组合{2，3}的2-1位
	 * 第2步：2-1=1位，无需固定为，则划分子组合为一位子组合{{2}{3}}；否则继续固定第n为，去子组合m-n为；
	 * 第3步：将一位子组合于前一位固定值合并，为{2,1}{3,1}；若还有固定值，将合并组合和前固定值组合，至组合完毕
	 * 
	 * @param sourceInt
	 *            源数组
	 * @param num
	 *            子组合大小
	 * @return 子组合数据
	 */
	public static int[][] getGamesZhuHe(int[] sourceInt, int num) {
		if (num <= sourceInt.length) {
			// 计算结果组合的大小，更初始化结果组合
			int resultSize = zuHe(sourceInt.length, num);
			int[][] resultInts = new int[resultSize][];

			// 从第fixed_i开始固定，去子组合的num-1位组合。
			// 如{1,2,3,4}中去2位组合，则先固定第1位1，然后去{2,3,4}的(2-1)位子组合
			for (int fixed_i = 0; fixed_i <= sourceInt.length - num; fixed_i++) {
				// 如果取1位子组合，递归返回。
				// 如去{2,3,4}一位组合，则返回{{2},{3},{4}}
				if (num == 1) {
					for (int source_i = 0; source_i < sourceInt.length; source_i++) {
						int[] subInt = new int[num];
						subInt[0] = sourceInt[source_i];
						resultInts[source_i] = subInt;
					}
				} else {
					// 取第fixed_i位为固定值。
					// 如第1次循环，固定值为1...依次类推
					int fixedInt = sourceInt[fixed_i];
					// 获取固定值后的子组合。
					// 第1次循环，子组合为{2,3,4}..依次类推
					int[] nextInt = subIntByIndex(sourceInt, fixed_i);

					// 递归获取{2,3,4}组合中的2-1位子组合集合
					// 第一次返回{{2}，{3}，{4}}...依次类推
					int[][] tempInts = getGamesZhuHe(nextInt, num - 1);

					// 将返回的子组合与固定值组合
					// 如第一次返回{{2},{3},{4}}与固定值1组合{{2,1},{3,1},{4,1}}...依次类推
					for (int temp_i = 0; temp_i < tempInts.length; temp_i++) {
						int[] tempInt = new int[tempInts[temp_i].length + 1];
						tempInt = addInt(tempInt, tempInts[temp_i]);
						tempInt[tempInt.length - 1] = fixedInt;

						// 将子组合集合加入结果组合中
						int size = getIntsSize(resultInts);
						resultInts[size] = tempInt;
					}

				}
			}
			return resultInts;
		} else {
			System.out.println("num error!!!");
			return null;
		}
	}

	/**
	 * 获取数组的大小
	 * 
	 * @param resultInts
	 *            数组对象
	 * @return 数组大小
	 */
	private static int getIntsSize(int[][] resultInts) {
		int size = 0;

		for (int i = 0; i < resultInts.length; i++) {
			if (resultInts[i] != null) {
				size++;
			}
		}
		return size;
	}

	/**
	 * 将sourceInt数组，添加到destinationInt数组中，从第一位开始填充
	 * 
	 * @param resultInt
	 *            结果数组
	 * @param sourceInt
	 *            源数组
	 * @return 结果数组
	 */
	private static int[] addInt(int[] resultInt, int[] sourceInt) {
		for (int source_i = 0; source_i < sourceInt.length; source_i++) {
			resultInt[source_i] = sourceInt[source_i];
		}

		return resultInt;
	}

	/**
	 * 截取数组的子数组，从start位至末尾
	 * 
	 * @param sourceInt
	 *            源数组
	 * @param start
	 *            起始位
	 * @return
	 */
	private static int[] subIntByIndex(int[] sourceInt, int start) {
		int[] resultInt = new int[sourceInt.length - start - 1];

		for (int i = start + 1, j = 0; i < sourceInt.length; i++, j++) {
			resultInt[j] = sourceInt[i];
		}

		return resultInt;
	}

	/**
	 * 求a取b的组合数
	 */
	private static int zuHe(int a, int b) {
		int up = 1;
		for (int up_i = 0; up_i < b; up_i++) {
			up = up * a;
			a--;
		}

		int down = jieCheng(b);

		return up / down;
	}

	/**
	 * 求b的阶乘
	 */
	private static int jieCheng(int b) {
		int result = 0;

		if (b == 1 || b == 0) {
			result = b;
		} else {
			result = b * jieCheng(b - 1);
		}

		return result;
	}
	
	@Override
	public String getZhuMa() {
		String zhuMaString = null;
		StringBuffer danMaString = new StringBuffer();
		StringBuffer tuoMaString = new StringBuffer();

		Boolean isDantou = false;
		for (int i = 0; i < mTeamList.size(); i++) {
			TeamInfo info = mTeamList.get(i);
			if (info.isSelected()) {
				if (info.isDan()) {
					isDantou = true;
					danMaString.append(info.getSelectedTeamString());
					tuoMaString.append("#");
				} else {
					tuoMaString.append(info.getSelectedTeamString());
					danMaString.append("#");
				}
			} else {
				tuoMaString.append("#");
				danMaString.append("#");
			}
			if (i < mTeamList.size() - 1) {
				danMaString.append(",");
				tuoMaString.append(",");
			}

			if (isDantou) {
				zhuMaString = danMaString.toString() + "$"
						+ tuoMaString.toString();
			} else {
				zhuMaString = tuoMaString.toString();
			}
		}

		return zhuMaString;
	}

	@Override
	public boolean isTouZhu() {
		int iZhuShu = getZhuShu();
		if (iZhuShu == 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
