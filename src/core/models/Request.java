package core.models;

import org.dreambot.api.methods.map.Area;

import java.util.List;

public class Request {
    private String name;
    private List<Item> items;
    private Area bankArea;
    private int world;

    public Request(String name, List<Item> items, Area bankArea, int world) {
        this.name = name;
        this.items = items;
        this.bankArea = bankArea;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public Area getBankArea() {
        return bankArea;
    }

    public int getWorld() {
        return world;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setBankArea(Area bankArea) {
        this.bankArea = bankArea;
    }

    public void setWorld(int world) {
        this.world = world;
    }
}
