package com.example.panguangyi.contentproviderdemo0203;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.lang.ref.WeakReference;

public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.example.panguangyi.contentproviderdemo0203.BookProvider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    private static class SafeHandler extends Handler {
        private WeakReference<Context> mRef;
        public SafeHandler(Context context) {
            mRef = new WeakReference<Context>(context);
        }
    }

    private SafeHandler mHandler = new SafeHandler(getContext());

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.d(TAG,"onCreate,current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        initDB();
        return true;
    }

    private void initDB() {
        mDb = new DBOpenHelper(mContext).getWritableDatabase();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mDb.execSQL("delete from " + DBOpenHelper.BOOK_TABLE_NAME);
//                mDb.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);
//                mDb.execSQL("insert into book values(2,'android')");
//                mDb.execSQL("insert into book values(3,'java')");
//                mDb.execSQL("insert into book values(4,'python')");
//                mDb.execSQL("insert into user values(1,'jack',1)");
//                mDb.execSQL("insert into user values(2,'lose',0)");
//            }
//        });
        mDb.execSQL("delete from " + DBOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(2,'android')");
        mDb.execSQL("insert into book values(3,'java')");
        mDb.execSQL("insert into book values(4,'python')");
        mDb.execSQL("insert into user values(1,'jack',1)");
        mDb.execSQL("insert into user values(2,'lose',0)");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG,"delete,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        int count = mDb.delete(table,selection,selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG,"insert,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        mDb.insert(table,null,values);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG,"query,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG,"update,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        int row = mDb.update(table,values,selection,selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG,"getType");
        return null;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DBOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.USER_TABLE_NAME;
                break;
            default:break;
        }
        return tableName;
    }
}
