package com.teinvdlugt.android.arxiv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SearchFieldLayout extends LinearLayout {
    private static final int[] searchFields = {R.string.search_all, R.string.search_title, R.string.search_author,
            R.string.search_abstract, R.string.search_comment, R.string.search_category, R.string.search_id};
    private static final String[] searchFieldCodes = {"all", "ti", "au", "abs", "co", "cat", "id"};

    private Spinner spinner;
    private EditText editText;
    private ImageButton closeButton;
    private OnCloseClickListener listener;

    public interface OnCloseClickListener {
        void onClickClose(SearchFieldLayout view);
    }

    public void setOnCloseClickListener(OnCloseClickListener listener) {
        this.listener = listener;
    }

    public SearchFieldLayout(Context context) {
        super(context);
        init();
    }

    public SearchFieldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public String getText() {
        return editText.getText().toString();
    }

    public String getSearchFieldCode() {
        return searchFieldCodes[spinner.getSelectedItemPosition()];
    }

    private void init() {
        spinner = new Spinner(getContext());
        editText = new EditText(getContext());
        closeButton = new ImageButton(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, getSpinnerTexts());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        LayoutParams editTextParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        editTextParams.weight = 1;

        closeButton.setImageResource(R.drawable.ic_clear_black_24dp);
        closeButton.setBackgroundResource(0);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onClickClose(SearchFieldLayout.this);
            }
        });

        addView(spinner);
        addView(editText, editTextParams);
        addView(closeButton);
    }

    private String[] getSpinnerTexts() {
        String[] result = new String[searchFields.length];
        for (int i = 0; i < searchFields.length; i++) {
            result[i] = getContext().getString(searchFields[i]);
        }
        return result;
    }
}
