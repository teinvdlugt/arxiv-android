package com.teinvdlugt.android.arxiv;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private static final String TAG = "XMLParser";

    private static final String ns = null;

    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // Look for the entry tag
            if (parser.getName().equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Entry readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        Entry entry = new Entry();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        parser.require(XmlPullParser.START_TAG, ns, "entry");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                entry.setId(readText(parser));
            } else if (name.equals("updated")) {
                try {
                    String dateText = readText(parser);
                    entry.setUpdated(format.parse(dateText));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(TAG, "readEntry: Invalid updated date");
                }
            } else if (name.equals("published")) { // TODO: 19-9-2016 Time zones?
                try {
                    String dateText = readText(parser);
                    entry.setPublished(format.parse(dateText));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(TAG, "readEntry: Invalid published date");
                }
            } else if (name.equals("title")) {
                entry.setTitle(readText(parser));
            } else if (name.equals("summary")) {
                entry.setSummary(readText(parser));
            } else if (name.equals("author")) {
                entry.getAuthors().add(readAuthor(parser));
            } else if (name.equals("comment")) {
                entry.setComment(readText(parser));
            } else if (name.equals("link")) {
                String rel = parser.getAttributeValue(null, "rel");
                String href = parser.getAttributeValue(null, "href");
                if (rel.equals("alternate"))
                    entry.setWebsiteUrl(href);
                else if (parser.getAttributeValue(null, "title").equals("pdf"))
                    entry.setPdfUrl(href);
                parser.nextTag();
            } else {
                skip(parser);
            }
        }

        return entry;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        String name = "";
        parser.require(XmlPullParser.START_TAG, ns, "author");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            if (parser.getName().equals("name"))
                name = readText(parser);
            else skip(parser);
        }
        return name;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
