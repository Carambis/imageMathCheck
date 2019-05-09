package by.bsuir.service;

import by.bsuir.entity.SymbolWeight;

import java.io.IOException;
import java.util.List;

public interface SymbolWeightService {
    void train() throws IOException;
    boolean isWeightEmpty();
}
