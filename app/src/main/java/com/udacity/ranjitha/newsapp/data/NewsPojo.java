package com.udacity.ranjitha.newsapp.data;

import android.graphics.Bitmap;

public class NewsPojo {

    //Variable used for store the type of the content
    private String sectionTitle;

    //variable used for store the title of the content
    private String title;

    //variable for the store the author name
    private String author;

    //variable used for store the publication date of the content
    private String publicationDate;

    //variable used for store the web url of the content
    private String url;

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public NewsPojo(String sectionTitle, String title, String author, String publicationDate, String url) {
        this.sectionTitle = sectionTitle;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.url = url;
    }

    public String getUrl() {
        return url;

    }


}
