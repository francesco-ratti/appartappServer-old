package com.polimi.mrf.appartapp.google;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.Gender;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleTokenVerifier {

    private static final String FIREBASE_PROJECT_ID="appartapp-5f260";
    private static final String APPLICATION_NAME = "appartapp";
    private static final String PUBLIC_KEY_URL="https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";
    private static final String ISSUER="https://securetoken.google.com/appartapp-5f260";

    public static GoogleUserInfo verifyToken(String idTokenString, String accessTokenString) throws GeneralSecurityException, IOException {

        //GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)

        GooglePublicKeysManager manager = new GooglePublicKeysManager.Builder(new NetHttpTransport(), new GsonFactory())
                .setPublicCertsEncodedUrl(PUBLIC_KEY_URL)
                .build();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(manager)
                .setAudience(Collections.singletonList(FIREBASE_PROJECT_ID))
                .setIssuer(ISSUER)
                .build();

                    //"https://people.googleapis.com/v1/people/me?personFields=birthdays,genders&access_token=${res.credential.accessToken}`

        System.out.println("access token "+accessTokenString);

        /*
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+accessTokenString);

        PeopleService peopleService=PeopleService.Builder();
        PeopleService.People.Get people=peopleService.people().get("people/me");
        String personFields=people.getPersonFields();
         */
        /*
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                    .url(url)
                    .build();
         */

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            //access PEOPLE API in order to get gender and birthday
            final Credential cred = new Credential.Builder(
                    BearerToken.authorizationHeaderAccessMethod(
                    )
            ).build();

            cred.setAccessToken(accessTokenString);

            PeopleService service = new PeopleService.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), cred)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Person personData=service.people().get("people/me").setPersonFields("genders,birthdays").execute();

            //List<EmailAddress> emailAddresses= personData.getEmailAddresses();
            List<Birthday> birthdays= personData.getBirthdays();
            List<Gender> genders= personData.getGenders();

            if (birthdays.isEmpty() || genders.isEmpty())
                return null;

            return new GoogleUserInfo(payload.getSubject(), payload.getEmail(), payload.getEmailVerified(), (String) payload.get("name"), (String) payload.get("picture"), (String) payload.get("locale"), (String) payload.get("family_name"), (String) payload.get("given_name"), birthdays.get(birthdays.size()-1), genders.get(0));
        } else {
            return null;
        }

        /*
        try (Response response = client.newCall(request).execute()) {
                String res=response.body().string();

                System.out.println(res);

                GoogleIdToken idToken = verifier.verify(idTokenString);
                if (idToken != null) {
                    Payload payload = idToken.getPayload();

                    return new GoogleUserInfo(payload.getSubject(), payload.getEmail(), payload.getEmailVerified(), (String) payload.get("name"), (String) payload.get("picture"), (String) payload.get("locale"), (String) payload.get("family_name"), (String) payload.get("given_name"));
                } else {
                    return null;
                }
            }*/

// (Receive idTokenString by HTTPS POST)
    }
}
