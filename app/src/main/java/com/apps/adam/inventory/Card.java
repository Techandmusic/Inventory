package com.apps.adam.inventory;


import android.widget.Button;

public class Card  {

    private int ID;
    private String Title;
    private String Author;
    private Double Price;
    private Integer Quantity;
    private String SupplierName;
    private String SupplierPhone;



    //Constructor method
    public Card(int id, String title, String author, Double price, int quantity, String supName, String supPhone) {
        ID = id;
        Title = title;
        Author = author;
        Price = price;
        Quantity = quantity;
        SupplierName = supName;
        SupplierPhone = supPhone;
    }

    //Getter methods to access private variables


    public int getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public Double getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public String getSupplierPhone() {
        return SupplierPhone;
    }












}
