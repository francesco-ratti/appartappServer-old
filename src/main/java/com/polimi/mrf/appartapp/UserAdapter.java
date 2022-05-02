package com.polimi.mrf.appartapp;

import com.google.gson.*;

import java.util.List;

import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserImage;

import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", user.getId());
        obj.addProperty("email", user.getEmail());
        obj.addProperty("password", user.getPassword());
        obj.addProperty("name", user.getName());
        obj.addProperty("surname", user.getSurname());
        obj.addProperty("birthday", user.getBirthday().getTime());
        obj.addProperty("gender", user.getGender().toString());

        Gson gson=new Gson();
        TypeAdapter<List> listAdapter=gson.getAdapter(List.class);
        obj.add("images", listAdapter.toJsonTree(user.getImages()));

        return obj;
    }
}
