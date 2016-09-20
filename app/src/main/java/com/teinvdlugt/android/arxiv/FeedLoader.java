package com.teinvdlugt.android.arxiv;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FeedLoader extends AsyncTaskLoader<List<Entry>> {
    public static final String URL = "feedUrl";

    /**
     * @param context The context
     * @param args    This Bundle must contain the Url to load with the key FeedLoader.URL
     */
    public FeedLoader(Context context, @NonNull Bundle args) {
        super(context);
        this.args = args;
    }

    private Bundle args;
    private List<Entry> data;

    @Override
    protected void onStartLoading() {
        if (data != null)
            deliverResult(data);
        else forceLoad();
    }

    @Override
    public List<Entry> loadInBackground() {
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return null;
        }

        try {
            return loadXmlFromNetwork(args.getString(URL));
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
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        data = null;
    }

    @Override
    public void deliverResult(List<Entry> data) {
        if (isReset()) return;
        this.data = data;
        if (isStarted())
            super.deliverResult(data);
    }

    public static Bundle getBundle(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        return bundle;
    }
}
