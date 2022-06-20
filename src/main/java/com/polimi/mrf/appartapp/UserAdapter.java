package com.polimi.mrf.appartapp;

import com.google.gson.*;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserImage;

import javax.persistence.Column;
import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", user.getId());
        obj.addProperty("email", user.getEmail());
        //obj.addProperty("password", user.getPassword());
        obj.addProperty("name", user.getName());
        obj.addProperty("surname", user.getSurname());
        obj.addProperty("birthday", user.getBirthday().getTime());
        obj.addProperty("gender", user.getGender().toString());

        //OPTIONAL ATTRIBUTES
        obj.addProperty("bio", user.getBio()!=null ? user.getBio() : "");
        obj.addProperty("reason", user.getReason()!=null ? user.getReason() : "");
        obj.addProperty("month", user.getMonth()!=null ? user.getMonth().toString() : "");
        obj.addProperty("job", user.getJob()!=null ? user.getJob() : "");
        obj.addProperty("income", user.getIncome()!=null ? user.getIncome() : "");
        obj.addProperty("smoker", user.getSmoker()!=null ? user.getSmoker().toString() : "");
        obj.addProperty("pets", user.getPets()!=null ? user.getPets() : "");

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create();
        obj.add("images", gson.toJsonTree(user.getImages()));

        return obj;
    }
}
