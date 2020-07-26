package com.example.android.storehouse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.storehouse.data.StoreContract.StoreEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.R.attr.data;
import static android.R.attr.path;
import static android.R.attr.switchMinWidth;
import static android.provider.SyncStateContract.*;
import static com.example.android.storehouse.data.StoreProvider.LOG_TAG;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER = 0;
    private static final int REQUEST_CODE_GALLERY = 1;
    private File mFileTemp;
    private String path;
    private String imagePath;

    private final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri mCurrentItemUri;

    private EditText mNameEditText;

    private EditText mQuantityEditText;

    private EditText mPriceEditText;

    private ImageView mImageView;

    private Button mIncrement;
    private Button mDecrement;
    private EditText mCustName;

    /**
     * Boolean flag that keeps track of whether the item has been edited (true) or not (false)
     */
    private boolean mItemHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mItemHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new item or editing an existing one.
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        // If the intent DOES NOT contain a item content URI, then we know that we are
        // creating a new item.
        if (mCurrentItemUri == null) {
            // This is a new item, so change the app bar to say "Add an item"
            setTitle(getString(R.string.editor_activity_title_new_item));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a item that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing item, so change app bar to say "Edit item"
            setTitle(getString(R.string.editor_activity_title_edit_item));

            // Initialize a loader to read the item data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mImageView = (ImageView) findViewById(R.id.product_imageView);
        mIncrement = (Button) findViewById(R.id.increment);
        mDecrement = (Button) findViewById(R.id.decrement);


        mIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumOneToQuantity();
                mItemHasChanged = true;
            }
        });

        mDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractOneFromQuantity();
                mItemHasChanged = true;
            }
        });


        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mImageView.setOnTouchListener(mTouchListener);

        Button selectImageButton = (Button) findViewById(R.id.select_img_btn);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                checkForPermission();
                mItemHasChanged = true;
            }
        });

        Button orderButton = (Button) findViewById(R.id.order_btn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCustName = (EditText) findViewById(R.id.customer_name);
                String yourName = mCustName.getText().toString().trim();
                String nameString = mNameEditText.getText().toString().trim();
                String quantityString = mQuantityEditText.getText().toString().trim();
                String priceString = mPriceEditText.getText().toString().trim();

                String priceMessage = orderSummary(yourName, nameString, quantityString, priceString);

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Inventory order for " + yourName);
                intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    public String orderSummary(String name, String pdtName, String Q, String P) {

        String priceMessage = "NAME : " + name;
        priceMessage += "\nProduct name : " + pdtName;
        priceMessage += "\nQuantity : " + Q;
        priceMessage += "\nTotal Price : " + P;
        return priceMessage;
    }

    private void sumOneToQuantity() {
        String previousValueString = mQuantityEditText.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        mQuantityEditText.setText(String.valueOf(previousValue + 1));
    }

    private void subtractOneFromQuantity() {
        String previousValueString = mQuantityEditText.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            mQuantityEditText.setText(String.valueOf(previousValue - 1));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        try {


            Cursor cursor = getContentResolver().query(resultData.getData(), null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();


            mFileTemp = new File(path);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(EditorActivity.this).load(mFileTemp)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_add_shopping_cart)
                .crossFade()
                .centerCrop()
                .into(mImageView);

    }

    public void openImageSelector() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_GALLERY);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(EditorActivity.this, mPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(mPermission, 123);
        } else {
            openImageSelector();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            int len = permissions.length;
            for (int i = 0; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRotionale = shouldShowRequestPermissionRationale(permission);
                    if (!showRotionale) {
                        showDialogOK("storage permission req.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        Uri uri = Uri.fromParts("111", EditorActivity.this.getPackageName(),
                                                null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 123);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }, "NEVER_ASK");
                    } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        showDialogOK("storage permission req", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        checkForPermission();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }, "DENY");
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener, String from) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditorActivity.this)
                .setMessage(message);

        if (from.equals("DENY")) {
            alertDialog.setTitle("Gallery permission")
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("CANCEL", okListener)
                    .create()
                    .show();
        } else if (from.equals("NEVER_ASK")) {
            alertDialog.setTitle("Gallery permission")
                    .setPositiveButton("SETTING", okListener)
                    .setNegativeButton("CANCEL", okListener)
                    .create()
                    .show();
        }
    }

    /**
     * Get user input from editor and save item into database.
     */
    private void saveItem() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();

        // Check if this is supposed to be a new item
        // and check if all the fields in the editor are blank
        if (TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString) &&
                path == null) {
            // Since no fields were modified, we can return early without creating a new item.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            Toast.makeText(this, "All fields is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nameString)) {
            // Since no fields were modified, we can return early without creating a new item.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            Toast.makeText(this, "Name field is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, "Price field is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, "Quantity field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and item attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(StoreEntry.COLUMN_NAME, nameString);
        values.put(StoreEntry.COLUMN_PRICE, priceString);
        values.put(StoreEntry.COLUMN_QUANTITY, quantityString);
        values.put(StoreEntry.COLUMN_IMAGE, String.valueOf(path));

        if (mCurrentItemUri == null) {

            if (mFileTemp == null) {
                Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
                return;
            }

            values.put(StoreEntry.COLUMN_IMAGE, String.valueOf(path));

            Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            if (mFileTemp != null) {
                values.put(StoreEntry.COLUMN_IMAGE, String.valueOf(mFileTemp));
            } else if (imagePath != null) {
                values.put(StoreEntry.COLUMN_IMAGE, String.valueOf(imagePath));
            }

            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new item, hide the "Delete" menu item.
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save item to database
                saveItem();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the item hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this item.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the item.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the item in the database.
     */
    private void deleteItem() {

        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }


    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all item attributes, define a projection that contains
        // all columns from the item table
        String[] projection = {
                StoreEntry._ID,
                StoreEntry.COLUMN_NAME,
                StoreEntry.COLUMN_QUANTITY,
                StoreEntry.COLUMN_PRICE,
                StoreEntry.COLUMN_IMAGE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,         // Query the content URI for the current item
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of item attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
            int imageColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_IMAGE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            Integer quantity = cursor.getInt(quantityColumnIndex);
            Float price = cursor.getFloat(priceColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPriceEditText.setText(Float.toString(price));
            imagePath = cursor.getString(imageColumnIndex);
            Glide.with(EditorActivity.this).load(cursor.getString(imageColumnIndex))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_add_shopping_cart)
                    .crossFade()
                    .centerCrop()
                    .into(mImageView);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mQuantityEditText.setText("0");
        mPriceEditText.setText("");

    }
}
