/**
 * Copyright 2010 (c) Pratul Kalia
 */


package net.agropedia.pop;

import java.net.URI;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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

public class PracticesView extends ListActivity {

	private PracticesViewAdapter mAdapter;
	private static Integer nid;
	private static Typeface tf;
	private DBManager dbm;
	private URI uri;
	private XMLRPCClient client;
	private static ProgressDialog fetchingData;
	private static ListView lv;
	private String[] mColumns;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		nid = b.getInt("nid");
		Log.w("Node ID for this view is", nid.toString());

		dbm = new DBManager(this);
		dbm.open();

		mColumns = new String[] {"title",
				"created",
				"changed",
				"body",
				"field_introduction",
				"field_climate_req",
				"field_soil_condi",
				"field_variti",
				"field_cropping_sys",
				"field_prep",
				"field_seed_sowing",
				"field_nutrient_mana",
				"field_water_mana",
				"field_weed_mana",
				"field_disease_mana",
				"field_insect_pest_mana",
				"field_harvesting_threshing",
				"field_yield"};

		lv = getListView();
		tf = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Hindi.ttf");

		mAdapter = new PracticesViewAdapter(this);
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

	private class PracticesViewAdapter extends BaseAdapter {

		private final LayoutInflater mInflater;
		private final HashMap<String, String> mData;

		public PracticesViewAdapter(Context ctx) {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mData = new HashMap<String, String>();

    		Cursor t = dbm.db.query("practices", new String[] {"changed"}, "nid = ?", new String[] {String.valueOf(nid)}, null, null, null);
    		t.moveToFirst();

    		if (t.getString(0) == null) {
    			Log.w("POP", "Details absent. Fetching from network.");
    			fetchingData = ProgressDialog.show(ctx, "Please wait", "Fetching data over the network...", true, true);
    			new PracticesViewFetchTask().execute();
    		}
    		else {
    			Log.w("POP", "Details exist. Getting from DB.");
    			getDataFromDB();
    		}

    		t.close();
		}

		private void getDataFromDB() {
    		Cursor c = dbm.db.query("practices", mColumns, "nid = ?", new String[] {String.valueOf(nid)}, null, null, null);

    		c.moveToFirst();

    		int i = 0;
    		while (i < 18) {
//    			mData.add(c.getString(i));
    			i++;
    		}

    		if (c != null && !c.isClosed()) {
    			c.close();
    		}
		}

    	private class PracticesViewFetchTask extends AsyncTask<Void, Void, Void> {

    		@SuppressWarnings("unchecked")
			protected Void doInBackground(Void... params) {
                uri = URI.create("http://a.pratul.in/services/xmlrpc");
	    		client = new XMLRPCClient(uri);

	    		try {
	    				Object[] p = {nid, mColumns};

	    				HashMap<String, Object> n = (HashMap<String, Object>) client.callEx("node.get", p);

	    				for (String k : n.keySet()) {
	    					Object val = n.get(k);
	    					Log.w("Current key is", k);
	    					String clas = val.getClass().getSimpleName();

	    					String ref = "String";
	    					if (clas.equals(ref)) {
	    						mData.put(k, val.toString());
	    					} else {
	    						Object[] bigarray = (Object[]) val;
	    						HashMap<String, String> smallarray = (HashMap<String, String>) bigarray[0];
	    						mData.put(k, smallarray.get("value"));
	    					}

	    				}
	    		}
	    		catch (Exception e) {
	    			Log.w("POP", "ERROR AGAYA LOL", e);
	    			cancel(false);
	    		}
				return null;

    		}

    		@Override
			protected void onCancelled() {
//    			setResult(Activity.RESULT_CANCELED);
    			finish();

    		}

			@Override
			protected void onProgressUpdate(Void... values) {

			}

			@Override
			protected void onPostExecute(Void result) {
				fetchingData.dismiss();
				lv.setAdapter(mAdapter);

//				ContentValues values = new ContentValues();
//
//	    		for (int pos = 0; pos < mData.size(); pos++) {
//	    			values.put("nid", mNIDs.get(pos));
//	    			values.put("title", mNodeTitles.get(pos));
//	    			dbm.db.insert("practices", null, values);
//	    		}
				Log.w("POP", "Async onPostExecute has run.");

				dbm.close();
			}
    	}


    	/**
    	 * Required adapter methods
    	 */
    	public int getCount() {
    		return mData.size();
		}

		public Object getItem(int position) {
			return mData.get(mColumns[position]);
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
			tvr.setText(mData.get(mColumns[position]));
			tvr.setTypeface(tf);

			return convertView;
		}

	}


}
