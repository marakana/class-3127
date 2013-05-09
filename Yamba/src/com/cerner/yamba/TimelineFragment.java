package com.cerner.yamba;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.cerner.yambalib.StatusContract;

public class TimelineFragment extends ListFragment {
	private static final String[] FROM = { StatusContract.Columns.USER,
			StatusContract.Columns.MESSAGE, StatusContract.Columns.CREATED_AT };
	private static final int[] TO = { R.id.status_row_username,
			R.id.status_row_message, R.id.status_row_createdAt };
	private static final int LOADER_ID = 42;
	private SimpleCursorAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getString(R.string.timelineLoading));

		adapter = new SimpleCursorAdapter(getActivity(), R.layout.status_row,
				null, FROM, TO, 0);
		adapter.setViewBinder(VIEW_BINDER);

		setListAdapter(adapter);

		getLoaderManager().initLoader(LOADER_ID, null, callbacks);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		DetailsFragment fragment = (DetailsFragment) getFragmentManager()
				.findFragmentByTag(DetailsFragment.class.getSimpleName());

		if (fragment != null && fragment.isVisible()) {
			fragment.setId(id);
			fragment.updateUI();
		} else {
			startActivity(new Intent(getActivity(), DetailsActivity.class)
					.putExtra("id", id));
		}
	}

	private final LoaderCallbacks<Cursor> callbacks = new LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			if (id != LOADER_ID)
				return null;

			return new CursorLoader(getActivity(), StatusContract.CONTENT_URI,
					null, null, null, null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			adapter.swapCursor(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			adapter.swapCursor(null);
		}
	};

	private static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() == R.id.status_row_createdAt) {
				long timestamp = cursor.getLong(columnIndex);
				CharSequence relTime = DateUtils
						.getRelativeTimeSpanString(timestamp);
				((TextView) view).setText(relTime);
				return true;
			} else {
				return false;
			}
		}
	};
}
