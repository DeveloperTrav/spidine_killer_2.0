package core.nodes;

import core.API;
import core.Areas;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.Objects;

public class Traverse extends TaskNode {

    @Override
    public boolean accept() {
//        log("TRAVERSE: " + canTraverse());
        return canTraverse();
    }

    @Override
    public int execute() {
        if (getBank().isOpen()) {
            API.status = "Closing bank...";
            getBank().close();
            sleepUntil(() -> !getBank().isOpen(), (int) Calculations.nextGaussianRandom(2000, 1000));
        }

        if (API.isInDungeonArea(getLocalPlayer())) {
            API.status = "Walking to altar...";
            if (getWalking().shouldWalk(Calculations.random(3, 6))) {
                getWalking().walk(Areas.spidineArea.getCenter().getRandomizedTile(3));
            }
        } else {
            if (API.isInTowerArea(getLocalPlayer())) {
                GameObject trapDoor = getGameObjects().closest(o -> Objects.nonNull(o) && o.getName().equals("Trapdoor") && API.isInTowerArea(o));
                API.status = "Opening trapdoor...";

                if (Objects.nonNull(trapDoor)) {
                    if (trapDoor.hasAction("Open")) {
                        if (trapDoor.interact("Open")) {
                            sleepUntil(() -> trapDoor.hasAction("Climb-down"), (int) Calculations.nextGaussianRandom(5000, 1000));
                        }
                    } else {
                        if (trapDoor.interact("Climb-down")) {
                            sleepUntil(() -> API.isInDungeonArea(getLocalPlayer()), 6000);
                        }
                    }
                } else {
                    if (getWalking().shouldWalk(Calculations.random(3, 5))) {
                        getWalking().walk(Areas.trapDoorArea.getCenter().getRandomizedTile(2));
                    }
                }
            } else {
                if (API.isInTowerDoorArea(getLocalPlayer())) {
                    GameObject door = getGameObjects().closest(o -> Objects.nonNull(o) && o.getName().equals("Tower door") && API.isInTowerDoorArea(o));
                    API.status = "Opening door...";

                    if (Objects.nonNull(door)) {
                        if (door.interact("Open")) {
                            sleepUntil(() -> API.isInTowerArea(getLocalPlayer()), (int)Calculations.nextGaussianRandom(4000, 1000));
                        }
                    }
                } else {
                    API.status = "Walking to tower...";

                    if (getWalking().shouldWalk(Calculations.random(4, 7))) {
                        getWalking().walk(Areas.towerDoor.getCenter().getRandomizedTile(2));
                    }
                }
            }
        }

        return API.sleep();
    }

    private boolean canTraverse() {
        return !API.isInSpidineArea(getLocalPlayer()) && !getInventory().isFull() && getInventory().contains("Ardougne teleport")
                && getInventory().contains("Raw sardine") && getInventory().contains("Red spiders' eggs");
    }
}
