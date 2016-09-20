package com.teinvdlugt.android.arxiv;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Entry>> {
    private static final int LOADER_ID = 0;
    private static final String TAG = "MainActivity";
    private static final String URL = "http://export.arxiv.org/api/query?search_query=google"; // For debugging

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
