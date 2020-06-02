package core.nodes;

import core.API;
import core.Utils;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;

import java.util.Objects;

public class Sip extends TaskNode {

    @Override
    public boolean accept() {
        return canSip();
    }

    @Override
    public int execute() {
        if (getTabs().isOpen(Tab.INVENTORY)) {
            API.status = "Sipping potion...";
            if (getInventory().get(i -> Objects.nonNull(i) && i.getName().contains("Combat potion")).interact()) {
                sleepUntil(() -> getSkills().getRealLevel(Skill.ATTACK) != getSkills().getBoostedLevels(Skill.ATTACK), 7000);
            }
        } else {
            API.status = "Opening inventory...";
            if (getTabs().openWithMouse(Tab.INVENTORY)) {
                sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), (int) Calculations.nextGaussianRandom(3500, 1000));
            }
        }

        return API.sleep();
    }

    private boolean canSip() {
        return !getDialogues().inDialogue() && !Utils.canLoot() && API.isInSpidineArea(getLocalPlayer())
                && getSkills().getRealLevel(Skill.ATTACK) == getSkills().getBoostedLevels(Skill.ATTACK)
                && getInventory().contains(i -> Objects.nonNull(i) && i.getName().contains("Combat potion"));
    }
}
