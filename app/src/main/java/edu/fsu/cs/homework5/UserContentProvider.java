package edu.fsu.cs.homework5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class UserContentProvider extends ContentProvider {
    public final static String DBNAME = "hw5.db";
    public final static String TABLE_NAME = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://edu.fsu.cs.homework5.provider/users" );

    //String values of table name and field names for the Users table
    public static String empId = "empid";
    public static String password = "password";
    public static String name = "name";
    public static String email = "email";
    public static String gender = "gender";
    public static String department = "department";
    public static String lastlogin = "lastlogin";

    // SQLite Database Tables
    private static final String SQL_CREATE_USERS = "CREATE TABLE " + TABLE_NAME + " (" + empId + " TEXT PRIMARY KEY, "
            + password +" TEXT,"
            + name + " TEXT,"
            + email + " TEXT,"
            + gender + " TEXT,"
            + lastlogin + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + department + " TEXT)";

    MainDatabaseHelper mOpenHelper;

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("tag", SQL_CREATE_USERS);
            db.execSQL(SQL_CREATE_USERS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//            onCreate(db);
        }
    }

    public UserContentProvider() {
    }

    @Override
    public boolean onCreate() {
//        getContext().deleteDatabase(DBNAME);
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        Log.i("tag", uri.toString());
        Log.i("tag", values.getAsString(empId));
        String eid = values.getAsString(empId);
        if (eid == null || eid.equals("")){
            return null;
        }

        Cursor cursor = mOpenHelper.getReadableDatabase().query(TABLE_NAME, null, null, null,null, null, null);
        String[] columnNames = cursor.getColumnNames();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnNames.length; i++)
            sb.append(columnNames[i] + ", ");
        Log.i("tag", sb.toString());
        cursor.close();
        id = mOpenHelper.getWritableDatabase().insert(TABLE_NAME, null, values); // maybe change to string var

        if (id == -1) {
            Log.d("Error", "Insert failed to DB");
            return null;
        }

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        if (uri.toString().equals(CONTENT_URI.toString())) {
            return mOpenHelper.getWritableDatabase().delete("Users", selection, selectionArgs);
        }
        else
            throw new UnsupportedOperationException("Delete Failed");
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //start up here to do the query
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        String tableName = uri.getLastPathSegment();
        if(tableName.equals(TABLE_NAME)){
            return mOpenHelper.getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);
        }
        else{
            throw new UnsupportedOperationException("Incorrect URI for Query");
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        String tableName = uri.getLastPathSegment();
        if(tableName.equals(TABLE_NAME)){
            return mOpenHelper.getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
        }
        else{
            throw new UnsupportedOperationException("Incorrect URI for Update");
        }
    }
}