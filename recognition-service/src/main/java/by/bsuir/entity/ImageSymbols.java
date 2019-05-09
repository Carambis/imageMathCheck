package by.bsuir.entity;

import lombok.Data;

public @Data class ImageSymbols {
    private int[] image;
    private String symbol;

    public ImageSymbols(int[] image, String symbol) {
        this.image = image;
        this.symbol = symbol;
    }

    public ImageSymbols(){

    }
}
