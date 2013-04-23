package com.ruyicai.activity.buy.jc.oddsprize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruyicai.util.PublicMethod;

import android.util.Log;

/**
 * 竞彩算奖的排列组合类
 * @author win
 *
 */
public class JCPrizePermutationandCombination {
	
	
	/**
	 * 返回一个包含每场比赛中所选项赔率中最大赔率的数组
	 * @param list 含有每场比赛所选赔率的list列表
	 * @return
	 */
    private static	double[] getEachGameMaxOdds(List<double[]> list){
    	double[] maxArrays = new double[list.size()];
    	for(int i = 0;i<list.size();i++){
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			maxArrays[i]= sonDouble[sonDouble.length-1];
		}
    	
		return maxArrays;
	}
    /**
     * 获取最小中奖金额
     * @param list 含有每场比赛各种赛况赔率的list列表
     * @param num  最小中奖的中奖场数
     * @return 最小中奖金额
     */
    public static double FreedomGuoGuanMixPrize(List<double[]> list,int num){
    	double mixPrize = 1;
    	double[] mixArrays = new double[list.size()];
    	for(int i = 0;i<list.size();i++){
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0]; 
		}
    	Arrays.sort(mixArrays);
    	for(int i = 0;i<num;i++){
    		mixPrize *= mixArrays[i];
    	}
    	return mixPrize*2;//2为每注两元
    }
    /**
     * 获取多串最小中奖金额
     * @param list 含有每场比赛各种赛况赔率的list列表
     * @param num  最小中奖的中奖场数
     * @return 最小中奖金额
     */
    public static double getDuoMixPrize(int teamNum,List<double[]> list,int num){
    	double mixPrize = 1;
    	double[] mixArrays = new double[list.size()];
    	for(int i = 0;i<list.size();i++){
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			mixArrays[i] = sonDouble[0]; 
		}
    	Arrays.sort(mixArrays);
    	double[] mixs = new double[num];
    	for(int i = 0;i<num;i++){
    		mixs[i] = i;
    		mixPrize *= mixArrays[i];
    	}
    	return mixPrize*2*getDouMixNum(teamNum,mixArrays,mixs);//2为每注两元
    }
    /**
     * 返回竞彩多串过关最小个数
     * 将几场比赛分成几组
     * odds所有场次的赔率
     * mix 最小几场的赔率
     */
	public  static double getDouMixNum(int teamNum,double[] odds,double[] mixs) {
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
			int num =0;
			for (int n=0;n<result.length;n++) {
				int index = result[n];
				for(double mix:mixs){
					if(index == mix){
						num++;
						break;
					}
				}
			}
			if(num==mixs.length){
				resultInt++;
			}
		}
		return resultInt;
	}
	/**
	 * 竞彩单关投注的最大金额
	 * @param list
	 * @param muti
	 * @return
	 */
	public static String getDanGuanPrize(List<double[]> list,int muti){
		double[] mixArrays = new double[list.size()];
		double maxPrize = 0;
		for(int i = 0;i<list.size();i++){
			double[] sonDouble = list.get(i);
			Arrays.sort(sonDouble);
			Log.v("mixvalue",""+sonDouble[0]);
			mixArrays[i] = sonDouble[0];
			maxPrize += sonDouble[sonDouble.length-1];
		}
		Arrays.sort(mixArrays);
		StringBuffer aa = new StringBuffer();
		String mixValue = PublicMethod.formatStringToTwoPoint(mixArrays[0]*2* muti);
		String maxValue = PublicMethod.formatStringToTwoPoint(maxPrize*2* muti);
		aa.append("预计中奖金额：").append(mixValue).append("元").append("~").append(maxValue).append("元");
		return aa+"";
	}
	
	/**
	 * 获取自由过关的最大金额
	 * @param list 赔率列表
	 * @param k  k串1
	 * @return
	 */
	public static double getFreedomGuoGuanMaxPrize(List<double[]> list,int k){
		double[] odds = getEachGameMaxOdds(list);
		return select(odds,k)*2;
	}
	/**
	 * 获取自由过关的最大金额
	 * @param list 赔率列表
	 * @param k  k串1
	 * @return
	 */
	public static double getDuoMaxPrize(int teamNum,List<double[]> list,int k){
		double[] odds = getEachGameMaxOdds(list);
		return getDouMaxAmt(teamNum,odds,k)*2;
	}
    /**
     * 返回竞彩多串过关最大金额
     * @param teamNum 多串过关3*3 teamNum = 3
     * @param select 2*1 select=2
     * @return
     * 将几场比赛分成几组
     */
	public  static double getDouMaxAmt(int teamNum,double[] odds, int select) {
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
			for (int n=0;n<result.length;n++) {
				betcode[n] = odds[result[n]];
			}
			resultInt += select(betcode,select);
		}
		return resultInt;
	}
	
	private static  double select(double[] odds,int k) {
		double prizeAmt = 0;
		double[] hascombine = new double[k];
		List<Double> prizeList1 = new ArrayList<Double>();
		List<Double> prizeList  = subselect(0, 1, hascombine, k,odds,prizeList1);
		for(int i=0;i<prizeList.size();i++){
			prizeAmt += prizeList.get(i);
		}
		return prizeAmt;
	}
	private static List<Double> subselect(int head, int index, double[] r, int k, double[] odds,List<Double> prizeList) {
		
	
		for (int i = head; i < odds.length + index - k; i++) {
			if (index < k) {
				r[index - 1] = odds[i];
				subselect(i + 1, index + 1, r, k,odds,prizeList);
			} else if (index == k) {
				r[index - 1] = odds[i];
				double eachZhuPrize = 1;//每注彩票的中奖金额{中了的话}
				for(int j=0;j<r.length;j++){
					eachZhuPrize *= r[j];
				}
				prizeList.add(eachZhuPrize);
				subselect(i + 1, index + 1, r, k,odds,prizeList);
			} 

		}
		return prizeList;
	}

}
