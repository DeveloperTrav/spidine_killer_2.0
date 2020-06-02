package core.nodes;

import core.API;
import core.Utils;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import sun.applet.AppletResourceLoader;

import java.util.Objects;

public class Summon extends TaskNode {

    @Override
    public boolean accept() {
//        log("SUMMON: " + canSummon());
        return canSummon();
    }

    @Override
    public int execute() {
        API.status = "Summoning...";
        GameObject altar = getGameObjects().closest(o -> Objects.nonNull(o) && o.getName().equals("Symbol of life") && API.isInSpidineArea(o));

        if (Objects.nonNull(altar)) {
            log("PREVIOUS: " + API.previousSardines);
            log("CURRENT: " + API.currentSardines);
            if (API.currentSardines != API.previousSardines) {
                sleepUntil(Utils::canLoot, 7000);
                API.previousSardines = getInventory().count("Raw sardine");
                API.currentSardines = API.previousSardines;
                return API.sleep();
            }

            if (altar.interact("Activate")) {
                API.currentSardines--;
                sleepUntil(() -> getDialogues().inDialogue(), (int) Calculations.nextGaussianRandom(4000, 1000));
                getKeyboard().type(" ");
                sleepUntil(Utils::canAttack, 8000);
            }
        }

        return API.sleep();
    }

    private boolean canSummon() {
        return API.isInSpidineArea(getLocalPlayer()) && !Utils.canLoot() && !Utils.canAttack()
                && !getDialogues().inDialogue() && getInventory().contains("Raw sardine");
    }
}
