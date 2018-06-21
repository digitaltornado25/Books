package com.tornado.booklist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BooksViewHolder> {

    List<BooksInfo> bookList;
    LayoutInflater inflater;
    public BookListAdapter(List<BooksInfo> bookList, Context context){
        inflater=LayoutInflater.from(context);
        this.bookList=bookList;
    }
    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.list_item_view,parent,false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bookList.get(position).image);
        if(bookList.get(position).Author!=null)
            holder.author.setText(bookList.get(position).Author[0]);
        holder.title.setText(bookList.get(position).Title);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}

class BooksViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView title,author;
    public BooksViewHolder(View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.image);
        title=itemView.findViewById(R.id.title);
        author=itemView.findViewById(R.id.author);
    }
}
