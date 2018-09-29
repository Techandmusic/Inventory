package com.apps.adam.inventory;



public class Card  {

    private String Title;
    private Double Price;
    private Integer Quantity;

    //Constructor method
    public Card(String title, Double price, int quantity) {
        Title = title;
        Price = price;
        Quantity = quantity;
    }

    //Getter methods to access private variables
    public String getTitle() {
        return Title;
    }

    public Double getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }








}
