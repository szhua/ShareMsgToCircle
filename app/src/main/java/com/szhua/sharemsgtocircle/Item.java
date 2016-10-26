package com.szhua.sharemsgtocircle;

/**
 * Created by szhua on 2016/10/25.
 */
public class Item {
    private int imgRes ;
    private boolean selected ;


    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Item{" +
                "imgRes=" + imgRes +
                ", selected=" + selected +
                '}';
    }
}
