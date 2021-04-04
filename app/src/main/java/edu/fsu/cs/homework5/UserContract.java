package edu.fsu.cs.homework5;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class UserContract {
    static String[] mProjection;
    static String[] mListColumns;
    static String mSelectionClause;
    static String[] mSelectionArgs;
    static String mOrderBy;
    static int[] mListItems;

    static Cursor mCursor;
    static CursorAdapter mCursorAdapter;

    public static boolean checkUser(Context context, String empId, String password) {
        mSelectionClause = UserContentProvider.empId +
                " = ? AND " + UserContentProvider.password +
                " = ? ";
        Log.i("tag", mSelectionClause + ": " + empId + ", " + password);
        mSelectionArgs = new String[] { empId, password };

        mCursor = context.getContentResolver().query(
                UserContentProvider.CONTENT_URI,
                null,
                mSelectionClause,
                mSelectionArgs,
                null);
        boolean exist  = mCursor != null && mCursor.getCount() == 1;
        mCursor.close();
       return exist;
    }

    public static void addUser(Context context, ContentValues values) {
        Uri insert = context.getContentResolver().insert(UserContentProvider.CONTENT_URI, values);
        if (insert == null){
            Toast.makeText(context, "Register Failed: Username already taken.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "Register Successful", Toast.LENGTH_LONG).show();
        }
    }

    public static void deleteUser(Context context, String empId) {
        mSelectionClause = UserContentProvider.empId + " = ?";
        mSelectionArgs = new String[] {empId};
        context.getContentResolver().delete(UserContentProvider.CONTENT_URI, mSelectionClause, mSelectionArgs);
    }

    public static Cursor queryOneUser(Context context, String empId) {
        mProjection = new String[] {
                UserContentProvider.empId,
                UserContentProvider.name,
                UserContentProvider.password,
                UserContentProvider.email,
                UserContentProvider.department,
                UserContentProvider.lastlogin
        };

        mSelectionClause = UserContentProvider.empId +
                " = ? ";
        mSelectionArgs = new String[] { empId };

        return context.getContentResolver().query(
                UserContentProvider.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);
    }

    public static Cursor queryOtherUsers(Context context, String empId) {
        mProjection = new String[] {
                UserContentProvider.empId,
                UserContentProvider.email,
                UserContentProvider.lastlogin
        };

        mSelectionClause = UserContentProvider.empId +
                " != ?";
        mSelectionArgs = new String[] { empId };

        return context.getContentResolver().query(
                UserContentProvider.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);
    }

}
