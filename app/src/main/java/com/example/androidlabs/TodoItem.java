package com.example.androidlabs;

public class TodoItem {
    private String todoText;
    private boolean urgent;

    public void setTodoText(String listItem) {
        todoText = listItem;
    }

    public String getTodoText() {
        return todoText;
    }

    public void setUrgent(boolean checked) {
        urgent = checked;
    }

    public boolean isUrgent() {
        return urgent;
    }
}
