package com.example.android.storehouse;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.storehouse.data.StoreContract;
import com.example.android.storehouse.data.StoreContract.StoreEntry;
import com.example.android.storehouse.data.StoreDbHelper;

import static android.R.attr.name;
import static android.net.Uri.parse;

/**
 * Created by hp on 8/29/2017.
 */

public class StoreCursorAdapter extends CursorAdapter {

    private Context mContext;

    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView nameView = (TextView) view.findViewById(R.id.product_name_text_view);
        TextView priceView = (TextView) view.findViewById(R.id.product_price_text_view);
        final TextView quantityView = (TextView) view.findViewById(R.id.product_quantity_text_view);
        ImageView imgView = (ImageView) view.findViewById(R.id.product_imageView);

        // Extract properties from cursor
        // Find the columns of inventory attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
        int quantityIndex = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
        int imgIndex = cursor.getColumnIndex(StoreEntry.COLUMN_IMAGE);
        int rowIndex = cursor.getColumnIndex(StoreEntry._ID);

        // Read the product attributes from the Cursor for the current product
        final String productName = cursor.getString(nameColumnIndex);
        final int productPrice = cursor.getInt(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityIndex);
        //Uri imgUri = parse(cursor.getString(imgIndex));
        final int rowId = cursor.getInt(rowIndex);

        // Populate fields with extracted properties
        nameView.setText(productName);
        priceView.setText(Integer.toString(productPrice));
        quantityView.setText(Integer.toString(productQuantity));
        Glide.with(context).load(cursor.getString(imgIndex))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_add_shopping_cart)
                .crossFade()
                .centerCrop()
                .into(imgView);

        Button sellButton = (Button) view.findViewById(R.id.sell_product);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StoreDbHelper dbHelper = new StoreDbHelper(context);
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                int items = Integer.parseInt(quantityView.getText().toString());
                if (items > 0) {
                    int mQuantitySold = items - 1;
                    ContentValues values = new ContentValues();
                    values.put(StoreEntry.COLUMN_QUANTITY, mQuantitySold);
                    String selection = StoreEntry._ID + "=?";
                    String[] selectionArgs = new String[]{String.valueOf(rowId)};
                    int rowsAffected = database.update(StoreEntry.TABLE_NAME, values, selection, selectionArgs);
                    if (rowsAffected != -1) {
                        quantityView.setText(Integer.toString(mQuantitySold));
                    }
                } else
                    Toast.makeText(context, "No Stock Left ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
