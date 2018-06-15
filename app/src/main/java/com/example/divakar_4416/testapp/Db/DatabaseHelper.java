package com.example.divakar_4416.testapp.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String dataBaseName = "ContactDb";  //NO I18N


    Context c;

    public DatabaseHelper(Context context) {
        super(context, dataBaseName, null, 1);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

		/*db.execSQL("CREATE TABLE serverdetailstable(servername Text PRIMARY KEY,"		//NO I18N
				+ "domainname"				//NO I18N
				+ " TEXT ,"			//NO I18N
				+ "username" + " TEXT ," + "password" + " TEXT ,UNIQUE (servername))"); 	//NO I18N
*/


        db.execSQL("CREATE TABLE notes( id INTEGER PRIMARY KEY   AUTOINCREMENT ,phone Text,name Text,notes Text,UNIQUE (id))"); 	//NO I18N
        db.execSQL("CREATE TABLE remainder(id INTEGER PRIMARY KEY   AUTOINCREMENT ,phone Text,name Text,reminder Text,time Text,UNIQUE (id))"); 	//NO I18N
        db.execSQL("CREATE TABLE sms_schedule(id INTEGER PRIMARY KEY   AUTOINCREMENT ,phone Text,name Text,sms Text,time Text,UNIQUE (id))"); 	//NO I18N

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addNotes(String phone,String name,String notes) {
        long failure = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("name", name);
        contentValues.put("notes", notes);
        failure = db.insert("notes", null, contentValues); 	//NO I18N

        db.close();
        return failure;

    }
    public int getRowCount(){
        int rowcount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from notes", null);		//NO I18N
        rowcount = cursor.getCount();
        return rowcount;
    }
    public ArrayList<String> getNotes(String phone) {

        ArrayList <String> sal = new ArrayList<String>();
        String[] columns ={"phone", "name","notes"};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("notes", columns, "phone=?", new String[] { phone }, null, null, null);
        if ((cursor.getCount() != 0) &&  (cursor.moveToFirst())) {
            do {
                String note =cursor.getString(cursor.getColumnIndex("notes"));
                sal.add(note);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sal;
    }
}
