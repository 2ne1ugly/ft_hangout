package com.example.hangout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getByteArrayAsBitmap(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    private ContactManager() {}
    public static ContactManager _instance;
    public static ContactManager instance() {
        if (_instance == null) {
            _instance = new ContactManager();
            _instance.updateContacts();
        }
        return _instance;
    }


    public List<Contact> _items;

    public Contact newContact(String name, String number, String description, Bitmap bitmap) {
        if (_items == null)
            updateContacts();
        SQLiteDatabase db = AppManager.Instance().getContactManager().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_NAME, name);
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_NUMBER, number);
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_DESCRIPTION, description);
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_BITMAP, getBitmapAsByteArray(bitmap));

        Contact contact = new Contact(
                name,
                number,
                description,
                (int) db.insert(ContactSQLHelper.Entry.TABLE_NAME, null, values),
                bitmap);
        _items.add(contact);
        return contact;
    }

    public void updateContacts() {
        SQLiteDatabase db = AppManager.Instance().getContactManager().getReadableDatabase();
        Cursor cursor = db.query(
                ContactSQLHelper.Entry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        _items = new ArrayList<Contact>();
        while(cursor.moveToNext()) {
            Contact contact = new Contact(
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactSQLHelper.Entry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactSQLHelper.Entry.COLUMN_NAME_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactSQLHelper.Entry.COLUMN_NAME_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(ContactSQLHelper.Entry._ID)),
                    getByteArrayAsBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow(ContactSQLHelper.Entry.COLUMN_NAME_BITMAP))));
            _items.add(contact);
        }
        cursor.close();
    }

    public List<Contact> getContacts() {
        if (_items == null)
            updateContacts();
        return _items;
    }

    public Contact getContact(int id) {
        if (_items == null)
            updateContacts();
        for (Contact contact : _items) {
            if (contact.getID() == id)
                return contact;
        }
        return null;
    }

    public Contact getContactByNumber(String number) {
        updateContacts();
        for (Contact contact : _items) {
            if (contact.getNumber().equals(number)) {
                return contact;
            }
        }
        return null;
    }

    public static void editContact(Contact contact) {
        SQLiteDatabase db = AppManager.Instance().getContactManager().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_NUMBER, contact.getNumber());
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_DESCRIPTION, contact.getDescription());
        values.put(ContactSQLHelper.Entry.COLUMN_NAME_BITMAP, getBitmapAsByteArray(contact.getImage()));

// Which row to update, based on the title
        String selection = ContactSQLHelper.Entry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(contact.getID()) };

        int count = db.update(
                ContactSQLHelper.Entry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public static void deleteContact(Contact contact) {
        SQLiteDatabase db = AppManager.Instance().getContactManager().getWritableDatabase();
        String selection = ContactSQLHelper.Entry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(contact.getID())};
        int deletedRows = db.delete(ContactSQLHelper.Entry.TABLE_NAME, selection, selectionArgs);
    }
}
