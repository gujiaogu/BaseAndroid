package com.yunshu.baseandroid.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Object Box实体类，不能存在有参数的构造函数
 *
 * Created by Tyrese on 2017/12/19.
 */

@Entity
public class Customer {
    @Id
    public long id;
    public String name;

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
}
