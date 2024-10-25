import ru.dfhub.eirc.Gui;

@Deprecated
public class GuiStyleTest {
    public static void main(String[] args) {
        // Non actual
        Gui.init();
        Gui.showWelcomeMessage();
        Gui.showNewMessage("Good info message", Gui.MessageType.SYSTEM_GOOD);
        Gui.showNewMessage("Info/Warning text", Gui.MessageType.SYSTEM_INFO);
        Gui.showNewMessage("Error text", Gui.MessageType.SYSTEM_ERROR);
        Gui.showNewMessage("Self user message", Gui.MessageType.SELF_USER_MESSAGE);
        Gui.showNewMessage("Other user message", Gui.MessageType.USER_MESSAGE);
        Gui.showNewMessage("MegaUsername joined!", Gui.MessageType.USER_SESSION);
    }
}