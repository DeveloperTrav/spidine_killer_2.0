package core;

import core.client.MuleClient;
import core.models.Item;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.wrappers.interactive.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class API {
    private static MethodContext context;
    public static MuleClient client;
    public static String status = "Starting script...";
    public static int totalSpiderEggs = 0;
    public static int spiderEggs = 0;
    public static int previousSardines = 0;
    public static int currentSardines = 0;
    public static boolean muling = false;

    public API(MethodContext context) {
        API.context = context;
    }

    public static int sleep() {
        return (int) Calculations.nextGaussianRandom(500, 150);
    }

    public static boolean isInSpidineArea(Entity e) {
        return Areas.spidineArea.contains(e);
    }
    public static boolean isInTowerArea(Entity e) { return Areas.tower.contains(e); }
    public static boolean isInTowerDoorArea(Entity e) { return Areas.towerDoor.contains(e); }
    public static boolean isInDungeonArea(Entity e) { return Areas.dungeon.contains(e); }
    public static boolean isInBankArea(Entity e) { return Areas.bank.contains(e); }
    public static void logout() {
        if (context.getBank().isOpen()) {
            context.getBank().close();
            MethodProvider.sleepUntil(() -> !context.getBank().isOpen(), (int)Calculations.nextGaussianRandom(4000, 1000));
        }
        context.getTabs().logout();
        Utils.getScript().stop();
    }

    public static List<Item> getSupplyList() {
        List<Item> supplies = new ArrayList<>();
        if (!(context.getBank().count("Ardougne teleport") >= 70))
            supplies.add(new Item("Ardougne teleport", 100));
        if (!(context.getBank().count("Raw sardine") >= 500))
            supplies.add(new Item("Raw sardine", 1000));
        if (!(context.getBank().count("Tuna") >= 150))
            supplies.add(new Item("Tuna", 350));
        if (!(context.getBank().count(i -> Objects.nonNull(i) && i.getName().contains("Combat potion")) >= 100))
            supplies.add(new Item("Combat potion(4)", 200));
        return supplies;
    }

    public static boolean allItemsInTrade() {
        if (Objects.nonNull(Utils.getRequest())) {
            if (Objects.nonNull(Utils.getRequest().getItems())) {
                for (Item item : Utils.getRequest().getItems()) {
                    if (!context.getTrade().contains(false, item.getAmount(), item.getName())) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
