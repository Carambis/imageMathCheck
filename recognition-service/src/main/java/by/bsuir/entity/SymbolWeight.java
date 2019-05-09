package by.bsuir.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "symbol_weight")
public @Data
class SymbolWeight {
    private String symbol;
    private int[] weight;

    public SymbolWeight(String symbol, int[] weight) {
        this.symbol = symbol;
        this.weight = weight;
    }

    public SymbolWeight() {
    }
}
