package com.example.da_mientay_admin.EventBus;

import com.example.da_mientay_admin.Model.Size;

import java.util.List;

public class UpdateSize {
    private List<Size> sizeList;

    public UpdateSize(List<Size> sizeList) {
        this.sizeList = sizeList;
    }

    public UpdateSize() {
    }

    public List<Size> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<Size> sizeList) {
        this.sizeList = sizeList;
    }
}
