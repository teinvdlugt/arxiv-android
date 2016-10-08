package com.teinvdlugt.android.arxiv;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Entry implements Serializable {
    private String id;
    private Date updated;
    private Date published;
    private String title;
    private String summary;
    private List<String> authors = new ArrayList<>();
    private String comment;
    private String pdfUrl;
    private String websiteUrl;
    // TODO: 19-9-2016 Categories

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String formatAuthors(Context context, int max) {
        if (authors.size() == 0) return context.getString(R.string.no_authors);

        StringBuilder authorsText = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            authorsText.append(authors.get(i));
            if (i != authors.size() - 1) {
                authorsText.append(", ");
                if (i == max - 1) {
                    authorsText.append("...");
                    break;
                }
            }
        }
        return authorsText.toString();
    }
}
