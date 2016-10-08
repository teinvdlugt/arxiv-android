package com.teinvdlugt.android.arxiv;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private SearchLayout searchLayout;
    private QueryGivenListener listener;

    public interface QueryGivenListener {
        void onQueryGiven(String url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchLayout = (SearchLayout) view.findViewById(R.id.search_layout);
        view.findViewById(R.id.search_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        String url = "http://export.arxiv.org/api/query?search_query=" + searchLayout.makeSearchQuery();
        if (listener != null) listener.onQueryGiven(url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (QueryGivenListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
