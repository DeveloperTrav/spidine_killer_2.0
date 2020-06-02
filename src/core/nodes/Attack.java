package core.nodes;

import core.API;
import core.Utils;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.Objects;

public class Attack extends TaskNode {

    @Override
    public boolean accept() {
//        log("ATTACK: " + canAttack());
        return canAttack();
    }

    @Override
    public int execute() {
        NPC spidine = Utils.getSpidine();

        if (Objects.nonNull(spidine)) {
            API.status = "Attacking...";
            if (spidine.interact("Attack")) {
                sleepUntil(() -> getLocalPlayer().isInCombat(), (int) Calculations.nextGaussianRandom(4000, 2000));
            }
        }

        return API.sleep();
    }

    private boolean canAttack() {
        return Utils.canAttack() && !getLocalPlayer().isInCombat();
    }
}
