import com.google.gson.Gson;
import core.API;
import core.Areas;
import core.Utils;
import core.client.MuleClient;
import core.models.Item;
import core.models.Request;
import core.nodes.*;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.api.methods.walking.web.node.AbstractWebNode;
import org.dreambot.api.methods.walking.web.node.impl.BasicWebNode;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.utilities.Timer;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

@ScriptManifest(category = Category.COMBAT, name = "Spidine Killer 2", author = "NotJohn", version = 2.0)
public class Main extends TaskScript {
    private API api = new API(this);
    private Utils utils = new Utils(this, new Gson());
    private Timer timer = new Timer();

    public void onStart() {
        API.previousSardines = getInventory().count("Raw sardine");
        API.currentSardines = getInventory().count("Raw sardine");
        try {
            API.client = new MuleClient(new URI("ws://localhost:4457"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                API.client.connect();
                sleepUntil(() -> API.client.isOpen(), 10000);
                ArrayList<Item> items = new ArrayList<Item>() {{
                    add(new Item("Tuna", 10));
                    add(new Item("Raw sardine", 10));
                    add(new Item("Knife", 1));
                }};
                API.client.send(Utils.getGson().toJson(new Request(getLocalPlayer().getName(), items, Areas.bank, getWorlds().getMyWorld().getRealID())));
            }
        }.start();

//        addNodes(new Worldhop());
//        addNodes(new Teleport());
//        addNodes(new Eat());
//        addNodes(new Sip());
//        addNodes(new Pickup());
//        addNodes(new Attack());
//        addNodes(new Bank());
//        addNodes(new Traverse());
//        addNodes(new Summon());
//        addNodes(new Dialogue());

        //Inside tower
        BasicWebNode bw1 = new BasicWebNode(2647, 3222);
        BasicWebNode bw2 = new BasicWebNode(2647, 3214);

        bw1.addConnections(bw2);
        bw2.addConnections(bw1);

        //Dungeon
        BasicWebNode bw3 = new BasicWebNode(3044, 4373);
        BasicWebNode bw4 = new BasicWebNode(3046, 4366);
        BasicWebNode bw5 = new BasicWebNode(3044, 4360);

        bw3.addConnections(bw4);
        bw4.addConnections(bw3);
        bw4.addConnections(bw5);
        bw5.addConnections(bw4);

        WebFinder webFinder = getWalking().getWebPathFinder();
        AbstractWebNode[] webNodes = { bw1, bw2, bw3, bw4, bw5 };
        for (AbstractWebNode webNode : webNodes) {
            webFinder.addWebNode(webNode);
        }
    }

    @Override
    public void onExit() {
        API.client.close();
    }

    public void onPaint(Graphics2D g) {
        g.drawString("Status: " + API.status, 10, 30);
        g.drawString("Run time: " + timer.formatTime(this.timer.elapsed()), 10, 45);
        g.drawString("Eggs collected: " + API.spiderEggs, 10, 60);
        g.drawString("Total eggs collected: " + API.totalSpiderEggs, 10, 75);
    }
}
