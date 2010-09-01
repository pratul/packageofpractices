/**
 * Copyright 2010 (c) Pratul Kalia
 */

package net.agropedia.pop;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class POPHome extends ListActivity {

	private HomeAdapter mLVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(mLVAdapter);

    }

	public class HomeAdapter extends BaseAdapter {

    	private final ArrayList<String> mItemsList = new ArrayList<String>();

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

