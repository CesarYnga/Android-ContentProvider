package com.cesarynga.contentproviders.model;

public class Task {
    public long id;
    public String title;
    public String dateTime;

    @Override
    public String toString() {
        return "id: " + id + " - title: " + title + " - " + "date time: " + dateTime;
    }
}
