package org.iblogger.opensource;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @title
 * @author LiYa
 * @verson 1.0 Feb 2, 2012 3:35:40 PM
 */
public class PageFregmentDemo extends FragmentActivity {

	private ViewPager mViewPager = null;

	@Override
	protected void onCreate(Bundle saveInstance) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(saveInstance);
		setContentView(R.layout.page_demo);
		mViewPager = (ViewPager) findViewById(R.id.myViewPager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
	}

	int NUM = 5;

	/**
	 * @title
	 * @author LiYa
	 * @verson 1.0 Feb 2, 2012 3:41:45 PM
	 */
	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM;
		}

		@Override
		public Fragment getItem(int pos) {
			return ListAdapterItemFragment.newInstance(pos);
		}
	}

	public static class ListAdapterItemFragment extends ListFragment {

		int curPos = 0;

		public static ListAdapterItemFragment newInstance(int pos) {
			ListAdapterItemFragment listFrag = new ListAdapterItemFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("curPoc", pos);
			listFrag.setArguments(bundle);
			return listFrag;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			System.out.println("onCreateView.....");
			View v = inflater.inflate(R.layout.pager_list_item, container,
					false);
			View tv = v.findViewById(R.id.text);
			((TextView) tv).setText("Fragment #" + curPos);
			return (v);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			System.out.println("onActivityCreated.....");
			super.onActivityCreated(savedInstanceState);
			Bundle bundle = getArguments();
			curPos = bundle.getInt("curPoc", 0);
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, TITLES));
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			System.out.println("onCreate.....");
			super.onCreate(savedInstanceState);
		}

	}

	/**
	 * Our data, part 1.
	 */
	public static final String[] TITLES = { "张三", "李四", "王五", "Richard II",
			"Richard III", "Merchant of Venice", "Othello", "King Lear" };
}
