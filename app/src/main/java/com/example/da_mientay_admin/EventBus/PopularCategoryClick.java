package com.example.da_mientay_admin.EventBus;

import com.example.da_mientay_admin.Model.PopularCategory;

public class PopularCategoryClick {
    private PopularCategory popularCategory;

    public PopularCategoryClick(PopularCategory popularCategory) {
        this.popularCategory = popularCategory;
    }

    public PopularCategory getPopularCategory() {
        return popularCategory;
    }

    public void setPopularCategory(PopularCategory popularCategory) {
        this.popularCategory = popularCategory;
    }
}

