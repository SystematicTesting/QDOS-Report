package com.systematictesting.comparator;

import java.util.Comparator;

import com.google.gson.JsonObject;

public class DealerComparator implements Comparator<JsonObject> {
    @Override
    public int compare(JsonObject o1, JsonObject o2) {
        return ((Double)o1.getAsJsonObject("distance").get("km").getAsDouble()).compareTo((Double)o2.getAsJsonObject("distance").get("km").getAsDouble());
    }
}