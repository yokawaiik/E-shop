package com.YoKawaiiK.e_shop.Model;

public class FavouritesClass {

    private String productImage;
    private String productTitle;
    private String productPrice;
    private String productExpiryDate;
    private boolean isFavorite;


//    public FavouritesClass() {
//    }

    public FavouritesClass(
            String productImage,
            String productTitle,
            String productPrice,
            String productExpiryDate,
            boolean isFavorite

    ) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productPrice = productExpiryDate;
        this.isFavorite = isFavorite;
    }

//    private String productimage;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }


    public String getProductExpiryDate() {
        return productExpiryDate;
    }

    public void setProductExpiryDate(String productExpiryDate) {
        this.productExpiryDate = productExpiryDate;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

//    private String producttitle;
//    private String productprice;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean IsFavorite) {
        this.isFavorite = IsFavorite;
    }

//    private boolean checked;
}