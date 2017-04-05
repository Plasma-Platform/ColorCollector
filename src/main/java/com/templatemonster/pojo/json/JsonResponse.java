package com.templatemonster.pojo.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResponse
{
    @JsonProperty("MD5SUM")
    private String md5;

    @JsonProperty("command")
    private String command;

    @JsonProperty("data")
    private JsonData data;

    public JsonResponse()
    {}

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public JsonData getData() {
        return data;
    }

    public void setData(JsonData data) {
        this.data = data;
    }
}