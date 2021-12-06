package com.sprobotics.network.util;

import java.io.File;

public class PART {
    String key;
    File file;

    public PART(String key, File file) {
        this.key = key;
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



}
