package com.converter.server.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientIDService {

    Map<String, String> sessionIDs = new HashMap<>();

    public ClientIDService(Map<String, String> sessionIDs) {
        this.sessionIDs = sessionIDs;
    }

    public Map<String, String> getSessionIDs() {
        return sessionIDs;
    }

    public void setSessionIDs(Map<String, String> sessionIDs) {
        this.sessionIDs = sessionIDs;
    }
}
