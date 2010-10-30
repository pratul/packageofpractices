/**
 * Copyright 2010 (c) Pratul Kalia
 */


package net.agropedia.pop;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
	private URI uri;
	private XMLRPCClient client;
	private static LayoutInflater mInflater;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = getListView();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    	private Cursor c;
    	TextView tvtitle;
    	TextView tvnid;

    	@SuppressWarnings("unchecked")
    	public PracticesListAdapter() {
    		mNIDs = new ArrayList<Integer>();
    		mNodeTitles = new ArrayList<String>();

    		uri = URI.create("http://a.pratul.in/services/xmlrpc");
    		client = new XMLRPCClient(uri);

    		try {
    			Object[] r = (Object[]) client.callEx("agro.getpopnids", null);

    			for (Object i : r) {
    				String[] p = {"title"};
    				Object[] params = {i, p};

    				String tmptoSave = i.toString();
    				Integer toSave = Integer.parseInt(tmptoSave);
    				mNIDs.add(toSave);

    				HashMap<String, String> n = (HashMap<String, String>) client.callEx("node.get", params);

    				for (String k : n.keySet()) {
    					String val = n.get(k);
    					mNodeTitles.add(val);
    				}
    			}

    		}
    		catch (Exception e) {
    			Log.w("POP", "ERROR AGAYA LOL", e);
    		}
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


    	/**
    	 * Compulsory adapter methods
    	 */
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
