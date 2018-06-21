package com.tornado.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private Utils(){}

    public static List<BooksInfo> fetchDataFromBooks(String stringUrl) {
        URL url=createUrl(stringUrl);
        String jsonResponse;
        jsonResponse = makeHttpRequest(url);
        List<BooksInfo> bookList=extractBooksFromJson(jsonResponse);
        return bookList;
    }
    public static String makeHttpRequest(URL url) {
        String jsonResponse="";
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        if(url==null){
            Log.d("Harshit","url is null");
            return null;
        }
        try {
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            inputStream=urlConnection.getInputStream();
            jsonResponse=readFromStream(inputStream);
        } catch (IOException e) {
            Log.e("Harshit","makeHttpRequest().1",e);
        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("Harshit","makeHttpRequest().2",e);
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream){
        StringBuilder stringBuilder=new StringBuilder();

        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line= null;
        try {
            line = reader.readLine();
            while (line!=null){
                stringBuilder.append(line);
                line=reader.readLine();
            }
        } catch (IOException e) {
            Log.e("Harshit","readFromStream()",e);
        }
        return stringBuilder.toString();
    }
    private static ArrayList<BooksInfo> extractBooksFromJson(String jsonResponse){
        ArrayList<BooksInfo> bookList=new ArrayList<>();
        String publisher,publishedDate;
        Bitmap image;String[] author;

        if(jsonResponse==null){
            Log.d("Harshit","JsonResponse is null");
            return null;
        }
        try {
            JSONObject baseJsonResponse=new JSONObject(jsonResponse);
            JSONArray booksArray= baseJsonResponse.getJSONArray("items");
            Log.d("Harshit","Array length="+booksArray.length());

            for(int i=0;i<booksArray.length();i++){
                JSONObject items=booksArray.getJSONObject(i);
                JSONObject volumeInfo=items.getJSONObject("volumeInfo");
                String title=volumeInfo.getString("title");


                if(volumeInfo.has("authors")){
                    JSONArray jsonAuthors=volumeInfo.getJSONArray("authors");
                    author=new String[jsonAuthors.length()];
                    for(int j=0;j<jsonAuthors.length();j++){
                        author[j]=jsonAuthors.getString(j);
                    }
                }
                else
                    author=null;
                if(volumeInfo.has("publisher"))
                    publisher=volumeInfo.getString("publisher");
                else
                    publisher=null;

                if(volumeInfo.has("publishedDate"))
                    publishedDate=volumeInfo.getString("publishedDate");
                else
                    publishedDate=null;
                if(volumeInfo.has("imageLinks"))
                    image= convertToImage(volumeInfo.getJSONObject("imageLinks").getString("thumbnail"));
                else
                    image=null;
                //Log.d("Harshit","publishedDate="+publishedDate);
                //double rating=volumeInfo.getDouble("averageRating");

                BooksInfo booksInfo=new BooksInfo(title,author,publisher,image);
                bookList.add(booksInfo);
            }
        } catch (JSONException e) {
            Log.e("Harshit","exractBooksFromJson()",e);
        }
        return bookList;
    }

    private static Bitmap convertToImage(String string) {
        Bitmap bitmap=null;
        if(string==null){
            return null;
        }
        URL url=createUrl(string);
        try {
            bitmap= BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (IOException e) {
            Log.e("Harshit","convertToImage()",e);
        }
        return bitmap;
    }

    private static URL createUrl(String string) {
        URL url=null;
        try {
            url=new URL(string);
        } catch (MalformedURLException e) {
            Log.e("Harshit","createUrl()",e);
        }
        return url;
    }
    public static String makeUrl(String baseUrl,String query){
        Uri buildUri=Uri.parse(baseUrl).buildUpon().appendQueryParameter("q",query).build();
        Log.d("Harshit",buildUri.toString());
        return buildUri.toString();
    }
}