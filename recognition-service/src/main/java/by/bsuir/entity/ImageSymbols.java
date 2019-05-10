package by.bsuir.entity;

import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

public class ImageSymbols {
    private int[] image;
    private String symbol;

    public ImageSymbols(int[] image, String symbol) {
        this.image = image;
        this.symbol = symbol;
    }

    public ImageSymbols(){
    }

    public int[] getImage() {
        return image;
    }

    public void setImage(int[] image) {
        this.image = image;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageSymbols that = (ImageSymbols) o;
        return Arrays.equals(image, that.image) &&
                Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(symbol);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "ImageSymbols{" +
                "image=" + Arrays.toString(image) +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
