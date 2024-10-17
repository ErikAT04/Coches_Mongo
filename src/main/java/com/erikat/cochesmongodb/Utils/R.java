package com.erikat.cochesmongodb.Utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class R {
    public static InputStream getProperties(String filename){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("config"+ File.separator+filename);
    }
    public static URL getResource(String filename){
        return Thread.currentThread().getContextClassLoader().getResource("ui/"+filename);
    }
}
