package com.viratech.wopihost.controller;

import com.viratech.wopihost.config.ConfigData;
import com.viratech.wopihost.converter.DocxToHtmlConverter;
import com.viratech.wopihost.dto.CheckFileInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@RestController
@RequestMapping("/wopi")
@Slf4j
public class WopiController {

    private final ConfigData configData;

    @Autowired
    public WopiController(ConfigData configData) {
        this.configData = configData;
    }

    @GetMapping("/files/{fileName}")
    public CheckFileInfoDTO wopi(@PathVariable("fileName") String fileName) throws Exception {
        log.info("file : " + Paths.get(configData.getWordFilesPath()).resolve(fileName));
        try {
//            createEmptyWordDocument(fileName);
            CheckFileInfoDTO cfi = new CheckFileInfoDTO();
            cfi.setBaseFileName(fileName);
            cfi.setVersion("1");
            cfi.setOwnerId("Worddd");
            cfi.setUserFriendlyName("Worddd");
            cfi.setSize(Files.size(Paths.get(configData.getWordFilesPath()).resolve(fileName)));
            return cfi;
        } catch (Throwable t) {
            log.error("Error write", t);
        }
        log.error("File not created");
        return null;
    }

    @GetMapping("/files/{fileName}/html")
    public String convertDocxToHtml(@PathVariable("fileName") String fileName) {
        try {
            return DocxToHtmlConverter.convert(fileName);
        } catch (Throwable t) {
            log.error("Error occurred while converting content to html", t);
            return null;
        }
    }

    private void createEmptyWordDocument(String fileName) throws IOException {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream fos = new FileOutputStream(fileName);
        log.info("Writing {}", fileName);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setText("");
        document.write(fos);
        fos.close();
        document.close();
    }

    @PostMapping("/files/{fileName}/contents")
    public void updateFileContent(@PathVariable("fileName") String fileName, @RequestBody byte[] bytes) throws IOException {
        File file = Paths.get(configData.getWordFilesPath()).resolve(fileName).toFile();
        try (FileOutputStream fop = new FileOutputStream(file)) {
            fop.write(bytes);
            fop.flush();
        } catch (IOException e) {
            log.error("postFile failed, errMsg: {}", e.getMessage());

        }
    }

    @GetMapping("/files/{fileName}/contents")
    public ResponseEntity<InputStreamResource> getFileContent(@PathVariable("fileName") String fileName) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(Paths.get(configData.getWordFilesPath()).resolve(fileName).toFile());
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .contentLength(Files.size(Paths.get(configData.getWordFilesPath()).resolve(fileName)))
                .lastModified(new Date().getTime())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }
}
