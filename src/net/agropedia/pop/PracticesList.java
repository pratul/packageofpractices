/**
 * Copyright 2010 (c) Pratul Kalia
 */

package net.agropedia.pop;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PracticesList extends ListActivity {

	private PracticesListAdapter mAdapter;
	private static Typeface tf;
	private DBManager dbm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();
		mAdapter = new PracticesListAdapter();
		tf = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Hindi.ttf");

		dbm = new DBManager(this);
		dbm.open();

		lv.setAdapter(mAdapter);
	}


	public class PracticesListAdapter extends BaseAdapter {
    	private final ArrayList<String> mItemsList;
    	private final LayoutInflater mInflater;
    	TextView tvr;

    	public PracticesListAdapter() {
    		mItemsList = new ArrayList<String>(Arrays.asList("हरे चारे हेतु हाइब्रिड नेपियर की खेती", "बाजरा का हरा चारा", "ग्वार का हरा चारा", "ज्वार का हरा चारा", "मक्का का हरा चारा", "लोबिया का हरा चारा", "धान की उन्नतशील खेती", "Sugarcane Package of Practices", "Package of practices for Soyabean", "Package of practices for Maize"));
    		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	}

		public int getCount() {
			return mItemsList.size();
		}

		public Object getItem(int position) {
			return mItemsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lv_practiceslist_row, null);
			}

			tvr = (TextView) convertView.findViewById(R.id.tv_practiceslist_row);
			tvr.setText(mItemsList.get(position));
			tvr.setTypeface(tf);

			return convertView;
		}
	}
}
