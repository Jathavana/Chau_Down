package com.example.yadu.chaudown;

/**
 * Created by Tom on 11/02/2015.
 */
public class Ingredient {
    private String _name;
    private String _category;
    private int _amount;
    private String _unit;

    public Ingredient() {
    }

    public Ingredient(String name, String category, int amount, String unit) {
        this._name = name;
        this._category = category;
        this._amount = amount;
        this._unit = unit;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String newName) {
        this._name = newName;
    }

    public String getCategory() {
        return this._category;
    }

    public void setCategory(String newCategory) {
        this._category = newCategory;
    }

    public int getAmount() {
        return this._amount;
    }

    public void setAmount(int newAmount) {
        this._amount = newAmount;
    }

    public String getUnit() {
        return this._unit;
    }

    public void setUnit(String newUnit) {
        this._unit = newUnit;
    }

    @Override
    public String toString() {
        return this._name;
    }
}
