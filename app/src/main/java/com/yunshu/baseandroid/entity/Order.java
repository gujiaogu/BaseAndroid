package com.yunshu.baseandroid.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Object Box实体类，不能存在有参数的构造函数
 *
 * Created by Tyrese on 2017/12/19.
 */

@Entity
public class Order {
    @Id
    public long id;
    public ToOne<Customer> customer;

    private String orderName;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
