package com.polimi.mrf.appart;

import com.google.gson.*;
import com.polimi.mrf.appart.entities.*;

import java.lang.reflect.Type;
import java.util.List;

public class MatchAdapter implements JsonSerializer<Match> {
    @Override
    public JsonElement serialize(Match match, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        GsonBuilder gsonb=new GsonBuilder();
        UserAdapter userAdapter=new UserAdapter();
        Gson gs=gsonb.excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(User.class, userAdapter)
                .registerTypeAdapter(CredentialsUser.class, userAdapter)
                .registerTypeAdapter(GoogleUser.class, userAdapter)
                .create();

        obj.add("apartment", gs.toJsonTree(match.getApartment()));
        obj.addProperty("matchDate", match.getMatchDate().getTime());

        return obj;
    }
}
