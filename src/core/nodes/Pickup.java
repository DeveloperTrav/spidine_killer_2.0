package core.nodes;

import core.API;
import core.Utils;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.Objects;

public class Pickup extends TaskNode {

    @Override
    public boolean accept() {
//        log("PICKUP: " + canPickup());
        return canPickup();
    }

    @Override
    public int execute() {
        GroundItem egg = Utils.getEgg();
        int eggs = getInventory().count("Red spiders' eggs");

        if (Objects.nonNull(egg)) {
            API.status = "Looting...";
            if (egg.interact("Take")) {
                sleepUntil(() -> getInventory().count("Red spiders' eggs") != eggs, 6400);
            }
        }

        return API.sleep();
    }

    private boolean canPickup() {
        return !Utils.canAttack() && !getInventory().isFull()
                && Utils.canLoot() && !getDialogues().inDialogue();
    }
}
