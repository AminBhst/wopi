package com.viratech.wopihost;

import com.viratech.wopihost.config.ConfigData;
import com.viratech.wopihost.dto.CheckFileInfo;
import com.viratech.wopihost.dto.WopiDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
public class WopiController {

    private final ConfigData configData;

    public WopiController(ConfigData configData) {
        this.configData = configData;
    }

    @GetMapping("/wopi/files/{fileName}")
    public CheckFileInfo wopi(@PathVariable("fileName") String fileName) throws Exception {
        log.info("fileName : " + fileName);
        XWPFDocument document = new XWPFDocument();
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.close();
            document.write(fos);
            log.info("Writing {}", fileName);
            document.close();
            CheckFileInfo cfi = new CheckFileInfo();
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
        throw new Exception("wrffg");
    }

    @PostMapping("/wopi/files/{fileName}/contents")
    public void updateFileContent(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
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

    @GetMapping("/wopi/files/{fileName}/contents")
    public byte[] getFileContent(@PathVariable("fileName") String fileName) throws FileNotFoundException, IOException {
        return IOUtils.toByteArray(new FileInputStream(fileName));
    }
}
