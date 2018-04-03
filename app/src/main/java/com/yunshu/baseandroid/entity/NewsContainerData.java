package com.yunshu.baseandroid.entity;

import java.util.List;

public class NewsContainerData {


    private int id;//主键
    private String title;//"标题",新闻标题

    private int type;//新闻类型
    private List<String> imageurls;//"10.jpg",图片地址
    private int delFlag;//标志位，0为删除，1为未删除
    private int createUesrId;//"张三",创建人
    private String createDate;//"2016-03-09",创建时间
    private String remark;//"新闻",备注
    private int commentCount; // 评论条数
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getCreateUserId() {
        return createUesrId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUesrId = createUserId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getImageUrl() {
        return imageurls;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageurls = imageUrl;
    }


}
