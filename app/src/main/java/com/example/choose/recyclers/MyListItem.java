package com.example.choose.recyclers;

import android.net.Uri;

import java.util.ArrayList;

public class MyListItem{

    Uri mediaUri;
    String title;

    public MyListItem() { }

    public Uri getMediaUri() {
        return mediaUri;
    }

    public String getTitle() {
        return title;
    }

    public void setMediaUri(Uri mediaUri) {
        this.mediaUri = mediaUri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static ArrayList<MyListItem> createContactsList(int numContacts) {
        ArrayList<MyListItem> items = new ArrayList<>();
        for (int i = 1; i <= numContacts; i++) {
            items.add(new MyListItem());
        }
        return items;
    }
}