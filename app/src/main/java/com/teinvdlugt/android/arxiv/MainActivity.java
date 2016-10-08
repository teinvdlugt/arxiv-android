package com.teinvdlugt.android.arxiv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Entry>> {
    private static final int LOADER_ID = 0;
    private static final String TAG = "MainActivity";
    private static final String URL = "http://export.arxiv.org/api/query?search_query=google"; // For debugging
    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;

    private FeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedAdapter(this, null);
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID, FeedLoader.getBundle(URL), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            onClickSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickSearch() {
        startActivityForResult(new Intent(this, SearchActivity.class), SEARCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String url;
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && (url = data.getStringExtra(SearchActivity.RETURN_URL)) != null) {
            getSupportLoaderManager().restartLoader(LOADER_ID, FeedLoader.getBundle(url), this);
        }
    }

    @Override
    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
        return new FeedLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {
        if (loader.getId() == LOADER_ID) {
            if (data == null) {
                Toast.makeText(MainActivity.this, "Errortje", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {
        adapter.setData(null);
        adapter.notifyDataSetChanged();
    }
}
