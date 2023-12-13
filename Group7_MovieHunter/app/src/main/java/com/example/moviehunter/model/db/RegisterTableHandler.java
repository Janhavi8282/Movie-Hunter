package com.example.moviehunter.model.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.moviehunter.model.Movie;
import com.example.moviehunter.model.Users;
import com.example.moviehunter.model.db.DBHelper;
import com.example.moviehunter.model.db.TableHandler;

import java.util.ArrayList;
import java.util.List;

public class RegisterTableHandler implements TableHandler {
    static final String TABLE_NAME="Users";
    static final String COL_ID="Id";
    static final String COL_NAME="Name";
    static final String COL_EMAIL="email";
    static final String COL_PHONE="phone";
    static final String COL_GENDER="gender";
    static final String COL_IMAGE="image";
    static final String COL_PASSWORD= "Password";
    static final String COL_ISUSER = "isUser";
    static final String COL_ICON = "icon";

    //create table Users
    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT, " +
            COL_EMAIL + " TEXT, " +
            COL_PHONE + " TEXT, " +
            COL_GENDER + " TEXT, " +
            COL_IMAGE + " TEXT, " +
            COL_PASSWORD + " TEXT, " +
            COL_ICON + " BLOB, " +
            COL_ISUSER + " BOOLEAN); ";

    //drop table
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
       // insertUsers(db, getTestData());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //insert users to database
    public boolean insertUsers(SQLiteDatabase db, Users users) {
        ContentValues cv = new ContentValues();

        cv.put(COL_ID, users.getId());
        cv.put(COL_NAME, users.getName());
        cv.put(COL_PASSWORD, users.getPassword());
        cv.put(COL_ISUSER, users.isUser());
        cv.put(COL_EMAIL, users.getEmail());
        cv.put(COL_PHONE, users.getPhone());
        cv.put(COL_GENDER, users.getGender());
        if (users.getIcon() != null) {
            cv.put(COL_ICON, DBBitmapUtility.getBytes(users.getIcon()));
        }
        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    // update User from profile
    public boolean updateUsers(SQLiteDatabase db, Users users) {
        ContentValues cv = new ContentValues();
        String[] selectionArgs = {String.valueOf(users.getId())};
        //"UPDATE Users SET gender=?,isUser=?,Id=?,Name=?,email=?,image=?,phone=?,Password=? WHERE Id = ?"
        cv.put(COL_ID, users.getId());
        cv.put(COL_NAME, users.getName());
        cv.put(COL_EMAIL, users.getEmail());
        cv.put(COL_PHONE, users.getPhone());
        cv.put(COL_GENDER, users.getGender());
        cv.put(COL_IMAGE, users.getImage());
        if (users.getIcon() != null)
            cv.put(COL_ICON, DBBitmapUtility.getBytes(users.getIcon()));
        long result = db.update(TABLE_NAME, cv, COL_ID + " = ?" , selectionArgs);
        return result != -1;
    }
    // Get user data from user ID
    @SuppressLint({"Recycle", "Range"})
    public Users getUsers(SQLiteDatabase db, String strUserName) {

        Cursor cursorObj;
        Users user = new Users();
        String[] selectionArgs = {strUserName};
        String sql = "select " +COL_ID + " as _id, "+COL_NAME  +" , " + COL_EMAIL+ " , " + COL_PHONE + " , " + COL_GENDER
                +" , " + COL_ICON + " , " + COL_IMAGE + " from "+ TABLE_NAME + " where " + COL_NAME +" = ? " ;
        cursorObj = db.rawQuery(sql , selectionArgs);

        if(cursorObj.getCount() > 0){
            if (cursorObj.moveToFirst()) {
                do {
                    user.setName(cursorObj.getString(cursorObj.getColumnIndex(COL_NAME)));//adding 2nd column data
                    user.setEmail(cursorObj.getString(cursorObj.getColumnIndex(COL_EMAIL)));
//                    user.setPassword(cursorObj.getString(cursorObj.getColumnIndex(COL_PASSWORD)));
                    user.setPhone(cursorObj.getString(cursorObj.getColumnIndex(COL_PHONE)));
                    user.setGender(cursorObj.getString(cursorObj.getColumnIndex(COL_GENDER)));
                    byte[] bytes = cursorObj.getBlob(cursorObj.getColumnIndex(COL_ICON));
                    if (bytes != null)
                        user.setIcon(DBBitmapUtility.getImage(bytes));
                } while (cursorObj.moveToNext());
            }
            return user;
        }else
            return null;
    }

    //
    //check if the user exist in the database or not
    public boolean checkUser(String username, String password, DBHelper dbHelper)
    {
        String[] columns = {COL_ID};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = COL_NAME + "=?" + " and " + COL_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        //db.close();

        if(count >0)
            return true;
        else
            return false;

    }

}
