package com.example.android.booklistingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rupali on 14-09-2016.
 */
public class bookInfoAdapter extends ArrayAdapter<bookInfo> {
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final bookInfo currentBook = getItem(position);
        //check if existing view is being reused, otherwise inflate the new
        View listitemView = convertView;

        if(listitemView == null)

        {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.bookinfo,parent,false);
        }

        TextView bookName = (TextView) listitemView.findViewById(R.id.textViewBookName);
        bookName.setText(currentBook.getBookName());

        TextView authorName = (TextView)listitemView.findViewById(R.id.textViewAuthorName);
        authorName.setText(currentBook.getAuthor());

        TextView publisher = (TextView) listitemView.findViewById(R.id.textViewPublisher);
        publisher.setText(currentBook.getPublisher());

        TextView publishedDate = (TextView) listitemView.findViewById(R.id.textViewPublishedDate);
        publishedDate.setText(currentBook.getPublishedDate());

        ImageView imageview = (ImageView) listitemView.findViewById(R.id.imageViewBookIcon);
        Picasso.with(getContext()).load(String.valueOf(currentBook.getImage())).into(imageview);

        return listitemView;

    }

    public bookInfoAdapter(Activity context, ArrayList<bookInfo> bookList)
    {
        super(context,0,bookList);

    }
}
