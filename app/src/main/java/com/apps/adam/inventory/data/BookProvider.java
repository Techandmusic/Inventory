package com.apps.adam.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {
    //Tag for log messages
    public static final String LOG_TAG = BookProvider.class.getSimpleName();
    //URI matcher code for the books table
    private static final int BOOKS = 100;
    //URI matcher code for individual books in the table
    private static final int BOOK_ID = 101;
    //URI matcher object
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Static initializer
    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    //Initializer provider and database helper
    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //Create cursor to hold query results
        Cursor cursor = null;
        //Figure out if the uri matcher matches a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = db.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

    }

    private Uri insertBook(Uri uri, ContentValues values) {
        //Extract values making sure that fields are not null
        String title = values.getAsString(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        if (title == null) {
            throw new IllegalArgumentException("Book requires a title");
        }
        String author = values.getAsString(BookContract.BookEntry.COLUMN_AUTHOR_NAME);
        if (author == null) {
            throw new IllegalArgumentException("You must enter an author");
        }
        Double price = values.getAsDouble(BookContract.BookEntry.COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Please enter sale price or 0.00 if book is free");
        }
        int quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
        if (quantity == 0) {
            throw new IllegalArgumentException("Please enter quantity in stock");
        }
        String supplierName = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Please enter name of supplier");
        }
        String supplierPhone = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE);
        if (supplierPhone == null) {
            throw new IllegalArgumentException("Please enter phone number for supplier");
        }
        //Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for: " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                //Delete all rows that match the selection and selectionArgs
                rowsDeleted = db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                //Delete a single row based on ID from the Uri
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

        //If 1 or more rows were deleted notify all listeneres that the data has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }



    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                //Extract ID from Uri so we know whcih row to update
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BookContract.BookEntry.COLUMN_PRODUCT_NAME)) {
            String title = values.getAsString(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
            if (title == null) {
                throw new IllegalArgumentException("Book must have a valid title");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_AUTHOR_NAME)) {
            String author = values.getAsString(BookContract.BookEntry.COLUMN_AUTHOR_NAME);
            if (author == null) {
                throw new IllegalArgumentException("Book must have an author");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_PRICE)) {
            Double price = values.getAsDouble(BookContract.BookEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Please enter sale price or 0.00 if book is free");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_QUANTITY)) {
            int quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
            if (quantity == 0) {
                throw new IllegalArgumentException("Please enter quantity in stock");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Please enter supplier name");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE)) {
            String supplierPhone = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("Please enter phone number for supplier");
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        //Otherwise get a writable database to update the data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
