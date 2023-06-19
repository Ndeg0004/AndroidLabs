package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoDatabase";
    private static final String TABLE_NAME = "TodoItems";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TODO = "todo";
    private static final String COLUMN_URGENCY = "urgency";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TODO + " TEXT, " +
                COLUMN_URGENCY + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops last table if created
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create table again
        onCreate(db);
    }

    public void addTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, item.getTodoText());
        values.put(COLUMN_URGENCY, item.isUrgent() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int todoTextIndex = cursor.getColumnIndex(COLUMN_TODO);
            int urgencyIndex = cursor.getColumnIndex(COLUMN_URGENCY);

            do {
                String todoText = cursor.getString(todoTextIndex);
                int urgencyValue = cursor.getInt(urgencyIndex);
                boolean isUrgent = (urgencyValue == 1);

                TodoItem todoItem = new TodoItem();
                todoItem.setTodoText(todoText);
                todoItem.setUrgent(isUrgent);

                todoList.add(todoItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return todoList;
    }

    public void deleteTodoItem(String todoText) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TODO + "=?", new String[]{todoText});
        db.close();
    }

    public void updateTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, item.getTodoText());
        values.put(COLUMN_URGENCY, item.isUrgent() ? 1 : 0);
        db.update(TABLE_NAME, values, COLUMN_TODO + "=?", new String[]{item.getTodoText()});
        db.close();
    }
}
