package com.converter.server.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientIDService {

    private Map<String, String> sessionIDs = new HashMap<>();

    public ClientIDService(Map<String, String> sessionIDs) {
        this.sessionIDs = sessionIDs;
    }

    public void putSession(String sessionID, String state) {
        this.sessionIDs.put(sessionID, state);
    }

    public String findSession(String sessionID) {
        return this.sessionIDs.get(sessionID);
    }

    public Optional<String> findSessionOptional(String sessionID) {
        String session = this.sessionIDs.get(sessionID);

        if (session != null) {
            return Optional.of(session);
        } else {
            return Optional.empty();
        }
    }

    public void removeSession(String sessionID) {
        this.sessionIDs.remove(sessionID);
    }
}
