package com.erikat.cochesmongodb.Utils;

import java.io.File;
import java.io.InputStream;

public class R {
    public static InputStream getProperties(String filename){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("config"+ File.separator+filename);
    }
}
