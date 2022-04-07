package com.example.da_mientay_admin.EventBus;

public class SizeAddOnEvent {
private  boolean Addon;
private int pos;


    public SizeAddOnEvent(boolean addon, int pos) {
        Addon = addon;
        this.pos = pos;
    }

    public boolean isAddon() {
        return Addon;
    }

    public void setAddon(boolean addon) {
        Addon = addon;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
