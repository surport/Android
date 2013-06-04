/**
 * 
 */
package com.ruyicai.code;

import com.ruyicai.pojo.AreaNum;


/**
 * 购彩注码抽象类
 * @author Administrator
 *
 */
public abstract class CodeInterface {
   

	public abstract String zhuma(AreaNum areaNums[],int beishu,int type);

//	public abstract String zhuma(AreaNum areaNums[],int beishu);
	/**
	 * 对于小于10的注码之前加0的方法
	 * @param code 直接获取的小球上的数
	 * @return
	 */
	public static StringBuffer formatInteger(int code){
		StringBuffer formatCode = new StringBuffer();
			if(code<10){
				formatCode.append(0).append(code);
			}else{
				formatCode.append(code);
			}
			
		return formatCode;		
	}


}
