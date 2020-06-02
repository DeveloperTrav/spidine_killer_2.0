package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

public class Teleport extends TaskNode {

    @Override
    public boolean accept() {
//        log("EMERG TELE: " + canTeleport());
        return canTeleport();
    }

    @Override
    public int execute() {
        if (getTabs().isOpen(Tab.INVENTORY)) {
            if (getInventory().interact("Ardougne teleport", "Break")) {
                sleepUntil(() -> !getInventory().contains("Ardougne teleport"), (int)Calculations.nextGaussianRandom(3000, 1000));
            }
        } else {
            if (getTabs().openWithMouse(Tab.INVENTORY)) {
                sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), (int) Calculations.nextGaussianRandom(3000, 1000));
            }
        }

        return API.sleep();
    }

    private boolean canTeleport() {
        return getLocalPlayer().getHealthPercent() < Calculations.random(15, 20);
    }
}
