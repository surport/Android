package com.ruyicai.activity.common;

import java.util.Date;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 自定义下拉刷新列表
 * 
 * @author Administrator
 * 
 */
public class PullRefreshListView extends ListView implements OnScrollListener {
	private static final String TAG = "PullRefreshListView";

	/** 松手刷新状态 */
	private final static int RELEASE_TO_REFRESH = 0;
	/** 下拉刷新状态 */
	private final static int PULL_TO_REFRESH = 1;
	/** 正在刷新状态 */
	private final static int REFRESHING = 2;
	/** 刷新完成状态 */
	private final static int DONE = 3;
	/** 正在加载状态 */
	private final static int LOADING = 4;
	/** 填充比例：实际的padding的距离与界面上偏移距离的比例,用于实现难下拉的感觉 */
	private final static int PADDING_RATIO = 3;

	private LayoutInflater inflater;

	/** 列表头视图对象 */
	private LinearLayout listHeadView;
	/** 提示 */
	private TextView tipsTextview;
	/** 最后更新时间 */
	private TextView lastUpdatedTextView;
	/** 箭头指示 */
	private ImageView arrowImageView;
	/** 加载进度条 */
	private ProgressBar progressBar;

	/** 上转下箭头动画 */
	private RotateAnimation upToDownAnimation;
	/** 下转上箭头动画 */
	private RotateAnimation downToUpAnimation;

	/** 列表刷新接口 */
	private OnRefreshListener refreshListener;
	/** 下拉列表状态 */
	private int listState;
	/** 触摸起始y坐标 */
	private int touchStartY;
	/** 列表头视图的高度 */
	private int listHeadViewHeight;
	/** 显示的第一个表项的索引 */
	private int visibleFirstItemIndex;
	/** 可见的最后一个表项的索引 */
	private int visibleLastItemIndex;
	/** 可见的所有表项的个数 */
	private int visibleAllItemCount;
	/** 用于保证startY的值在一个完整的touch事件中只被记录一次 */
	private boolean isRecoredStartY;
	private boolean isBack;
	/** 是否可以刷新：实现了OnRefreshListener接口，才可以刷新 */
	private boolean isRefreshable;
	private boolean isPush;
	
	private boolean isShow = true;

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public PullRefreshListView(Context context) {
		super(context);
		initPullRefreshListView(context);
	}

	public PullRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPullRefreshListView(context);
	}

	/**
	 * 初始化下拉刷新列表
	 * 
	 * @param context
	 */
	private void initPullRefreshListView(Context context) {
		initHeadView(context);
		initArrowImageAnimation();
		initPullRefreshListViewState();
	}

	/**
	 * 初始化列表状态
	 */
	private void initPullRefreshListViewState() {
		// 初始化下拉列表状态
		listState = DONE;
		isRefreshable = false;
		isPush = true;
	}

	/**
	 * 初始化箭头动画
	 */
	private void initArrowImageAnimation() {
		// 下拉箭头由上之下旋转动画
		upToDownAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upToDownAnimation.setInterpolator(new LinearInterpolator());
		upToDownAnimation.setDuration(250);
		upToDownAnimation.setFillAfter(true);

		// 下拉箭头有下至上旋转动画
		downToUpAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downToUpAnimation.setInterpolator(new LinearInterpolator());
		downToUpAnimation.setDuration(200);
		downToUpAnimation.setFillAfter(true);
	}

	/**
	 * 初始化列表头视图
	 * 
	 * @param context
	 */
	private void initHeadView(Context context) {
		inflater = LayoutInflater.from(context);
		// 获取列表头视图对象，并获相关的组件的对象
		listHeadView = (LinearLayout) inflater.inflate(R.layout.pullrefreshlist_head,
				null);
		arrowImageView = (ImageView) listHeadView
				.findViewById(R.id.head_arrowImageView);
		progressBar = (ProgressBar) listHeadView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) listHeadView
				.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) listHeadView
				.findViewById(R.id.head_lastUpdatedTextView);

		// 测量头视图的高度，并设置其Padding，使其不可见
		measureHeadView(listHeadView);
		listHeadViewHeight = listHeadView.getMeasuredHeight();
		listHeadView.setPadding(0, -1 * listHeadViewHeight, 0, 0);
		listHeadView.invalidate();
		addHeaderView(listHeadView, null, false);
		setOnScrollListener(this);
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureHeadView(View headView) {
		ViewGroup.LayoutParams p = headView.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		// 计算宽度
		int headViewWidthSpec = ViewGroup
				.getChildMeasureSpec(0, 0 + 0, p.width);

		// 计算高度
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			// EXACTLY完全，父元素决定子元素的大小
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			// UNSPECIFIED不指定，可以设置任意高度
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}

		headView.measure(headViewWidthSpec, childHeightSpec);
	}

	/**
	 * 滑动监听事件
	 */
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		visibleFirstItemIndex = firstVisiableItem;
		visibleLastItemIndex = firstVisiableItem + arg2 - 1;
		visibleAllItemCount = arg2;

		if (Constants.isDebug)PublicMethod.myOutLog(TAG, "列表显示的第一个表项:" + visibleFirstItemIndex + ",最后一个表项:"
				+ visibleLastItemIndex + "总共显示的表项:" + visibleAllItemCount);
		if (visibleFirstItemIndex == 1 && !isPush) {
			setSelection(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 如果设置了刷新接口，为可刷新状态，才进行如下的操作
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 手指按下屏幕，并且当前开始显示表头，且没有记录点击的Y坐标的时候，记录按下的Y坐标的值
				if (visibleFirstItemIndex == 0 && !isRecoredStartY) {
					isRecoredStartY = true;
					isPush = true;
					touchStartY = (int) event.getY();
					if (Constants.isDebug)PublicMethod.myOutLog(TAG, "在ACTION_DOWN时候，记录当前按下的Y坐标:" + touchStartY);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (listState != REFRESHING && listState != LOADING) {
					// 如果当还未下拉至RELEASE_TO_REFRESH状态，就放手，返回初始状态
					if (listState == PULL_TO_REFRESH) {
						listState = DONE;
						changeHeaderViewByState();
						if (Constants.isDebug)PublicMethod.myOutLog(TAG, "由下拉刷新状态，到done状态");
					}

					// 如果下拉至RELEASE_TOREFRESH状态，松手，转变成REFRESHING状态，并调用onRefresh方法刷新
					if (listState == RELEASE_TO_REFRESH) {
						listState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
						if (Constants.isDebug)PublicMethod.myOutLog(TAG, "由松开刷新状态，转变成refreshing状态");
					}
				}

				// 下次按下下拉的时候，从新保留按下的Y坐标
				isRecoredStartY = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				// 获取当前手指的Y坐标
				int tempY = (int) event.getY();

				// 如果还没有记录点击时的Y坐标，并且已经下拉至显示表头了，记录点击开始的Y坐标
				if (!isRecoredStartY && visibleFirstItemIndex == 0) {
					if (Constants.isDebug)PublicMethod.myOutLog(TAG, "在move时候记录下位置");
					isRecoredStartY = true;
					touchStartY = tempY;
				}

				if (listState != REFRESHING && isRecoredStartY
						&& listState != LOADING) {
					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (listState == RELEASE_TO_REFRESH) {
						setSelection(0);
						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - touchStartY) / PADDING_RATIO < listHeadViewHeight)
								&& (tempY - touchStartY) > 0) {
							listState = PULL_TO_REFRESH;
							changeHeaderViewByState();
							if (Constants.isDebug)PublicMethod.myOutLog(TAG, "由松开刷新状态转变到下拉刷新状态");
						}
						// 一下子推到顶了
						else if (tempY - touchStartY <= 0) {
							listState = DONE;
							changeHeaderViewByState();
							if (Constants.isDebug)PublicMethod.myOutLog(TAG, "由松开刷新状态转变到done状态");
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (listState == PULL_TO_REFRESH) {
						setSelection(0);
						// 如果下拉的高度大于表头的高度的时候，下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - touchStartY) / PADDING_RATIO >= listHeadViewHeight) {
							listState = RELEASE_TO_REFRESH;
							isBack = true;
							changeHeaderViewByState();
							if (Constants.isDebug)PublicMethod.myOutLog(TAG, "由done或者下拉刷新状态转变到松开刷新");
						}
						// 上推到顶了
						else if (tempY - touchStartY <= 0) {
							listState = DONE;
							changeHeaderViewByState();
							isPush = false;
							if (Constants.isDebug)PublicMethod.myOutLog(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}

					// done状态下
					if (listState == DONE) {
						// 如果已经向下滑动了，则转换为PULL_TO_TRFRESH状态
						if (tempY - touchStartY > 0) {
							listState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (listState == PULL_TO_REFRESH) {
						// 如果是下拉刷新状态，在下拉的过程中添加表头的padding值
						listHeadView.setPadding(0, -1 * listHeadViewHeight
								+ (tempY - touchStartY) / PADDING_RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (listState == RELEASE_TO_REFRESH) {
						listHeadView.setPadding(0, (tempY - touchStartY)
								/ PADDING_RATIO - listHeadViewHeight, 0, 0);
					}
				}
				break;
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (listState) {
		case RELEASE_TO_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(upToDownAnimation);

			tipsTextview.setText(getResources().getString(
					R.string.release_to_refresh));
			if (Constants.isDebug)PublicMethod.myOutLog(TAG, "当前状态，松开刷新");
			break;
		case PULL_TO_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(downToUpAnimation);

				tipsTextview.setText(getResources().getString(
						R.string.pull_to_refresh));
			} else {
				tipsTextview.setText(getResources().getString(
						R.string.pull_to_refresh));
			}
			if (Constants.isDebug)PublicMethod.myOutLog(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:
			listHeadView.setPadding(0, 0, 0, 0);
			if (isShow) {
				progressBar.setVisibility(View.VISIBLE);
			} else {
				progressBar.setVisibility(View.GONE);
			}
			
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText(getResources().getString(R.string.refreshing));
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			if (Constants.isDebug)PublicMethod.myOutLog(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			listHeadView.setPadding(0, -1 * listHeadViewHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.pulltorefresh);
			tipsTextview.setText(getResources().getString(
					R.string.pull_to_refresh));
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			if (Constants.isDebug)PublicMethod.myOutLog(TAG, "当前状态，done");
			break;
		}
	}

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText(getResources().getString(R.string.updating)
				+ new Date().toLocaleString());
		super.setAdapter(adapter);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}
	
	public void onRefreshComplete() {
		listState = DONE;
//		lastUpdatedTextView.setText(getResources().getString(R.string.updating) + new Date().toLocaleString());
		changeHeaderViewByState();
		invalidateViews();
		setSelection(0);
	}
	
	public void setShowState(boolean flag) {
		this.isShow = flag;
	}

}