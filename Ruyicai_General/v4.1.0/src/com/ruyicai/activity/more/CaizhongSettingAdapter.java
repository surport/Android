package com.ruyicai.activity.more;

import java.util.List;
import java.util.Map;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CaizhongSettingAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;
	private RWSharedPreferences shellRW;

	public CaizhongSettingAdapter(Context context,
			List<Map<String, String>> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.list = list;
		shellRW = new RWSharedPreferences(context,
				ShellRWConstants.CAIZHONGSETTING);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.caizhong_setting_list_item, null);
			holder = new ViewHolder();
			holder.caizhongTextView = (TextView) convertView
					.findViewById(R.id.caizhong_TextView);
			holder.caizhongButton = (Button) convertView
					.findViewById(R.id.caizhong_set_checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.caizhongTextView.setText(list.get(position).get("shellName"));
		for (int i = 0; i < list.size(); i++) {
			if (shellRW.getStringValue(list.get(position).get("shellKey"))
					.equals(Constants.CAIZHONG_OPEN)) {
				holder.caizhongButton.setBackgroundResource(R.drawable.on);
				holder.caizhongButton.setTag(R.id.caizhong_set_checkbox, list
						.get(position).get("shellKey"));
			} else {
				holder.caizhongButton.setBackgroundResource(R.drawable.off);
				holder.caizhongButton.setTag(R.id.caizhong_set_checkbox, list
						.get(position).get("shellKey"));
			}
		}
		holder.caizhongButton.setOnClickListener(onClickListener);
		return convertView;
	}

	View.OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String lotnoString = v.getTag(R.id.caizhong_set_checkbox)
					.toString();

			if (CheckUtil.isWillSale(lotnoString, shellRW)) {
				PublicMethod.showMessage(mContext, PublicMethod.getMessageByLoto(mContext,shellRW,lotnoString));
			} else if (CheckUtil.isTickedClosed(lotnoString, shellRW)) {
				PublicMethod.showMessage(mContext, PublicMethod.getMessageByLoto(mContext,shellRW,lotnoString));
			} else if(Constants.RYJCLABEL.equals(lotnoString) 
					&& Constants.CAIZHONG_CLOSE.equals(shellRW.getStringValue(Constants.RYJC_SHOW_STATE))) {
				Toast.makeText(mContext, R.string.buy_ruyi_guess_close_info, Toast.LENGTH_SHORT).show();
			} else {
				String checkOpenOrClosed = shellRW.getStringValue(lotnoString);
				if (checkOpenOrClosed.equals(Constants.CAIZHONG_OPEN)) {
					shellRW.putStringValue(v.getTag(R.id.caizhong_set_checkbox)
							.toString(), Constants.CAIZHONG_CLOSE);
					v.setBackgroundResource(R.drawable.off);
				} else {
					shellRW.putStringValue(v.getTag(R.id.caizhong_set_checkbox)
							.toString(), Constants.CAIZHONG_OPEN);
					v.setBackgroundResource(R.drawable.on);
				}
			}

		}
	};

	public final class ViewHolder {
		public TextView caizhongTextView;
		public Button caizhongButton;
	}

}
