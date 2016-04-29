package com.twitty.store.entity;

import com.twitty.store.Identified;

import java.io.Serializable;
import java.util.Date;

public class Draft implements Identified<Integer>, Serializable {

    private Integer id;
    private String text;
    private Date creationDate;

    public Draft(Integer id, String text, Date creationDate) {
        this.id = id;
        this.text = text;
        this.creationDate = creationDate == null ? new Date() : creationDate;
    }

    @Override public void setId(Integer id) {
        this.id = id;
    }

    @Override public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Draft{");
        sb.append("id=").append(id);
        sb.append(", text='").append(text).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append('}');
        return sb.toString();
    }
}
