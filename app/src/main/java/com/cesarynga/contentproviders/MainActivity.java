package com.cesarynga.contentproviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.cesarynga.contentproviders.db.TaskContract;
import com.cesarynga.contentproviders.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.edt_task_title)
    EditText edtTaskTitle;
    @BindView(R.id.edt_task_date)
    EditText edtTaskDate;
    @BindView(R.id.edt_task_time)
    EditText edtTaskTime;
    @BindView(R.id.list_view)
    ListView listView;

    private ArrayAdapter<Task> adapter;
    private List<Task> taskList = new ArrayList<>();

    private long selectedId = 0;

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
        selectedId = 0;
        listView.clearChoices();
        taskList.clear();
        taskList.addAll(getTaskList());
        adapter.notifyDataSetChanged();
    }

    private List<Task> getTaskList() {
        Cursor cursor = query();
        List<Task> taskList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Task task = cursorToTask(cursor);
                taskList.add(task);
            }
            cursor.close();
        }
        return taskList;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.id = cursor.getLong(cursor.getColumnIndex(TaskContract.Task._ID));
        task.title = cursor.getString(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_TITLE));
        task.dateTime = cursor.getString(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_DATE_TIME));
        return task;
    }

    @OnClick(R.id.btn_insert_task)
    public void onInsertButtonClick() {
        ContentValues values = getContentValues();

        insert(values);

        renderTaskInView();
    }

    @OnClick(R.id.btn_update_task)
    public void onUpdateButtonClick() {
        ContentValues values = getContentValues();

        update(selectedId, values);

        renderTaskInView();
    }

    @OnClick(R.id.btn_delete_task)
    public void onDeleteButtonClick() {
        delete(selectedId);

        renderTaskInView();
    }

    @OnClick(R.id.edt_task_date)
    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edtTaskDate.setText(String.format("%4d-%02d-%02d", year, month + 1, dayOfMonth));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.edt_task_time)
    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edtTaskTime.setText(String.format("%02d:%02d:00.000", hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    @OnItemClick(R.id.list_view)
    public void onTaskClick(int position) {
        Task task = taskList.get(position);
        edtTaskTitle.setText(task.title);
        edtTaskDate.setText(task.dateTime.substring(0, 10));
        edtTaskTime.setText(task.dateTime.substring(10));
        selectedId = task.id;
    }

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TaskContract.Task.COLUMN_NAME_TITLE, edtTaskTitle.getText().toString());
        values.put(TaskContract.Task.COLUMN_NAME_DATE_TIME, edtTaskDate.getText().toString()
                + " " + edtTaskTime.getText().toString());
        return values;
    }

    private Cursor query() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{
                TaskContract.Task._ID,
                TaskContract.Task.COLUMN_NAME_TITLE,
                TaskContract.Task.COLUMN_NAME_DATE_TIME};
        return cr.query(TaskContract.Task.CONTENT_URI, projection, null, null, null);
    }

    private void insert(ContentValues values) {
        ContentResolver cr = getContentResolver();
        Uri uri = cr.insert(TaskContract.Task.CONTENT_URI, values);
        if (uri != null) {
            Log.i(TAG, "Inserted row id: " + uri.getLastPathSegment());
        } else {
            Log.i(TAG, "Uri null");
        }
    }

    private void update(long id, ContentValues values) {
        if (id > 0) {
            ContentResolver cr = getContentResolver();
            Uri uri = TaskContract.Task.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            int updatedRows = cr.update(uri, values, null, null);
            Log.i(TAG, "Updated rows: " + updatedRows);
        }
    }

    private void delete(long id) {
        if (id > 0) {
            ContentResolver cr = getContentResolver();
            Uri uri = TaskContract.Task.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            int updatedRows = cr.delete(uri, null, null);
            Log.i(TAG, "Deleted rows: " + updatedRows);
        }
    }
}
