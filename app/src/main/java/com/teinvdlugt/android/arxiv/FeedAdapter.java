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
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClickItem(Entry item);
    }

    public FeedAdapter(Context context, List<Entry> data, OnItemClickListener clickListener) {
        this.context = context;
        this.data = data;
        this.listener = clickListener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, authorsTV, publishedTV;
        Entry entry;

        ViewHolder(View itemView) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(R.id.titleTextView);
            authorsTV = (TextView) itemView.findViewById(R.id.authorsTextView);
            publishedTV = (TextView) itemView.findViewById(R.id.publishedTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRoot();
                }
            });
        }

        void bind(Entry data) {
            this.entry = data;
            if (data.getTitle() != null)
                titleTV.setText(data.getTitle());
            else titleTV.setText(R.string.unknown_title);
            if (data.getPublished() != null)
                publishedTV.setText(DateFormat.getDateInstance().format(data.getPublished()));

            // Display max 3 authors
            authorsTV.setText(data.formatAuthors(context, 3));
        }

        private void onClickRoot() {
            if (entry != null && listener != null) {
                listener.onClickItem(entry);
            }
        }
    }
}
