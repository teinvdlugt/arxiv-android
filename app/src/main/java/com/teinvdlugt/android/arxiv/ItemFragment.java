package com.teinvdlugt.android.arxiv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;


public class ItemFragment extends Fragment {
    private static final String ENTRY = "entry";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        Entry entry;
        if (getArguments() != null && (entry = (Entry) getArguments().getSerializable(ENTRY)) != null) {
            TextView titleTV, authorsTV, publishedTV, abstractTV;
            titleTV = (TextView) view.findViewById(R.id.title_textView);
            authorsTV = (TextView) view.findViewById(R.id.authors_textView);
            publishedTV = (TextView) view.findViewById(R.id.published_textView);
            abstractTV = (TextView) view.findViewById(R.id.abstract_textView);
            DateFormat dateFormat = DateFormat.getDateInstance();
            titleTV.setText(entry.getTitle());
            authorsTV.setText(entry.formatAuthors(getContext(), -1));
            publishedTV.setText(dateFormat.format(entry.getPublished()));
            abstractTV.setText(entry.getSummary());
            view.findViewById(R.id.getPdf_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    public static ItemFragment make(Entry entry) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY, entry);
        fragment.setArguments(args);
        return fragment;
    }
}
