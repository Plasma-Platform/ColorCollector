package com.templatemonster.pojo.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonData
{
    @JsonProperty("href")
    private String href;

    public JsonData()
    {}

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
