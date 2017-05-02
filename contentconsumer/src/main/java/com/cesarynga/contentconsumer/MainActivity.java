package com.cesarynga.contentconsumer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private ArrayAdapter<String> adapter;
    private List<String> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initUi();
        renderTaskInView();
    }

    private void initUi() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, taskList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    private void renderTaskInView() {
        listView.clearChoices();
        taskList.clear();
        taskList.addAll(getTaskList());
        adapter.notifyDataSetChanged();
    }

    private List<String> getTaskList() {
        Cursor cursor = query();
        List<String> taskList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String task = cursorToTask(cursor);
                taskList.add(task);
            }
            cursor.close();
        }
        return taskList;
    }

    private String cursorToTask(Cursor cursor) {
        return String.valueOf(cursor.getLong(0)) +
                " - " +
                cursor.getString(1) +
                " - " +
                cursor.getString(2);
    }

    private Cursor query() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{
                "_ID", "title", "date_time"};
        Uri uri = Uri.parse("content://com.cesarynga.contentproviders/task");
        return cr.query(uri, projection, null, null, null);
    }
}
