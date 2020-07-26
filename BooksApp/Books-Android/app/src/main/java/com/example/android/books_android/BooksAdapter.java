package com.example.android.books_android;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by hp on 8/12/2017.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter(Activity context, ArrayList<Books> books){
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item , parent, false);
        }

        Books currentItem = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentItem.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        authorTextView.setText(currentItem.generateStringOfAuthor().toString());

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description_text_view);
        descriptionTextView.setText(currentItem.getDescription());

        return listItemView;
    }
}
