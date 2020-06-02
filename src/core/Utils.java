package core;

import com.google.gson.Gson;
import core.models.Request;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.Objects;

public class Utils {
    private static AbstractScript a;
    private static MethodContext context;
    private static Gson gson;
    private static String muleName;
    private static Request request;

    public Utils(MethodContext context, Gson gson) {
        Utils.context = context;
        Utils.a = (AbstractScript)context;
        Utils.gson = gson;
        Utils.muleName = "";
    }

    public static NPC getSpidine() {
        return context.getNpcs().closest(n -> Objects.nonNull(n) && n.getName().equals("Spidine") && API.isInDungeonArea(n)
                && n.isInteracting(context.getLocalPlayer()));
    }

    public static GroundItem getEgg() {
        return context.getGroundItems().closest(g -> Objects.nonNull(g) && g.getName().equals("Red spiders' eggs"));
    }

    public static Player getNearbyPlayer() {
        return context.getPlayers().closest(p -> Objects.nonNull(p) && API.isInSpidineArea(p) && !p.getName().equals(context.getLocalPlayer().getName()));
    }

    public static Player getPlayerInBankAreaByName(String name) {
        return context.getPlayers().closest(p -> Objects.nonNull(p) && API.isInBankArea(p) && p.getName().equals(name));
    }

    public static AbstractScript getScript() {
        return a;
    }

    public static Gson getGson() {
        return gson;
    }

    public static String getMuleName() {
        return muleName;
    }

    public static Request getRequest() {
        return request;
    }

    public static boolean canLoot() {
        return Objects.nonNull(getEgg());
    }

    public static boolean canAttack() {
        return Objects.nonNull(getSpidine());
    }

    public static void setMuleName(String muleName) {
        Utils.muleName = muleName;
    }

    public static void setRequest(Request request) {
        Utils.request = request;
    }
}
