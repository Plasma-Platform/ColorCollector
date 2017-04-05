package com.templatemonster.pojo.color;

import java.io.Serializable;

public class ColorMapping implements Serializable {

    private Long id;
    private BigPaletteColorItem bigPaletteColorItem;
    private SmallPaletteColorItem smallPaletteColorItem;
    private Double weight;

    public ColorMapping(Long id, Double weight, BigPaletteColorItem bigPaletteColorItem, SmallPaletteColorItem smallPaletteColorItem) {
        setId(id);
        setWeight(weight);
        setBigPaletteColorItem(bigPaletteColorItem);
        setSmallPaletteColorItem(smallPaletteColorItem);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigPaletteColorItem getBigPaletteColorItem() {
        return bigPaletteColorItem;
    }

    public void setBigPaletteColorItem(BigPaletteColorItem bigPaletteColorItem) {
        this.bigPaletteColorItem = bigPaletteColorItem;
    }

    public SmallPaletteColorItem getSmallPaletteColorItem() {
        return smallPaletteColorItem;
    }

    public void setSmallPaletteColorItem(SmallPaletteColorItem smallPaletteColorItem) {
        this.smallPaletteColorItem = smallPaletteColorItem;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
