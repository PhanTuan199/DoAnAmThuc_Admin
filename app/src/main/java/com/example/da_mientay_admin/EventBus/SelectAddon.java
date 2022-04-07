package com.example.da_mientay_admin.EventBus;

import com.example.da_mientay_admin.Model.Addon;

public class SelectAddon {
    private Addon addon;

    public SelectAddon(Addon addon) {
        this.addon = addon;
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }
}
