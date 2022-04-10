package com.viratech.wopihost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckFileInfo {

    @JsonProperty("BaseFileName")
    private String baseFileName;

    @JsonProperty("OwnerId")
    private String ownerId;

    @JsonProperty("Size")
    private String size;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("UserFriendlyName")
    private String UserFriendlyName;

    @JsonProperty("UserCanWrite")
    private boolean userCanWrite;

    @JsonProperty("ReadOnly")
    private boolean readOnly;

    @JsonProperty("SupportsLocks")
    private boolean supportsLocks;

    @JsonProperty("SupportsUpdate")
    private boolean supportsUpdate;

    @JsonProperty("UserCanNotWriteRelative")
    private boolean userCanNotWriteRelative;

    public CheckFileInfo() {
        this.userCanWrite = true;
        this.readOnly = false;
        this.supportsLocks = false;
        this.supportsUpdate = true;
    }
}
