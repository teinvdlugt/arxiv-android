package com.teinvdlugt.android.arxiv;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Entry>> {
    private static final int LOADER_ID = 0;
    private static final String TAG = "MainActivity";
    private static final String URL = "http://export.arxiv.org/api/query?search_query=google"; // For debugging

    private TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(LOADER_ID, FeedLoader.getBundle(URL), this);
        resultTV = (TextView) findViewById(R.id.test);
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
                StringBuilder sb = new StringBuilder();
                for (Entry entry : data)
                    sb.append(entry.getTitle()).append("\n");
                resultTV.setText(sb.toString());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {
        resultTV.setText("");
    }

    public void onClickButton(View view) {
        resultTV.setText("");
        getSupportLoaderManager().restartLoader(LOADER_ID, FeedLoader.getBundle(URL), this);
    }
}
