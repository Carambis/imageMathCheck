package by.bsuir.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

@Document(collection = "symbol_weight")
public class SymbolWeight {
    private String symbol;
    private int[] weight;

    public SymbolWeight(String symbol, int[] weight) {
        this.symbol = symbol;
        this.weight = weight;
    }

    public SymbolWeight() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int[] getWeight() {
        return weight;
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolWeight that = (SymbolWeight) o;
        return Objects.equals(symbol, that.symbol) &&
                Arrays.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(symbol);
        result = 31 * result + Arrays.hashCode(weight);
        return result;
    }

    @Override
    public String toString() {
        return "SymbolWeight{" +
                "symbol='" + symbol + '\'' +
                ", weight=" + Arrays.toString(weight) +
                '}';
    }
}
