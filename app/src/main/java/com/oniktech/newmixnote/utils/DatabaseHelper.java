package com.oniktech.newmixnote.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "SimpleNote.db";

    private static final String NOTE_TABLE_NAME = "note";
    private static final String TEXTNOTE_TABLE_NAME = "textNote";
    private static final String VOICENOTE_TABLE_NAME = "voiceNote";
    private static final String CHECKLIST_TABLE_NAME = "checkList";
    private static final String PICTURENOTE_TABLE_NAME = "pictureNote";

    private static final String NOTE_COLUMN_ID = "id";
    private static final String NOTE_COLUMN_TITLE = "title";
    private static final String NOTE_COLUMN_TYPE = "type";
    private static final String NOTE_COLUMN_REMEMBER_TIME = "rememberTime";
    private static final String NOTE_COLUMN_HAVE_REMINDER = "haveReminder";

    private static final String TEXTNOTE_COLUMN_ID = "id";
    private static final String TEXTNOTE_COLUMN_NOTEID = "noteId";
    private static final String TEXTNOTE_COLUMN_TEXT = "text";

    private static final String VOICENOTE_COLUMN_ID = "id";
    private static final String VOICENOTE_COLUMN_NOTEID = "noteId";
    private static final String VOICENOTE_COLUMN_NAME = "voiceName";
    private static final String VOICENOTE_COLUMN_ADDRESS = "voiceAddress";

    private static final String CHECKLIST_COLUMN_ID = "id";
    private static final String CHECKLIST_COLUMN_NOTEID = "noteId";
    private static final String CHECKLIST_COLUMN_CHECKNAME = "checkName";
    private static final String CHECKLIST_COLUMN_CHECKED = "checked";

    private static final String PICTURENOTE_COLUMN_ID = "id";
    private static final String PICTURENOTE_COLUMN_NOTEID = "noteId";
    private static final String PICTURENOTE_COLUMN_NAME = "pictureName";
    private static final String PICTURENOTE_COLUMN_ADDRESS = "pictureAddress";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //all of tables have a foreign key to note table
            db.execSQL("create table if not exists " + NOTE_TABLE_NAME + "(" +
                    NOTE_COLUMN_ID + " integer primary key AUTOINCREMENT NOT NULL," +
                    NOTE_COLUMN_TITLE + " Text," +
                    NOTE_COLUMN_TYPE + " Text," +
                    NOTE_COLUMN_HAVE_REMINDER + " Text," +
                    NOTE_COLUMN_REMEMBER_TIME + " Text )"
            );
            db.execSQL("create table if not exists " + TEXTNOTE_TABLE_NAME + "(" +
                    TEXTNOTE_COLUMN_ID + " INTEGER primary key AUTOINCREMENT NOT NULL," +
                    TEXTNOTE_COLUMN_NOTEID + " INTEGER," +
                    TEXTNOTE_COLUMN_TEXT + " Text" +
                    ",CONSTRAINT fk_textnote FOREIGN KEY (" +
                    TEXTNOTE_COLUMN_NOTEID + ") REFERENCES " +
                    NOTE_TABLE_NAME + " (" + NOTE_COLUMN_ID + ") )"
            );
            db.execSQL("create table if not exists " + VOICENOTE_TABLE_NAME + "(" +
                    VOICENOTE_COLUMN_ID + " integer primary key AUTOINCREMENT NOT NULL," +
                    VOICENOTE_COLUMN_NOTEID + " integer," +
                    VOICENOTE_COLUMN_NAME + " Text," +
                    VOICENOTE_COLUMN_ADDRESS + " Text" +
                    ",CONSTRAINT fk_voicenote FOREIGN KEY (" +
                    VOICENOTE_COLUMN_NOTEID + ") REFERENCES " +
                    NOTE_TABLE_NAME + " (" + NOTE_COLUMN_ID + ") )"
            );
            db.execSQL("create table if not exists " + CHECKLIST_TABLE_NAME + "(" +
                    CHECKLIST_COLUMN_ID + " integer primary key AUTOINCREMENT NOT NULL," +
                    CHECKLIST_COLUMN_NOTEID + " integer," +
                    CHECKLIST_COLUMN_CHECKNAME + " Text," +
                    CHECKLIST_COLUMN_CHECKED + " Text" +
                    ",CONSTRAINT fk_checknote FOREIGN KEY (" +
                    CHECKLIST_COLUMN_NOTEID + ") REFERENCES " +
                    NOTE_TABLE_NAME + " (" + NOTE_COLUMN_ID + ") )"
            );
            db.execSQL("create table if not exists " + PICTURENOTE_TABLE_NAME + "(" +
                    PICTURENOTE_COLUMN_ID + " integer primary key AUTOINCREMENT NOT NULL," +
                    PICTURENOTE_COLUMN_NOTEID + " integer," +
                    PICTURENOTE_COLUMN_NAME + " Text," +
                    PICTURENOTE_COLUMN_ADDRESS + " Text" +
                    ",CONSTRAINT fk_picnote FOREIGN KEY (" +
                    PICTURENOTE_COLUMN_NOTEID + ") REFERENCES " +
                    NOTE_TABLE_NAME + " (" + NOTE_COLUMN_ID + ") )"
            );
        }catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ NOTE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TEXTNOTE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ VOICENOTE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ CHECKLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ PICTURENOTE_TABLE_NAME);
        onCreate(db);
    }

   public void insertTextNote(String title,String text) {

       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(NOTE_COLUMN_TITLE, title);
       contentValues.put(NOTE_COLUMN_TYPE, "TextNote");
       db.insert(NOTE_TABLE_NAME, null, contentValues);

       db = this.getReadableDatabase();
       //get last created id from table note
       Cursor res = db.rawQuery("select last_insert_rowid() as id from " + NOTE_TABLE_NAME, null);
       //for set foreign key in textnote table
       if (!res.isAfterLast()) {
           if (res.getCount() > 0) {
               res.moveToNext();
               db = this.getWritableDatabase();
               contentValues = new ContentValues();
               contentValues.put(TEXTNOTE_COLUMN_TEXT, text);
               contentValues.put(TEXTNOTE_COLUMN_NOTEID, res.getInt(res.getColumnIndex("id")));
               db.insert(TEXTNOTE_TABLE_NAME, null, contentValues);
           }
       }
       res.close();
   }


    public ArrayList<Note> getData() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes= new ArrayList<Note>();
        //for showing newer data in top of list
        Cursor result = db.rawQuery("select * from "+ NOTE_TABLE_NAME +" order by "+NOTE_COLUMN_ID+" desc", null);

        while(result.moveToNext()){
            Note note=new Note(result.getInt(result.getColumnIndex(NOTE_COLUMN_ID)),
                    result.getString(result.getColumnIndex(NOTE_COLUMN_TITLE)),
                    result.getString(result.getColumnIndex(NOTE_COLUMN_TYPE)));
            notes.add(note);

        }result.close();

        return notes;
    }

    public ArrayList<CheckList> getCheckListData(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CheckList> checkLists= new ArrayList<CheckList>();
        Cursor result = db.rawQuery("select * from "+ CHECKLIST_TABLE_NAME +" where "+CHECKLIST_COLUMN_NOTEID+" = "+noteId, null);

        while(result.moveToNext()){
            CheckList checkList=new CheckList(result.getInt(result.getColumnIndex(CHECKLIST_COLUMN_ID)),
                    result.getInt(result.getColumnIndex(CHECKLIST_COLUMN_NOTEID)),
                    result.getString(result.getColumnIndex(CHECKLIST_COLUMN_CHECKNAME)),
                    result.getString(result.getColumnIndex(CHECKLIST_COLUMN_CHECKED)));
            checkLists.add(checkList);

        }result.close();
        return checkLists;
    }

    public String getTextSummary(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String summary="";
        //get first 30 character of text to show in main activity
        Cursor cursor = db.rawQuery(" select "+TEXTNOTE_COLUMN_TEXT+",  substr( "+TEXTNOTE_COLUMN_TEXT+",0 , 32) as summary from "+
                TEXTNOTE_TABLE_NAME+" where "+TEXTNOTE_COLUMN_NOTEID+" = "+
                noteId, null);
        if(cursor.moveToNext())
        {
            if(cursor.getString(cursor.getColumnIndex(TEXTNOTE_COLUMN_TEXT)).length()> cursor.getString(cursor.getColumnIndex("summary")).length())
                summary = cursor.getString(cursor.getColumnIndex("summary"))+" ...";
            else {
                summary = cursor.getString(cursor.getColumnIndex("summary"));
            }
        }
        cursor.close();

        return summary;
    }


    public CheckList insertChecklistNote(String title,String text,String checked) {
        CheckList checklistItem;
        int noteId=-1,id=-1;
        //insert a new note
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_TYPE, "CheckList");
        db.insert(NOTE_TABLE_NAME, null, contentValues);
        //get id of created note
        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select last_insert_rowid() as id from " + NOTE_TABLE_NAME, null);
        if (!res.isAfterLast()) {
            if (res.getCount() > 0) {
                res.moveToNext();//must be first
                noteId=res.getInt(res.getColumnIndex("id"));
                db = this.getWritableDatabase();
                contentValues = new ContentValues();
                contentValues.put(CHECKLIST_COLUMN_CHECKNAME, text);
                contentValues.put(CHECKLIST_COLUMN_NOTEID, noteId);
                contentValues.put(CHECKLIST_COLUMN_CHECKED, checked);
                //insert a new checklist item for note
                db.insert(CHECKLIST_TABLE_NAME, null, contentValues);
            }
        }
        res.close();
        //give id of checklist item
        db = this.getReadableDatabase();
        res = db.rawQuery("select last_insert_rowid() as id from " + CHECKLIST_TABLE_NAME, null);
        if (!res.isAfterLast()) {
            if (res.getCount() > 0) {
                res.moveToNext();
                id=res.getInt(res.getColumnIndex("id"));
            }
        }
        res.close();

        checklistItem=new CheckList(id,noteId,text,"false");

        return checklistItem;
    }

    public CheckList insertChecklistItem(int noteId, String text, String checked,String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id=-1;
        CheckList checklistItem;
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKLIST_COLUMN_CHECKNAME, text);
        contentValues.put(CHECKLIST_COLUMN_NOTEID, noteId);
        contentValues.put(CHECKLIST_COLUMN_CHECKED, checked);
        db.insert(CHECKLIST_TABLE_NAME, null, contentValues);

        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select last_insert_rowid() as id from " + CHECKLIST_TABLE_NAME, null);
        if (!res.isAfterLast()) {
            if (res.getCount() > 0) {
                res.moveToNext();
                id=res.getInt(res.getColumnIndex("id"));
            }
        }
        res.close();

        checklistItem=new CheckList(id,noteId,text,checked);

        contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        db.update(NOTE_TABLE_NAME,contentValues ,NOTE_COLUMN_ID+" = "+noteId, null );

        return checklistItem;
    }

    public void updateChecklistItem(int id, String text, String checked,int noteId,String title) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKLIST_COLUMN_CHECKNAME, text);
        contentValues.put(CHECKLIST_COLUMN_CHECKED, checked);
        db.update(CHECKLIST_TABLE_NAME,contentValues ,CHECKLIST_COLUMN_ID+" = "+id, null );
        contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        db.update(NOTE_TABLE_NAME,contentValues ,NOTE_COLUMN_ID+" = "+noteId, null );
    }


  /*  public Integer deletePlates(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTES_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }*/


}