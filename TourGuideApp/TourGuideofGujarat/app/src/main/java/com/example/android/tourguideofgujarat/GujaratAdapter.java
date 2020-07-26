package com.example.android.tourguideofgujarat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 8/5/2017.
 */

public class GujaratAdapter extends ArrayAdapter<Gujarat> {

    public int mColorResourseId;

    public GujaratAdapter(Activity context, ArrayList<Gujarat> word, int colorResourseId){

        super(context, 0 , word);
        mColorResourseId = colorResourseId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Gujarat currentWord = getItem(position);

        TextView headTextView = (TextView) listItemView.findViewById(R.id.heading);
        headTextView.setText(currentWord.getHeading());

        TextView detailTextView = (TextView) listItemView.findViewById(R.id.details);
        detailTextView.setText(currentWord.getDeatails());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if (currentWord.hasImage()) {
            imageView.setImageResource(currentWord.getImageResourseId());
            imageView.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourseId);
        textContainer.setBackgroundColor(color);

        return listItemView;
    }
}
