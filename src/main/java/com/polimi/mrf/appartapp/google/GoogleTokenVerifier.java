package com.polimi.mrf.appartapp.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleTokenVerifier {

    private static final String FIREBASE_PROJECT_ID="appartapp-5f260";
    private static final String PUBLIC_KEY_URL="https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";
    private static final String ISSUER="https://securetoken.google.com/appartapp-5f260";

    public static GoogleUserInfo verifyToken(String idTokenString) throws GeneralSecurityException, IOException {

        //GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)

        GooglePublicKeysManager manager = new GooglePublicKeysManager.Builder(new NetHttpTransport(), new GsonFactory())
                .setPublicCertsEncodedUrl(PUBLIC_KEY_URL)
                .build();


        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(manager)
                .setAudience(Collections.singletonList(FIREBASE_PROJECT_ID))
                .setIssuer(ISSUER)
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            return new GoogleUserInfo(payload.getSubject(), payload.getEmail(), payload.getEmailVerified(), (String) payload.get("name"), (String) payload.get("picture"), (String) payload.get("locale"), (String) payload.get("family_name"), (String) payload.get("given_name"));
        } else {
            return null;
        }
    }
}
