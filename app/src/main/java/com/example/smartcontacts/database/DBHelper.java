package com.example.smartcontacts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartcontacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contacts.db";
    private static final int DB_VERSION = 2; // Incremented version
    private static final String TABLE_NAME = "contacts";
    private static final String COL_ID = "id";
    private static final String COL_FIRST_NAME = "firstName";
    private static final String COL_LAST_NAME = "lastName";
    private static final String COL_COMPANY = "company";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FIRST_NAME + " TEXT," +
                COL_LAST_NAME + " TEXT," +
                COL_COMPANY + " TEXT," +
                COL_PHONE + " TEXT," +
                COL_EMAIL + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, contact.getFirstName());
        values.put(COL_LAST_NAME, contact.getLastName());
        values.put(COL_COMPANY, contact.getCompany());
        values.put(COL_PHONE, contact.getPhone());
        values.put(COL_EMAIL, contact.getEmail());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, contact.getFirstName());
        values.put(COL_LAST_NAME, contact.getLastName());
        values.put(COL_COMPANY, contact.getCompany());
        values.put(COL_PHONE, contact.getPhone());
        values.put(COL_EMAIL, contact.getEmail());
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID, COL_FIRST_NAME, COL_LAST_NAME, COL_COMPANY, COL_PHONE, COL_EMAIL},
                COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Contact contact = new Contact(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
            db.close();
            return contact;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
