/**
 * Copyright 2010 (c) Pratul Kalia
 */

package net.agropedia.pop;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class POPHome extends ListActivity {

	private HomeAdapter mLVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLVAdapter = new HomeAdapter();
        setListAdapter(mLVAdapter);

    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		switch (position) {
		case 0:
			Intent popList = new Intent(getBaseContext(), POPlist.class);
			startActivity(popList);
		case 1:
//			Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://agropedia.iitk.ac.in"));
//			startActivity(browser);

		}

	}


	public class HomeAdapter extends BaseAdapter {

    	private final ArrayList<String> mItemsList;
    	private final LayoutInflater mInflater;

    	public HomeAdapter() {
    		mItemsList = new ArrayList<String>(Arrays.asList("Package of Practices", "Agropedia.net on the web"));
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
	    	final TextView tv;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lv_home_row, null);
			}

			tv = (TextView) convertView.findViewById(R.id.tv_homescreen_row);
			tv.setText(mItemsList.get(position));
			return convertView;
		}

    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.about, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_about:
			Context context = getApplicationContext();
			CharSequence text = "Part of the Agropedia project\nhttp://agropedia.iitk.ac.in";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

