package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Person extends Serializable {

    public long getId();

    public String getName();

    public void setName(String name);
}
