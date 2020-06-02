package core.nodes;

import core.API;
import core.Areas;
import core.Utils;
import core.models.Request;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.script.impl.TaskScript;

import java.util.Objects;

public class Bank extends TaskNode {



    @Override
    public boolean accept() {
//        log("BANK: " + canBank());
        return canBank();
    }

    @Override
    public int execute() {
        if (API.previousSardines == 1 && !getInventory().isFull()) {
            log("WAIT");
            sleepUntil(Utils::canLoot, 6400);
            API.previousSardines = -1;
            return API.sleep();
        }

        if (getBank().isOpen()) {
            API.status = "Banking...";
            if (!getInventory().isEmpty()) {
//                API.spiderEggs += getInventory().count("Red spiders' eggs");
                if (getBank().depositAllItems()) {
                    sleepUntil(() -> getInventory().isEmpty(), (int) Calculations.nextGaussianRandom(3500, 1500));
                    API.totalSpiderEggs = getBank().count("Red spiders' eggs");
                }
            }

            if (API.totalSpiderEggs >= 1500) {
                Utils.setRequest(new Request(getLocalPlayer().getName(), API.getSupplyList(), Areas.bank, getWorlds().getMyWorld().getRealID()));
                API.muling = true;
                API.client.send(Utils.getGson().toJson(Utils.getRequest()));
                return API.sleep();
            }

            if (!getBank().contains("Ardougne teleport") || !getBank().contains("Raw sardine") || !getBank().contains("Red spiders' eggs")
                    || !getBank().contains(i -> Objects.nonNull(i) && i.getName().contains("Combat potion")) || !getBank().contains("Tuna")) {
                getTabs().logout();
                API.logout();
            }

            if (!getInventory().contains("Ardougne teleport")) {
                if (getBank().withdraw("Ardougne teleport", 1)) {
                    sleepUntil(() -> getInventory().contains("Ardougne teleport"), (int)Calculations.nextGaussianRandom(3000, 1500));
                }
            }

            if (!getInventory().contains(i -> Objects.nonNull(i) && i.getName().contains("Combat potion"))) {
                if (getBank().contains("Combat potion(1)")) {
                    if (getBank().withdraw("Combat potion(1)")) {
                        sleepUntil(() -> getInventory().contains("Combat potion(1)"), (int)Calculations.nextGaussianRandom(3000, 1000));
                    }
                } else if (getBank().contains("Combat potion(2)")) {
                    if (getBank().withdraw("Combat potion(2)")) {
                        sleepUntil(() -> getInventory().contains("Combat potion(2)"), (int)Calculations.nextGaussianRandom(3000,1000));
                    }
                } else if (getBank().contains("Combat potion(3)")) {
                    if (getBank().withdraw("Combat potion(3)")) {
                        sleepUntil(() -> getInventory().contains("Combat potion(3)"), (int)Calculations.nextGaussianRandom(3000, 1000));
                    }
                } else {
                    if (getBank().withdraw("Combat potion(4)")) {
                        sleepUntil(() -> getInventory().contains("Combat potion(4)"), (int)Calculations.nextGaussianRandom(3000, 1000));
                    }
                }
            }

            if (!getInventory().contains("Tuna")) {
                int amount = getLocalPlayer().getHealthPercent() < 41 ? 2 : 1;
                if (getBank().withdraw("Tuna", amount)) {
                    sleepUntil(() -> getInventory().contains("Tuna"), (int)Calculations.nextGaussianRandom(4000, 1000));
                }
            }

            if (!getInventory().contains("Red spiders' eggs")) {
                if (getBank().withdraw("Red spiders' eggs", 1)) {
                    sleepUntil(() -> getInventory().contains("Red spiders' eggs"), (int)Calculations.nextGaussianRandom(3000, 1000));
                }
            }

            if (!getInventory().contains("Raw sardine")) {
                if (getBank().withdraw("Raw sardine", 7)) {
                    API.currentSardines = 7;
                    API.previousSardines = 7;
                    sleepUntil(() -> getInventory().contains("Raw sardine"), (int)Calculations.nextGaussianRandom(4000, 1000));
                }
            }

        } else {
            if (API.isInDungeonArea(getLocalPlayer())) {
                API.status = "Opening bank";
                if (getTabs().isOpen(Tab.INVENTORY)) {
                    if (getInventory().interact("Ardougne teleport", "Break")) {
                        API.previousSardines = -1;
                        sleepUntil(() -> !API.isInSpidineArea(getLocalPlayer()), (int) Calculations.nextGaussianRandom(3500, 1500));
                    }
                } else {
                    if (getTabs().openWithMouse(Tab.INVENTORY)) {
                        sleepUntil(() -> getBank().isOpen(), (int)Calculations.nextGaussianRandom(2500, 1000));
                    }
                }
            } else {
                API.status = "Opening bank...";
                if (getWalking().shouldWalk(Calculations.random(3, 5))) {
                    getBank().openClosest();
                    sleepUntil(() -> getWalking().shouldWalk(Calculations.random(3, 7)), (int) Calculations.nextGaussianRandom(4000, 2000));
                }
            }
        }

        return API.sleep();
    }

    private boolean canBank() {
        return !API.muling && ((getInventory().isFull() && !getInventory().contains("Tuna")) || (!getInventory().contains("Ardougne teleport")
                || !getInventory().contains("Raw sardine") && !getLocalPlayer().isInCombat() && !Utils.canLoot()));
    }
}
