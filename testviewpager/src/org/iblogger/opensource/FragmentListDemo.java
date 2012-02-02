package org.iblogger.opensource;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @title
 * @author LiYa
 * @verson 1.0 Feb 2, 2012 11:38:13 AM
 */
public class FragmentListDemo extends FragmentActivity {

	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		FragmentManager fragmentMgr = getSupportFragmentManager();
		if (fragmentMgr.findFragmentById(android.R.id.content) == null) {
			FragmentTransaction transaction = fragmentMgr.beginTransaction();
			transaction.add(android.R.id.content, new TitleListFragment());
			transaction.commit();
		}
	}

	/**
	 * @title
	 * @author LiYa
	 * @verson 1.0 Feb 2, 2012 11:49:03 AM
	 */
	public static class TitleListFragment extends ListFragment {

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			Toast.makeText(getActivity(), "the clicked = " + position,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, TITLES));
		}

	}

	/**
	 * Our data, part 1.
	 */
	public static final String[] TITLES = { "Henry IV (1)", "Henry V",
			"Henry VIII", "Richard II", "Richard III", "Merchant of Venice",
			"Othello", "King Lear" };
}
