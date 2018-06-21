package com.tornado.booklist;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String GOOGLE_BOOKS_API="https://www.googleapis.com/books/v1/volumes";

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    EditText search_et;
    ImageButton search_but;
    BookListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.rec_list);
        search_but=findViewById(R.id.search_but);
        search_et=findViewById(R.id.search_et);
        progressDialog=new ProgressDialog(this);

        search_but.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String completeUrl=Utils.makeUrl(GOOGLE_BOOKS_API,search_et.getText().toString().trim())+"&maxResults=40";
                        BookAsyncTask task=new BookAsyncTask();
                        task.execute(completeUrl);
                    }
                }
        );
        mAdapter=new BookListAdapter(new ArrayList<BooksInfo>(),this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

public class BookAsyncTask extends AsyncTask<String,Void,List<BooksInfo>>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...\nplease wait");
        progressDialog.show();
    }

    @Override
    protected List<BooksInfo> doInBackground(String... strings) {
        List<BooksInfo> result;
        if(TextUtils.isEmpty(strings[0])||strings[0]==null){
            return null;
        }
        result=Utils.fetchDataFromBooks(strings[0]);
        return result;
    }

    @Override
    protected void onPostExecute(List<BooksInfo> booksInfos) {
        super.onPostExecute(booksInfos);
        progressDialog.dismiss();

        if(booksInfos.isEmpty()||booksInfos!=null){
            mAdapter=new BookListAdapter(booksInfos,MainActivity.this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }
    }
}
}
