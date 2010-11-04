/**
 * Copyright 2010 (c) Pratul Kalia
 */


package net.agropedia.pop;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
	private static ListView lv;
	private static ProgressDialog fetchingData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lv = getListView();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tf = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Hindi.ttf");

		dbm = new DBManager(this);
		dbm.open();

		mAdapter = new PracticesListAdapter(this);
		lv.setAdapter(mAdapter);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		dbm.open();
	}

	@Override
	protected void onStop() {
		super.onStop();
		dbm.close();
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

    	public PracticesListAdapter(Context ctx) {
    		mNIDs = new ArrayList<Integer>();
    		mNodeTitles = new ArrayList<String>();

    		Cursor t = dbm.db.query("practices", new String[] {"nid"}, null, null, null, null, null);
    		if (t.moveToFirst()) {
    			Log.w("POP", "Going to get data from the db");
    			getDataFromDB();
    		} else {
    			Log.w("POP", "Going to fetch data over network...");
    			fetchingData = ProgressDialog.show(ctx, "Please wait", "Fetching data over the network...", true, true);
    			new NetworkFetchTask().execute();
    		}

    		t.close();
    	}

    	private void getDataFromDB() {
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

    	private class NetworkFetchTask extends AsyncTask<Void, Void, Void> {

    		@SuppressWarnings("unchecked")
			protected Void doInBackground(Void... params) {
//	    		uri = URI.create("http://a.pratul.in/services/xmlrpc");
	    		uri = URI.create("http://172.26.116.244/services/xmlrpc");
	    		client = new XMLRPCClient(uri);

	    		try {
	    			Object[] r = (Object[]) client.callEx("agro.getpopnids", null);

	    			for (Object i : r) {
	    				String[] c = {"title"};
	    				Object[] p = {i, c};

	    				String tmptoSave = i.toString();
	    				Log.w("POPLOOP", "NID is " + tmptoSave);
	    				Integer toSave = Integer.parseInt(tmptoSave);
	    				mNIDs.add(toSave);

	    				HashMap<String, String> n = (HashMap<String, String>) client.callEx("node.get", p);

	    				for (String k : n.keySet()) {
	    					String val = n.get(k);
	    					Log.w("POPLOOP", "TITLE IS " + val);
	    					mNodeTitles.add(val);
	    				}
	    			}

	    		}
	    		catch (Exception e) {
	    			Log.w("POP", "ERROR AGAYA LOL", e);
	    		}

				return null;
			}

			@Override
			protected void onProgressUpdate(Void... values) {

			}

			@Override
			protected void onPostExecute(Void result) {
				fetchingData.dismiss();
				lv.setAdapter(mAdapter);

				ContentValues values = new ContentValues();

	    		for (int pos = 0; pos < mNIDs.size(); pos++) {
	    			values.put("nid", mNIDs.get(pos));
	    			values.put("title", mNodeTitles.get(pos));
	    			dbm.db.insert("practices", null, values);
	    		}
				Log.w("POP", "Async onPostExecute has run.");

				dbm.close();
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
