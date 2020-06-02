package core.nodes;

import core.API;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;

public class Dialogue extends TaskNode {

    @Override
    public boolean accept() {
        return canDialogue();
    }

    @Override
    public int execute() {
        if (getDialogues().inDialogue()) {
            getKeyboard().type(" ");
            sleepUntil(() -> !getDialogues().inDialogue(), (int) Calculations.nextGaussianRandom(5000, 2000));
        }

        return API.sleep();
    }

    private boolean canDialogue() {
        return getDialogues().inDialogue();
    }
}
