package com.tornado.booklist;

import android.graphics.Bitmap;

public class BooksInfo {

    String Title,Author[],Publisher;
    String publishedDate;Bitmap image;

    public BooksInfo(String Title,String[] Author,String Publisher,Bitmap image){
        this.Title=Title;
        this.Author=Author;
        this.image=image;
        this.Publisher=Publisher;
    }
}
