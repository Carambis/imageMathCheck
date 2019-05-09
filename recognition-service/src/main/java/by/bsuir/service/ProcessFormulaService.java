package by.bsuir.service;

import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ProcessFormulaService {
    Resource processImage(BufferedImage bufferedImage) throws IOException;
    Resource createDocFile(String formula) throws IOException;
}
