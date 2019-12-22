package me.rysavys.geneac.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GedGsonBuilder {
    public static Gson create() {
        return new GsonBuilder()
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getAnnotation(SkipSerialisation.class) != null;
                }

                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .setPrettyPrinting()
            .create();
    }
}
