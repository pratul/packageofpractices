/**
 * Copyright 2010 (c) Pratul Kalia
 */


package net.agropedia.pop;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

		tf = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Hindi.ttf");

		dbm = new DBManager(this);
		dbm.open();

		mAdapter = new PracticesListAdapter();
		lv.setAdapter(mAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent pv = new Intent(getBaseContext(), PracticesView.class);
		Integer nid = (Integer) mAdapter.mNIDs.get(position);

		Bundle b = new Bundle();
		b.putInt("nid", nid);

		pv.putExtras(b);
		startActivity(pv);
	}


	public class PracticesListAdapter extends BaseAdapter {
    	private final ArrayList<Integer> mNIDs;
    	private final ArrayList<String> mNodeTitles;
    	private final LayoutInflater mInflater;
    	private Cursor c;
    	TextView tvtitle;
    	TextView tvnid;

    	public PracticesListAdapter() {
    		if (dbm.db == null) {
    			dbm.open();
    		}
    		mNIDs = new ArrayList<Integer>();
    		mNodeTitles = new ArrayList<String>();
    		getNodesData();
    		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	}

    	private void getNodesData() {

    		c = dbm.db.query("practices", new String[] {"nid", "title"}, null, null, null, null, "nid DESC");
    		c.moveToFirst();

    		do {
    			mNIDs.add(c.getInt(0));
    			mNodeTitles.add(c.getString(1));
    		} while (c.moveToNext());

    		if (c != null && !c.isClosed()) {
    			c.close();
    		}

    	}


    	public int getCount() {
			return mNodeTitles.size();
		}

		public Object getItem(int position) {
			return mNodeTitles.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lv_practiceslist_row, null);
			}

			tvtitle = (TextView) convertView.findViewById(R.id.tv_practiceslist_row);
			tvnid   = (TextView) convertView.findViewById(R.id.tv_practiceslist_nid);

			tvtitle.setText(mNodeTitles.get(position));
			tvtitle.setTypeface(tf);

			String bldr = "(Node ID " + String.valueOf(mNIDs.get(position)) + ")";
			tvnid.setText(bldr);

			return convertView;
		}
	}
}
