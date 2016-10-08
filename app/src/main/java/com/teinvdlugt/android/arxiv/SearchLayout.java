package com.teinvdlugt.android.arxiv;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchLayout extends LinearLayout implements SearchFieldLayout.OnCloseClickListener {
    private static final LayoutParams sfParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    private List<SearchFieldLayout> searchFields;

    public SearchLayout(Context context) {
        super(context);
        init();
    }

    public SearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        searchFields = new ArrayList<>();
        SearchFieldLayout sfLayout1 = new SearchFieldLayout(getContext());
        SearchFieldLayout sfLayout2 = new SearchFieldLayout(getContext());
        SearchFieldLayout sfLayout3 = new SearchFieldLayout(getContext());
        searchFields.add(sfLayout1);
        searchFields.add(sfLayout2);
        searchFields.add(sfLayout3);
        sfLayout1.setOnCloseClickListener(this);
        sfLayout2.setOnCloseClickListener(this);
        sfLayout3.setOnCloseClickListener(this);
        addView(sfLayout1, sfParams);
        addView(sfLayout2, sfParams);
        addView(sfLayout3, sfParams);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_add_black_24dp);
        TextView addTV = new TextView(getContext());
        addTV.setText(R.string.add_search_field);
        int _8dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        addTV.setPadding(_8dp, 0, 0, 0);
        ll.setPadding(_8dp, _8dp * 2, _8dp, _8dp);
        ll.addView(imageView);
        ll.addView(addTV);
        ll.setOnClickListener(new OnClickListener() {
            public void onClick(View view) { onClickAddField(); }
        });
        addView(ll);
    }

    public void onClickAddField() {
        SearchFieldLayout sfLayout = new SearchFieldLayout(getContext());
        searchFields.add(sfLayout);
        sfLayout.setOnCloseClickListener(this);
        addView(sfLayout, getChildCount() - 1, sfParams);
    }

    @Override
    public void onClickClose(SearchFieldLayout view) {
        removeView(view);
        searchFields.remove(view);
    }

    public String makeSearchQuery() {
        StringBuilder sb = new StringBuilder();
        for (SearchFieldLayout searchField : searchFields) {
            String text = searchField.getText().trim();
            if (text.isEmpty()) continue;
            else sb.append(searchField.getSearchFieldCode()).append(":").append(text).append("&");
            // TODO: 8-10-2016 Account for id lists and AND/OR things
        }
        // Remove last "&"
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
