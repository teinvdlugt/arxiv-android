package com.teinvdlugt.android.arxiv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ItemActivity extends AppCompatActivity {
    public static final String ENTRY = "entry";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Entry entry;
        if ((entry = (Entry) getIntent().getSerializableExtra(ENTRY)) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ItemFragment.make(entry))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}