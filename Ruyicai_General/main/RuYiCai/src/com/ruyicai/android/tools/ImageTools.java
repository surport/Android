package com.ruyicai.android.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 提供了图片的处理工具方法，如： 1.根据目标大小缩放位图； 2.根据目标显示大小高效的加载位图等；
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-22
 */
public class ImageTools {

	/**
	 * 从Resouce中获取图片资源，缩放至目标大小
	 * 
	 * @param aResources
	 *            Resource对象
	 * @param aResId
	 *            图片资源id
	 * @param aDstWidth
	 *            目标宽度
	 * @param aDstHeight
	 *            目标高度
	 * @return 缩放后Bitmap对象
	 */
	public static Bitmap scaleBitmapFromResourceBaseDestinationSize(
			Resources aResources, int aResId, int aDstWidth, int aDstHeight) {
		Bitmap rawBitmap = BitmapFactory.decodeResource(aResources, aResId);
		Bitmap desBitmap = Bitmap.createScaledBitmap(rawBitmap, aDstWidth,
				aDstHeight, false);

		return desBitmap;
	}

	/**
	 * 从Resource中获取图片，根据目标大小编码位图。适用于在一个有限的空间显示过大的图片， 根据目标大小计算编码方式，加载适当清晰度的图片
	 * ，减少不必要的内存消耗
	 * 
	 * @param aRes
	 *            Resource对象
	 * @param aResId
	 *            图片资源id
	 * @param aReqWidth
	 *            显示图片的宽度
	 * @param aReqHeight
	 *            显示图片的高度
	 * @return 编码后的位图对象
	 */
	public static Bitmap decodeBitmapFromResourceBaseRequireSize(
			Resources aRes, int aResId, int aReqWidth, int aReqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(aRes, aResId, options);

		options.inSampleSize = caculateBitmapInSampleSizeBaseRequireSize(
				options, aReqWidth, aReqHeight);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeResource(aRes, aResId, options);
	}

	/**
	 * 根据显示的大小计算位图编码的inSampleSize值
	 * 
	 * @param aOptions
	 *            BitmapFactory.Options对象
	 * @param aReqWidth
	 *            显示图片的宽
	 * @param aReqHeight
	 *            显示图片高
	 * @return inSampleSize计算结果
	 */
	public static int caculateBitmapInSampleSizeBaseRequireSize(
			BitmapFactory.Options aOptions, int aReqWidth, int aReqHeight) {
		final int height = aOptions.outHeight;
		final int width = aOptions.outWidth;
		int inSampleSize = 1;

		if (height > aReqHeight || width > aReqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) aReqHeight);
			final int widthRatio = Math
					.round((float) width / (float) aReqWidth);

			inSampleSize = heightRatio <= widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
