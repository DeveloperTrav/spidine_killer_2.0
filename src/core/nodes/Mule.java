package core.nodes;

import core.API;
import core.Utils;
import core.models.Item;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.Objects;

public class Mule extends TaskNode {
    private final Player player = Utils.getPlayerInBankAreaByName(Utils.getMuleName());

    @Override
    public boolean accept() {
        return canMule();
    }

    @Override
    public int execute() {
        if (getInventory().count("Red spiders' eggs") > 28) {
            if (!getBank().isOpen()) {
                if (Objects.nonNull(player)) {
                    if (getTrade().isOpen()) {
                        if (getTrade().isOpen(1)) {
                            if (getInventory().contains("Red spiders' eggs")) {
                                if (getTrade().addItem("Red spiders' eggs", 10000))
                                    sleepUntil(() -> !getInventory().contains("Red spiders' eggs"), (int) Calculations.nextGaussianRandom(5000, 1000));
                            } else {
                                if (getTrade().acceptTrade(1))
                                    sleepUntil(() -> getTrade().isOpen(2), (int) Calculations.nextGaussianRandom(4000, 2000));
                            }
                        } else {
                            if (API.allItemsInTrade()) {
                                if (getTrade().acceptTrade(2))
                                    sleepUntil(() -> !getTrade().isOpen(2), (int) Calculations.nextGaussianRandom(4000, 1000));
                            }
                        }
                    } else {
                        if (getTrade().tradeWithPlayer(player.getName())) {
                            sleepUntil(() -> getTrade().isOpen(), (int) Calculations.nextGaussianRandom(5000, 1500));
                        }
                    }
                }
            } else {
                if (getBank().close())
                    sleepUntil(() -> !getBank().isOpen(), (int)Calculations.nextGaussianRandom(3000, 2000));
            }
        } else {
            if (getBank().isOpen()) {
                if (!getInventory().isEmpty()) {
                    getBank().depositAllItems();
                    sleepUntil(() -> getInventory().isEmpty(), (int) Calculations.nextGaussianRandom(4000, 1000));
                }

                int count = getBank().count("Red spiders' eggs");
                if (count > 5) {
                    if (getBank().getWithdrawMode().equals(BankMode.NOTE)) {
                        getBank().withdraw("Red spiders' eggs", count - 5);
                        sleepUntil(() -> getInventory().contains("Red spiders' eggs"), (int) Calculations.nextGaussianRandom(4000, 2000));
                    } else {
                        if (getBank().setWithdrawMode(BankMode.NOTE))
                            sleepUntil(() -> getBank().getWithdrawMode().equals(BankMode.NOTE), (int)Calculations.nextGaussianRandom(3500, 1000));
                    }
                }
            } else {
                if (getWalking().shouldWalk(Calculations.random(2, 5))) {
                    getBank().openClosest();
                }
            }
        }

        return API.sleep();
    }
    private boolean canMule() {
        return API.muling;
    }
}
