package by.bsuir;

import by.bsuir.service.SymbolWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class RecognitionApp {

    private final SymbolWeightService symbolWeightService;

    @Autowired
    public RecognitionApp(SymbolWeightService symbolWeightService) {
        this.symbolWeightService = symbolWeightService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RecognitionApp.class, args);
    }

    @PostConstruct
    private void init() throws IOException {
        if (symbolWeightService.isWeightEmpty()) {
            symbolWeightService.train();
        }
    }
}
