package core.nodes;

import core.API;
import core.Utils;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.script.TaskNode;

import java.util.Objects;

public class Worldhop extends TaskNode {

    @Override
    public boolean accept() {
//        log("WORLDHOP: " + canWorldHop());
        return canWorldHop();
    }

    @Override
    public int execute() {
        World world = getWorlds().getRandomWorld(w -> w.isMembers() && !w.isDeadmanMode() && !w.isPVP() && w.getMinimumLevel() == 0
                && w.getID() != 318 && w.getID() != getClient().getCurrentWorld() && !w.isTournamentWorld() && !w.isTwistedLeague() && !w.isBountyWorld());
        getWorldHopper().hopWorld(world);
        sleepUntil(() -> getClient().getGameState() != GameState.HOPPING && getClient().getGameState() != GameState.LOADING && getClient().isLoggedIn(), 6000);

        return API.sleep();
    }

    private boolean canWorldHop() {
        return Objects.nonNull(Utils.getNearbyPlayer()) && !Utils.canLoot() && !Utils.canAttack() && !getLocalPlayer().isInCombat();
    }
}
