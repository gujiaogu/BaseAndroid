package com.yunshu.baseandroid.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Tyrese on 2017/12/19.
 */

@Entity
public class MySong {
    @Id(assignable = true)
    private long id;
    private String name;
    private String author;
    private String date;

    public MySong(long id, String name, String author, String date) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
