package com.ruyicai.activity.buy.jc.oddsprize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;

import android.util.Log;

/**
 * 竞彩算奖的排列组合类
 * 
 * @author win
 * 
 */
public class JCPrizePermutationandCombination {

	/**
	 * 返回一个包含每场比赛中所选项赔率中最大赔率的数组
	 * 
	 * @param list
	 *            含有每场比赛所选赔率的list列表
	 * @return
	 */
	private static double[] getEachGameMaxOdds(List<double[]> list) {
		double[] maxArrays = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			maxArrays[i] = sonDouble[sonDouble.length - 1];
		}

		return maxArrays;
	}

	/**
	 * 获取最小中奖金额
	 * 
	 * @param list
	 *            含有每场比赛各种赛况赔率的list列表
	 * @param threeDiffBallNums
	 *            最小中奖的中奖场数
	 * @return 最小中奖金额
	 */
	public static double FreedomGuoGuanMixPrize(List<double[]> list, int team,
			List<Boolean> isDanList, int isDanNum) {
		double mixPrize = 1;
		int num = team - isDanNum;
		double[] mixArrays = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0];
		}
		for (int n = 0; n < list.size(); n++) {
			if (isDanList.get(n)) {
				mixPrize *= mixArrays[n];
			}
		}
		Arrays.sort(mixArrays);
		for (int i = 0; i < num; i++) {
			mixPrize *= mixArrays[i];
		}
		return mixPrize * 2;// 2为每注两元
	}

	/**
	 * 获取多串最小中奖金额
	 * 
	 * @param list
	 *            含有每场比赛各种赛况赔率的list列表
	 * @param threeDiffBallNums
	 *            最小中奖的中奖场数
	 * @return 最小中奖金额
	 */
	public static double getDuoMixPrize(int teamNum, List<double[]> list,
			int team, List<Boolean> isDanList, int isDanNum) {
		double mixPrize = 1;
		int num = team - isDanNum;
		double[] mixArrays = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0];
		}
		for (int n = 0; n < list.size(); n++) {
			if (isDanList.get(n)) {
				mixPrize *= mixArrays[n];
			}
		}
		Arrays.sort(mixArrays);
		double[] mixs = new double[team];
		for (int i = 0; i < team; i++) {
			mixs[i] = i;
		}
		for (int i = 0; i < num; i++) {
			mixPrize *= mixArrays[i];
		}
		return mixPrize * 2 * getDouMixNum(teamNum, mixArrays, mixs);// 2为每注两元
	}

	/** add by pengcx 20130709 start */
	public static double getBeiJingDuoMinPrize(int teamNum,
			List<double[]> list, int team, List<Boolean> isDanList, int isDanNum) {
		return getDuoMixPrize(teamNum, list, team, isDanList, isDanNum) * 0.65;
	}

	public static double getBeiJingDuoMaxPrize(int teamNum,
			List<double[]> list, int team, List<Boolean> isDanList, int isDanNum) {
		return getDuoMaxPrize(teamNum, list, team, isDanList, isDanNum) * 0.65;
	}

	public static double getBeijingFreedomGuoGuanMixPrize(List<double[]> list,
			int team, List<Boolean> isDanList, int isDanNum) {
		return FreedomGuoGuanMixPrize(list, team, isDanList, isDanNum) * 0.65;
	}

	public static double getBeiJingFreedomGuoGuanMaxPrize(List<double[]> list,
			int k, List<Boolean> isDanList, int isDanNum) {
		return getFreedomGuoGuanMaxPrize(list, k, isDanList, isDanNum) * 0.65;
	}
	/** add by pengcx 20130709 end */

	/**
	 * 返回竞彩多串过关最小个数 将几场比赛分成几组 odds所有场次的赔率 mix 最小几场的赔率
	 */
	public static double getDouMixNum(int teamNum, double[] odds, double[] mixs) {
		// 初始化原始数据
		int[] a = new int[odds.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[teamNum];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		PublicMethod.combine(a, a.length, teamNum, b, teamNum, list);
		int resultInt = 0;
		for (int[] result : list) {
			int num = 0;
			for (int n = 0; n < result.length; n++) {
				int index = result[n];
				for (double mix : mixs) {
					if (index == mix) {
						num++;
						break;
					}
				}
			}
			if (num == mixs.length) {
				resultInt++;
			}
		}
		return resultInt;
	}

	/**
	 * 竞彩单关投注的最大金额
	 * 
	 * @param list
	 * @param muti
	 * @return
	 */
	public static String getDanGuanPrize(List<double[]> list, int muti) {
		double[] mixArrays = new double[list.size()];
		double maxPrize = 0;
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0];
			maxPrize += sonDouble[sonDouble.length - 1];
		}
		Arrays.sort(mixArrays);
		StringBuffer aa = new StringBuffer();
		String mixValue = PublicMethod.formatStringToTwoPoint(mixArrays[0]
				* muti);
		String maxValue = PublicMethod.formatStringToTwoPoint(maxPrize * muti);
		aa.append(mixValue).append("元").append("~")
				.append(maxValue).append("元");
		return aa + "";
	}

	/** add bypengcx 20130709 start */
	/**
	 * 获取北京单场单关最大预计奖金
	 * 
	 * @param list
	 * @param muti
	 * @return
	 */
	public static double getBeijingDanGuanMaxPrize(List<double[]> list) {
		double maxPrize = 0;
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			maxPrize += sonDouble[sonDouble.length - 1];
		}
		String maxValue = PublicMethod.formatStringToTwoPoint(maxPrize * 2 * 0.65);
		return Double.valueOf(CheckUtil.isNull(maxValue));
	}

	/**
	 * 获取北京单场单关最小预计奖学金
	 * 
	 * @param list
	 * @param muti
	 * @return
	 */
	public static double getBeijingDanGuanMinPrize(List<double[]> list) {
		double[] mixArrays = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0];
		}
		Arrays.sort(mixArrays);
		String mixValue = PublicMethod.formatStringToTwoPoint(mixArrays[0] * 2 * 0.65);

		return Double.valueOf(CheckUtil.isNull(mixValue));
	}

	/** add bypengcx 20130709 end */

	/**
	 * 竞彩单关投注的最大金额 足球比分游戏和篮球胜分差游戏的单关销售由原来的“浮动奖金”升级为“固定奖金”,在原来基础上乘2
	 * 
	 * @param list
	 * @param muti
	 * @return
	 */
	public static String getNewDanGuanPrize(List<double[]> list, int muti) {
		double[] mixArrays = new double[list.size()];
		double maxPrize = 0;
		for (int i = 0; i < list.size(); i++) {
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0];
			maxPrize += sonDouble[sonDouble.length - 1];
		}
		Arrays.sort(mixArrays);
		StringBuffer aa = new StringBuffer();
		String mixValue = PublicMethod.formatStringToTwoPoint(mixArrays[0]
				* muti * 2);
		String maxValue = PublicMethod.formatStringToTwoPoint(maxPrize * muti
				* 2);
		aa.append(mixValue).append("元").append("~")
				.append(maxValue).append("元");
		return aa + "";
	}

	/**
	 * 获取自由过关的最大金额
	 * 
	 * @param list
	 *            赔率列表
	 * @param k
	 *            k串1
	 * @return
	 */
	public static double getFreedomGuoGuanMaxPrize(List<double[]> list, int k,
			List<Boolean> isDanList, int isDanNum) {
		double[] odds = getEachGameMaxOdds(list);
		return select(odds, k, isDanList, isDanNum) * 2;
	}

	/**
	 * 混合投注获取自由过关的最大金额
	 * 
	 * @param list
	 *            赔率列表
	 * @param k
	 *            k串1
	 * @return
	 */
	public static double getFreedomHunMaxPrize(List<double[]> list, int k,
			List<Boolean> isDanList, int isDanNum) {
		double[] odds = getOdds(list);
		int[] size = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			size[i] = list.get(i).length;
		}
		double Allodds = setlectHun(odds, k, size);
		return Allodds * 2;
	}

	private static double[] getOdds(List<double[]> list) {
		double[] odds = new double[getLength(list)];
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			double[] odd = list.get(i);
			for (int j = 0; j < odd.length; j++) {
				odds[index] = odd[j];
				index++;
			}
		}
		return odds;
	}

	private static int getLength(List<double[]> list) {
		int length = 0;

		for (int i = 0; i < list.size(); i++) {
			double[] odd = list.get(i);
			length += odd.length;
		}
		return length;
	}

	/**
	 * 混合投注计算最大奖金
	 * 
	 * @param list
	 * @param k
	 * @param isDanList
	 * @param isDanNum
	 * @return
	 */
	private static double setlectHun(double[] obbs, int select, int size[]) {
		// 初始化原始数据
		int[] a = new int[obbs.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[select];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		PublicMethod.combine(a, a.length, select, b, select, list);

		// 返回数据对象
		double resultInt = 0;
		for (int[] result : list) {
			double obb = 1;
			if (isDiff(size, result)) {
				for (int p : result) {
					obb *= obbs[p];
				}
				resultInt += obb;
			}
		}
		return resultInt;
	}

	private static boolean isDiff(int[] size, int[] result) {
		boolean isDiff = true;
		int lengths = 0;
		for (int length : size) {
			lengths += length;
			int num = 0;
			for (int p : result) {
				if (p < lengths && p >= lengths - length) {
					num++;
				}
			}
			if (num >= 2) {
				isDiff = false;
				return isDiff;
			}
		}
		return isDiff;
	}

	/**
	 * 获取多串过关的最大金额
	 * 
	 * @param list
	 *            赔率列表
	 * @param k
	 *            k串1
	 * @return
	 */
	public static double getDuoMaxPrize(int teamNum, List<double[]> list,
			int k, List<Boolean> isDanList, int isDanNum) {
		double[] odds = getEachGameMaxOdds(list);
		return getDouMaxAmt(teamNum, odds, k, isDanList, isDanNum) * 2;
	}

	/**
	 * 返回竞彩多串过关最大金额
	 * 
	 * @param teamNum
	 *            多串过关3*3 teamNum = 3
	 * @param select
	 *            2*1 select=2
	 * @return 将几场比赛分成几组
	 */
	public static double getDouMaxAmt(int teamNum, double[] odds, int select,
			List<Boolean> isDanList, int isDanNum) {
		// 初始化原始数据
		int[] a = new int[odds.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[teamNum];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		PublicMethod.combine(a, a.length, teamNum, b, teamNum, list);
		double resultInt = 0;
		for (int[] result : list) {
			double[] betcode = new double[result.length];
			int danNum = 0;
			for (int n = 0; n < result.length; n++) {
				betcode[n] = odds[result[n]];
				if (isDanNum > 0 && isDanList.get(result[n])) {
					danNum++;
				}
			}
			if (isDanNum == 0 || danNum == isDanNum) {
				resultInt += select(betcode, select, null, 0);
			}
		}
		return resultInt;
	}

	private static double select(double[] odds, int k, List<Boolean> isDanList,
			int isDanNum) {
		double prizeAmt = 0;
		double[] hascombine = new double[k];
		boolean[] b = new boolean[k];
		List<Double> prizeList1 = new ArrayList<Double>();
		List<Double> prizeList = subselect(0, 1, hascombine, k, odds,
				prizeList1, isDanList, isDanNum, b);
		for (int i = 0; i < prizeList.size(); i++) {
			prizeAmt += prizeList.get(i);
		}
		return prizeAmt;
	}

	private static List<Double> subselect(int head, int index, double[] r,
			int k, double[] odds, List<Double> prizeList,
			List<Boolean> isDanList, int isDanNum, boolean[] b) {

		for (int i = head; i < odds.length + index - k; i++) {
			if (index < k) {
				r[index - 1] = odds[i];
				if (isDanList != null) {
					b[index - 1] = isDanList.get(i);
				}
				subselect(i + 1, index + 1, r, k, odds, prizeList, isDanList,
						isDanNum, b);
			} else if (index == k) {
				double eachZhuPrize = 1;// 每注彩票的中奖金额{中了的话}
				int isNum = 0;
				r[index - 1] = odds[i];
				if (isDanList != null) {
					b[index - 1] = isDanList.get(i);
				}
				if (isDanNum > 0) {
					for (int j = 0; j < b.length; j++) {
						if (b[j]) {
							isNum++;
						}
					}
				}
				if (isDanNum == 0 || isNum == isDanNum) {
					for (int j = 0; j < r.length; j++) {
						eachZhuPrize *= r[j];
					}
					prizeList.add(eachZhuPrize);
				}
				subselect(i + 1, index + 1, r, k, odds, prizeList, isDanList,
						isDanNum, b);
			}

		}
		return prizeList;
	}

}
