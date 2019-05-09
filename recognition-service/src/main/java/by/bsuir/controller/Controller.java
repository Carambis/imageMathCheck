package by.bsuir.controller;

import by.bsuir.service.ProcessFormulaService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class Controller {

    private final ProcessFormulaService processFormulaService;

    public Controller(ProcessFormulaService processFormulaService) {
        this.processFormulaService = processFormulaService;
    }

    @PostMapping(value = "/uploadFile")
    public Resource uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        Resource resource =  processFormulaService.processImage(bImage2);
        return resource;
    }
}
