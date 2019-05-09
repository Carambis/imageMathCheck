package by.bsuir.service.impl;

import by.bsuir.dao.SymbolWeightDao;
import by.bsuir.entity.SymbolWeight;
import by.bsuir.service.ProcessFormulaService;
import by.bsuir.service.utils.CustomNeuralNetwork;
import by.bsuir.service.utils.ImageRenderUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessFormulaServiceImpl implements ProcessFormulaService {
    private final Environment environment;
    private final SymbolWeightDao symbolWeightDao;

    @Autowired
    public ProcessFormulaServiceImpl(Environment environment, SymbolWeightDao symbolWeightDao) {
        this.environment = environment;
        this.symbolWeightDao = symbolWeightDao;
    }

    @Override
    public Resource processImage(BufferedImage bufferedImage) throws IOException{
        int width = Integer.valueOf(environment.getProperty("image.common.width"));
        int height = Integer.valueOf(environment.getProperty("image.common.height"));
        List<int[]> images = new ArrayList<>();
        int[][] arrays = ImageRenderUtils.getBW(bufferedImage);
        List<int[]> results = ImageRenderUtils.segment(arrays);
        for (int[] result : results) {
            int[][] newImage = new int[result[1] - result[0]][result[3] - result[2]];
            for (int x = result[0]; x < result[1]; x++) {
                for (int j = result[2]; j < result[3]; j++) {
                    newImage[x - result[0]][j - result[2]] = arrays[x][j];
                }
            }
            BufferedImage scaled = ImageRenderUtils.scaleImage(newImage, width, height);

            newImage = ImageRenderUtils.getBW(scaled);

            images.add(ImageRenderUtils.toSimpleArray(newImage));
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<SymbolWeight> weightList = symbolWeightDao.findAll();
        for (int[] imageByte : images) {
            for (SymbolWeight weight : weightList) {
                if (CustomNeuralNetwork.proceed(imageByte, weight.getWeight())) {
                    String symbol = weight.getSymbol();
                    if (stringBuilder.length() != 0) {
                        String lastSymbol = String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1));
                        if (symbol.equals("-") && lastSymbol.equals("-")) {
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            stringBuilder.append("=");
                        } else {
                            stringBuilder.append(symbol);
                        }
                    } else {
                        stringBuilder.append(symbol);

                    }
                    break;
                }
            }
        }

        return createDocFile(stringBuilder.toString());
    }

    @Override
    public Resource createDocFile(String formula) throws IOException {
        File file = File.createTempFile("temp", ".docx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(formula);
        document.write(fileOutputStream);
        Resource resource = new FileSystemResource(file);
        fileOutputStream.close();
        return resource;
    }
}
