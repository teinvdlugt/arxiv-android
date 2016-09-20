package com.teinvdlugt.android.arxiv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<Entry> data;
    private Context context;

    public FeedAdapter(Context context, List<Entry> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<Entry> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, authorsTV, publishedTV;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(R.id.titleTextView);
            authorsTV = (TextView) itemView.findViewById(R.id.authorsTextView);
            publishedTV = (TextView) itemView.findViewById(R.id.publishedTextView);
        }

        public void bind(Entry data) {
            if (data.getTitle() != null)
                titleTV.setText(data.getTitle());
            else titleTV.setText(R.string.unknown_title);
            if (data.getPublished() != null)
                publishedTV.setText(DateFormat.getDateInstance().format(data.getPublished()));

            // Display max 3 authors
            StringBuilder authorsText = new StringBuilder();
            for (int i = 0; i < data.getAuthors().size(); i++) {
                authorsText.append(data.getAuthors().get(i));
                if (i != data.getAuthors().size() - 1) {
                    authorsText.append(", ");
                    if (i == 2) {
                        authorsText.append("...");
                        break;
                    }
                }
            }
            authorsTV.setText(authorsText);
        }
    }
}
