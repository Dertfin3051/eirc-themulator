package ru.dfhub.eirc;

import org.json.JSONObject;

public class Main {

    private static ServerConnection serverConnection;
    private static JSONObject config;

    public static void main(String[] args) {
        Gui.init();
        Gui.showWelcomeMessage();

        try {
            Config.init();
            config = Config.getConfig();
        } catch (Exception e)
        {
            Gui.breakInput();
            Gui.showNewMessage("An error occurred while reading the config!", Gui.MessageType.SYSTEM_ERROR);
            return;
        }

        try {
            serverConnection = new ServerConnection(config.getString("server-address"), config.getInt("server-port"));
        } catch (Exception e)
        {
            Gui.breakInput();
            Gui.showNewMessage("Failed connect to the server!", Gui.MessageType.SYSTEM_ERROR);
            return;
        }

        DataParser.handleOutputSession(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DataParser.handleOutputSession(false)));
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    public static JSONObject getConfig() {
        return config;
    }
}