package com.yunshu.baseandroid.entity;

import java.util.List;

public class NewsJson {

    private String msg;
    private int status;
    private List<NewsContainerData> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<NewsContainerData> getData() {
        return data;
    }

    public void setData(List<NewsContainerData> data) {
        this.data = data;
    }


}
