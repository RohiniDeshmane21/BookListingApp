package com.example.android.booklistingapp;

/**
 * Created by Rupali on 14-09-2016.
 */
public class bookInfo {
    private String bookName;
    private String author;
    private String publisher;
    private String publishedDate;
    private String image;

    public bookInfo(String bName, String auth, String publisherName, String date,String imagesrc)
    {
        bookName = bName;
        author = auth;
        publisher = publisherName;
        publishedDate = date;
        image = imagesrc;

    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getImage() {
        return image;
    }

}
