package com.viratech.wopihost;

import com.viratech.wopihost.config.ConfigData;
import com.viratech.wopihost.dto.CheckFileInfo;
import com.viratech.wopihost.dto.WopiDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            document.write(new FileOutputStream(fileName));
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
}
