package com.example.da_mientay_admin.EventBus;

import com.example.da_mientay_admin.Model.Size;

public class SelectSize {
    private Size size;

    public SelectSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
