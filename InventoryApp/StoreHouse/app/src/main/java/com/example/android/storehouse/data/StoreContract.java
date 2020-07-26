package com.example.android.storehouse.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by hp on 8/29/2017.
 */

public class StoreContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.storehouse";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STOREHOUSE = "storehouse";

    private StoreContract() {
    }

    public static final class StoreEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STOREHOUSE);

        public static final String TABLE_NAME = "items";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "image";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOREHOUSE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOREHOUSE;

    }
}
