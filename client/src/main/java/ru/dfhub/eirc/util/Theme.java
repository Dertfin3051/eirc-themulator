package ru.dfhub.eirc.util;

import org.json.JSONObject;

import java.awt.*;
import java.util.List;

public class Theme {

    private static final List<String> paramList = List.of(
            "background",
            "message-read-box",
            "message-input-box",
            "input-text-color",
            "other-user-message",
            "self-user-message",
            "system-good-message",
            "system-info-message",
            "system-error-message",
            "user-session-message",
            "component-border-color",
            "welcome-message-font-size",
            "message-font-size"
    );

    public static final Theme DEFAULT_THEME = new Theme(
            new JSONObject(new ResourcesReader("default_theme.json").readString())
    );

    private final JSONObject themeObject;

    public Theme(JSONObject themeObject) {
        this.themeObject = themeObject;
    }

    public Color getColor(String colorParam) {
        String color = themeObject.optString(colorParam, "");
        return color.isEmpty() ? DEFAULT_THEME.getColor(colorParam) : Color.decode(color);
    }

    public int getSize(String sizeParam) {
        return themeObject.optInt(
                sizeParam,
                15
        );
    }
}
