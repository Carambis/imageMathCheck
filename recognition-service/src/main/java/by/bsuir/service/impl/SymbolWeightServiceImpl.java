package by.bsuir.service.impl;

import by.bsuir.dao.SymbolWeightDao;
import by.bsuir.entity.ImageSymbols;
import by.bsuir.entity.SymbolWeight;
import by.bsuir.service.SymbolWeightService;
import by.bsuir.service.utils.CustomNeuralNetwork;
import by.bsuir.service.utils.EnvironmentConst;
import by.bsuir.service.utils.ImageRenderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SymbolWeightServiceImpl implements SymbolWeightService {

    private final SymbolWeightDao symbolWeightDao;
    private final Environment environment;

    @Autowired
    public SymbolWeightServiceImpl(SymbolWeightDao symbolWeightDao,
                                   Environment environment) {
        this.symbolWeightDao = symbolWeightDao;
        this.environment = environment;
    }

    @Override
    public void train() throws IOException {
        int width = Integer.valueOf(environment.getProperty(EnvironmentConst.COMMON_WIDTH));
        int height = Integer.valueOf(environment.getProperty(EnvironmentConst.COMMON_HEIGHT));
        String path = environment.getProperty("image.common.path");
        System.out.println("Start learning " + width);
        List<SymbolWeight> weightList = new ArrayList<>();
        List<ImageSymbols> listImage = new ArrayList<>();
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "-.bmp", width, height), "-"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "+.bmp", width, height), "+"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "2.bmp", width, height), "2"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "a.bmp", width, height), "a"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "b.bmp", width, height), "b"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "c.bmp", width, height), "c"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "x.bmp", width, height), "x"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "y.bmp", width, height), "y"));
        listImage.add(new ImageSymbols(ImageRenderUtils.readImage(path + "z.bmp", width, height), "z"));
//

        String[] symbols = new String[]{"-", "+", "2", "a", "b", "c", "x", "y", "z"};
        for (String symbol : symbols) {
            int[] weights = new int[width * height];
            for (int i = 0; i < 2000; i++) {
                for (ImageSymbols imageSymbols : listImage) {
                    if (!imageSymbols.getSymbol().equals(symbol)) {
                        if (CustomNeuralNetwork.proceed(imageSymbols.getImage(), weights)) {
                            System.out.println("Symbol - " + symbol + " iteration - " + i + "decrease");
                            CustomNeuralNetwork.decrease(imageSymbols.getImage(), weights);
                        }
                    } else {
                        if (!CustomNeuralNetwork.proceed(imageSymbols.getImage(), weights)) {
                            System.out.println("Symol - " + symbol + " iteration - " + i + "increase");
                            CustomNeuralNetwork.increase(imageSymbols.getImage(), weights);
                        }
                    }
                }

            }
            weightList.add(new SymbolWeight(symbol, weights));
        }
        System.out.println("Finish");
        symbolWeightDao.save(weightList);
    }

    @Override
    public boolean isWeightEmpty() {
        Iterable<SymbolWeight> all = symbolWeightDao.findAll();
        boolean hasNext = all.iterator().hasNext();
        return !hasNext;
    }
}
