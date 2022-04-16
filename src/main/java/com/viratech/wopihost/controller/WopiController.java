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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public CheckFileInfoDTO wopi(@PathVariable("fileName") String fileName, HttpServletRequest request) throws Exception {
        log.info("fileName : " + fileName);
        log.info("header : " + request.getHeader("headhead"));
        try {
            createEmptyWordDocument(fileName);
            CheckFileInfoDTO cfi = new CheckFileInfoDTO();
            cfi.setBaseFileName(fileName);
            cfi.setVersion("1");
            cfi.setOwnerId("Worddd");
            cfi.setUserFriendlyName("Worddd");
            cfi.setSize(Files.size(Paths.get(fileName)));
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
    public void updateFileContent(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        response.reset();
        response.addHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        URL url = new URL(fileName);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        response.addHeader("Content-Length", String.valueOf(uc.getContentLengthLong()));
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setStatus(200);
        toClient.write(buffer);
        toClient.flush();
    }

    @GetMapping("/files/{fileName}/contents")
    public byte[] getFileContent(@PathVariable("fileName") String fileName) {
        try {
            return IOUtils.toByteArray(new FileInputStream(fileName));
        } catch (Throwable t) {
            log.error("Error occurred while retrieving contents!", t);
            return null;
        }
    }
}
