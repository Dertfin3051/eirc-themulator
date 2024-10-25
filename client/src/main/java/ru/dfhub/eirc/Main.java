package ru.dfhub.eirc;

import org.json.JSONObject;
import ru.dfhub.eirc.util.Encryption;
import ru.dfhub.eirc.util.Theme;

public class Main {

    private static ServerConnection serverConnection;
    private static JSONObject config;

    public static void main(String[] args) throws Exception {
        Gui.init();
        Gui.showWelcomeMessage();
        Config.init();
        config = Config.getConfig();
        Gui.applyTheme(new Theme(
                config.optJSONObject("theme", new JSONObject("{}")) // Theme or empty
        ));
        Gui.showNewMessage("Good info message", Gui.MessageType.SYSTEM_GOOD);
        Gui.showNewMessage("Info/Warning text", Gui.MessageType.SYSTEM_INFO);
        Gui.showNewMessage("Error text", Gui.MessageType.SYSTEM_ERROR);
        Gui.showNewMessage("Self user message", Gui.MessageType.SELF_USER_MESSAGE);
        Gui.showNewMessage("Other user message", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("MegaUsername joined!", Gui.MessageType.USER_SESSION);
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    public static JSONObject getConfig() {
        return config;
    }

    public static void handleServerShutdown() {
        Gui.showNewMessage("The server has shut down!", Gui.MessageType.SYSTEM_ERROR);
        Gui.breakInput();
    }
}