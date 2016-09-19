package com.teinvdlugt.android.arxiv;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButton(View view) {
        // TODO: 19-9-2016 Check connection
        String url = "http://export.arxiv.org/api/query?search_query=google";

        new AsyncTask<String, Void, List<Entry>>() {
            @Override
            protected List<Entry> doInBackground(String... args) {
                try {
                    return loadXmlFromNetwork(args[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                return null;
            }

            private List<Entry> loadXmlFromNetwork(String urlString) throws IOException, XmlPullParserException {
                List<Entry> entries;
                InputStream is = null;
                try {
                    XMLParser parser = new XMLParser();
                    is = createInputStream(urlString);
                    entries = parser.parse(is);
                } finally {
                    if (is != null)
                        is.close();
                }
                return entries;
            }

            private InputStream createInputStream(String urlString) throws IOException {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                return conn.getInputStream();
            }

            @Override
            protected void onPostExecute(List<Entry> entries) {
                if (entries == null) {
                    Toast.makeText(MainActivity.this, "Errortje", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Entry entry : entries)
                        sb.append(entry.getTitle()).append("\n");
                    ((TextView) findViewById(R.id.test)).setText(sb.toString());
                }
            }
        }.execute(url);
    }
}
