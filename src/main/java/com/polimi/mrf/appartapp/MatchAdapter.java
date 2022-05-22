package com.polimi.mrf.appartapp;

import com.google.gson.*;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.Match;
import com.polimi.mrf.appartapp.entities.User;

import java.lang.reflect.Type;
import java.util.List;

public class MatchAdapter implements JsonSerializer<Match> {
    @Override
    public JsonElement serialize(Match match, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        GsonBuilder gsonb=new GsonBuilder();

        obj.add("apartment", gsonb.excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(User.class, new UserAdapter()).create().toJsonTree(match.getApartment()));
        obj.addProperty("matchDate", match.getMatchDate().getTime());

        return obj;
    }
}
