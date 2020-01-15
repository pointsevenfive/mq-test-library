package com.pointsevenfive.esb.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public class ResourceHelper {
    public static String getResource(String path) {
        String resource = null;
        URL url = Resources.getResource(path);
        try {
            resource = Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            System.err.println(String.format("Unable to find resource at: %s", path));
        }
        return resource;
    }
}
