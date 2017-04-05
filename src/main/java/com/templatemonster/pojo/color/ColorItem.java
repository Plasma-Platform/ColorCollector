package com.templatemonster.pojo.color;

public class ColorItem
{
    protected Long id;
    protected Integer color;

    public ColorItem(Long id, Integer color) {
        setId(id);
        setColor(color);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
