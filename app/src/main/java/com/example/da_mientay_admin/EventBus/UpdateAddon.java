package com.example.da_mientay_admin.EventBus;

import com.example.da_mientay_admin.Model.Addon;

import java.util.List;

public class UpdateAddon {
    private List<Addon> addons;

    public UpdateAddon() {
    }

    public List<Addon> getAddons() {
        return addons;
    }

    public void setAddons(List<Addon> addons) {
        this.addons = addons;
    }
}
