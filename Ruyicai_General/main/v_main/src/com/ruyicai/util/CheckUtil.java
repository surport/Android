package com.ruyicai.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author fansm
 *
 */
public class CheckUtil {
	/** chinese code regex*/
	private static final String chinese_code_regex = "(^[\u4E00-\u9FA5]{2,16}$)|([\u4E00-\u9FA5]+[\u00b7][\u4E00-\u9FA5]+$)";
    /**
     * check name
     * @param name
     * @return
     */
	public static boolean isValidName(String name) {
		if (name == null || "".equals(name)) return false;
        if (name.length() >= 2 && name.length() <= 16) {
     	   if (isValidInput(chinese_code_regex,name)) {
     		   return true;
     	   }
        } 
 	   return false;
    } 
	/**
	 * check input
	 * @param chinese_code_regex
	 * @param name
	 * @return
	 */
    public static boolean isValidInput(String chinese_code_regex,String name) {
    	try {
	 	    Pattern chinese_code = Pattern.compile(chinese_code_regex);
	        Matcher proto = chinese_code.matcher(name);
	     	return proto.find();
    	} catch (Exception ex) {
    		return false;
    	}
    }
	/**
     * check card all place of China
     * @param card
     * @return
     */
	public static  boolean isValidCard(String card){
        if (card == null || "".equals(card)) return false;
        if (card.length() == 10) {
        	return true;
        } else if (card.length() == 18) {
        	return isValidChinaCard(card);
        }
		return false;

	}
	/**
	 * check card mainland of China
	 * @param card
	 * @return
	 */
	public static boolean isValidChinaCard(String card) {
		int year = Integer.parseInt(card.substring(6,10));
		int month = Integer.parseInt(card.substring(10,12));
		int day = Integer.parseInt(card.substring(12,14));

		int[] weight = {2,4,8,5,10,9,7,3,6,1,2,4,8,5,10,9,7};
		char[] checkCard = {'1','0','X','9','8','7','6','5','4','3','2'};

		int sum = 0;
		int[] tmpCard = new int[18];

		if (year < 1900 || month < 1 || month > 12 || day < 1 || day > 31 
				|| ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
				||(month == 2 && ((year % 4 > 0 && day > 28) || day > 29))){
			return false;
		}

		for (int i = 1; i < 18; i++) { 
			int j = 17 - i;
			tmpCard[i-1] = Integer.parseInt(card.substring(j,j+1));
		}

		for(int i = 0; i < 17; i++){ 
			sum += weight[i] * tmpCard[i];
		}
		sum = sum % 11; 
		if(card.charAt(17) != checkCard[sum]){
		    return false;
		}
		return true;
	}

}
