package by.bsuir.dao;

import by.bsuir.entity.SymbolWeight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SymbolWeightDao extends CrudRepository<SymbolWeight, String> {
    List<SymbolWeight> findAll();
}
