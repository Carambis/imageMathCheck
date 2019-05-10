package by.bsuir.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ProcessFormulaService {
    ByteArrayResource processImage(BufferedImage bufferedImage) throws IOException;
    ByteArrayResource createDocFile(String formula) throws IOException;
}
