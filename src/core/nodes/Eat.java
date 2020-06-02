package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

public class Eat extends TaskNode {

    @Override
    public boolean accept() {
//        log("EAT: " + canEat());
        return canEat();
    }

    @Override
    public int execute() {
        if (getTabs().isOpen(Tab.INVENTORY)) {
            API.status = "Eating...";
            int tuna = getInventory().count("Tuna") - 1;
            if (getInventory().interact("Tuna", "Eat")) {
                sleepUntil(() -> getInventory().count("Tuna") == tuna, (int)Calculations.nextGaussianRandom(3000, 1000));
            }
        } else {
            API.status = "Opening inventory...";
            if (getTabs().openWithMouse(Tab.INVENTORY)) {
                sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), (int) Calculations.nextGaussianRandom(3000, 1500));
            }
        }

        return API.sleep();
    }

    private boolean canEat() {
        return (!getBank().isOpen() && getInventory().contains("Tuna") && getLocalPlayer().getHealthPercent() < Calculations.random(40, 50)) ||
                getInventory().isFull() && getInventory().contains("Tuna");
    }
}
