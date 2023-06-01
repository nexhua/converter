package com.converter.server.routers;

import com.converter.server.services.ClientIDService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;
import java.util.SimpleTimeZone;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private ClientIDService clientIDService;

    @Test
    void checkIfAuthorizeSpotifyAddsSessionToClientIDService_failIfNot() {

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        ResponseEntity<?> response = authController.authorizeSpotify(mockHttpServletRequest);

        Assertions.assertTrue(clientIDService.findSessionOptional(mockHttpServletRequest.getSession().getId()).isPresent());
    }

    @Test
    void checkIfAuthorizeSpotifyRedirectSucessfully_andStateMatchesAndTokenIsCreated() {
        MockHttpServletRequest authSpotifyMockRequest = new MockHttpServletRequest();

        ResponseEntity<?> response = authController.authorizeSpotify(authSpotifyMockRequest);

        Optional<String> optionalState = clientIDService.findSessionOptional(authSpotifyMockRequest.getSession().getId());

        if(optionalState.isPresent()) {
            String state = optionalState.get();
        }

        Assertions.fail();
    }
}