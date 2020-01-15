package com.pointsevenfive.esb.config;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class Environment {

    private static String FILE = Paths.get("environment.properties").toAbsolutePath().toString();
    private String host;
    private String channel;
    private String queueManager;
    private int port;

    public Environment(String target) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(FILE)) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: unable to locate file %s", FILE));
        } catch (IOException e) {
            System.err.println(String.format("Error: unable to open file %s", FILE));
        }
        this.host = properties.getProperty(String.format("%s.host", target));
        this.channel = properties.getProperty(String.format("%s.channel", target));
        this.queueManager = properties.getProperty(String.format("%s.qmanager", target));
        this.port = Integer.parseInt(properties.getProperty(String.format("%s.port", target)));
    }

    // Getters and Setters

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(String queueManager) {
        this.queueManager = queueManager;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
