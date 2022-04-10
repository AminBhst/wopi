package com.viratech.wopihost;

import com.viratech.wopihost.config.ConfigData;
import com.viratech.wopihost.dto.CheckFileInfo;
import com.viratech.wopihost.dto.WopiDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class WopiController {

    private final ConfigData configData;

    public WopiController(ConfigData configData) {
        this.configData = configData;
    }

    @GetMapping("/wopi/files/{fileId}")
    public CheckFileInfo wopi(@RequestParam(required = false) String letterUid,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String letterContent) throws Exception {

        if (StringUtils.isEmpty(letterContent)) {
            String fileName = letterUid + ".docx";
            File file = new File(fileName);
            if (file.createNewFile()) {
                CheckFileInfo cfi = new CheckFileInfo();
                cfi.setBaseFileName(fileName);
                cfi.setVersion("1");
                cfi.setOwnerId(username);
                cfi.setUserFriendlyName(username);
                cfi.setSize("4445");
                return cfi;
            }
        }
        throw new Exception("wrffg");
    }
}
