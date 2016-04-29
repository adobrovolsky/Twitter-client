package com.twitty.store;

public interface Identified<PK> {

    public void setId(PK id);

    public PK getId();
}
