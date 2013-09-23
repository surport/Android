package com.ruyicai.activity.buy.jc.explain.zq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.zq.BaseListActivity.ExplainInfo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 亚盘的界面
 * 
 * @author Administrator
 * 
 */
public class AsiaActivity extends BaseListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVisable();
		initList();
	}

	private void setVisable() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.jc_asia_top_layout);
		layout.setVisibility(View.VISIBLE);
	}

	public JSONArray getJsonArray() {
		JSONArray json = null;
		try {
			json = JcExplainActivity.jsonObject.getJSONObject("result")
					.getJSONArray("letGoals");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	public void initList() {
		JSONArray json = getJsonArray();
		if (json != null) {
			listMain = (ListView) findViewById(R.id.buy_jc_main_listview);
			adapter = new JcInfoAdapter(context, getScoreInfo(json));
			listMain.setAdapter(adapter);
		}
	}

	protected List getScoreInfo(JSONArray jsonArray) {
		listInfo = new ArrayList<ExplainInfo>();
		for (int i = 0; i < jsonArray.length(); i++) {
			ExplainInfo info = new ExplainInfo();
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setCompanyName(json.getString("companyName"));
				info.setFirstDownodds(json.getString("firstDownodds"));
				info.setFirstGoal(json.getString("firstGoalName"));
				info.setFirstUpodds(json.getString("firstUpodds"));
				info.setGoal(json.getString("goalName"));
				info.setUpOdds(json.getString("upOdds"));
				info.setDownOdds(json.getString("downOdds"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listInfo.add(info);
		}
		return listInfo;
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<ExplainInfo> mList;

		public JcInfoAdapter(Context context, List<ExplainInfo> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ExplainInfo info = mList.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.jc_asia_list_item,
						null);
				holder = new ViewHolder();
				holder.companyName = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_company);
				holder.firstGoal = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_center_leve);
				holder.firstUpodds = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_center_win);
				holder.firstDownodds = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_center_fail);
				holder.upOdds = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_right_win);
				holder.goal = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_right_leve);
				holder.downOdds = (TextView) convertView
						.findViewById(R.id.jc_europe_list_item_text_right_fail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.companyName.setText(info.getCompanyName());
			holder.firstUpodds.setText(info.getFirstUpodds());
			holder.firstDownodds.setText(info.getFirstDownodds());
			holder.firstGoal.setText(info.getFirstGoal());
			holder.upOdds.setText(info.getUpOdds());
			holder.downOdds.setText(info.getDownOdds());
			holder.goal.setText(info.getGoal());
			return convertView;
		}

		class ViewHolder {
			TextView firstGoal;// 初盘亚盘
			TextView firstUpodds;// 初盘胜水位
			TextView firstDownodds;// 初盘负水位
			TextView goal;// 即时亚盘
			TextView upOdds;// 即时胜
			TextView downOdds;// 即时负
			TextView companyName;// 公司名
		}
	}
}
