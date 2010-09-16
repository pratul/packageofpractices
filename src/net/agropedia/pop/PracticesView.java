/**
 * Copyright 2010 (c) Pratul Kalia
 */


package net.agropedia.pop;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PracticesView extends ListActivity {

	private PracticesViewAdapter mAdapter;
	private static Integer nid;
	private static Typeface tf;
	private DBManager dbm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		nid = b.getInt("nid");

		dbm = new DBManager(this);
		dbm.open();
		ListView lv = getListView();
		tf = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Hindi.ttf");

		mAdapter = new PracticesViewAdapter();
		lv.setAdapter(mAdapter);
	}


	private class PracticesViewAdapter extends BaseAdapter {

		private final LayoutInflater mInflater;
		private final ArrayList<String> mData;

		public PracticesViewAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mData = new ArrayList<String>();

    		Cursor c = dbm.db.query(
    				"practices",
    				new String[] {"title", "f_introduction", "f_climatic", "f_soil", "f_varieties", "f_cropping", "f_field_prep", "f_seed_sowing", "f_nutrient", "f_water", "f_weed", "f_disease", "f_insect", "f_harvesting", "f_yield"},
    				"nid = ?",
    				new String[] {String.valueOf(nid)},
    				null,
    				null,
    				null);

    		c.moveToFirst();

    		int i = 0;
    		while (i < 15) {
    			mData.add(c.getString(i));
    			i++;
    		}

    		if (c != null && !c.isClosed()) {
    			c.close();
    		}

		}

    	public int getCount() {
    		return 15;
		}

		public Object getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tvr;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lv_practicesview_row, null);
			}

			tvr = (TextView) convertView.findViewById(R.id.tv_practicesview_row);
			tvr.setText(mData.get(position));
			tvr.setTypeface(tf);

			return convertView;
		}

	}


}
