package org.multicookers;

import java.util.ArrayList;
import java.util.List;

public class MulticookerCollection {
    private List<Multicooker> multicookers = new ArrayList<>();

    public void addMulticooker(Multicooker multicooker) {
        multicookers.add(multicooker);
    }

    public void removeMulticooker(int id) {
        multicookers.removeIf(h -> h.getId() == id);
    }

    public List<Multicooker> getAllMulticookers() {
        return multicookers;
    }

    public Multicooker getMulticookerById(int id) {
        for (Multicooker multicooker : multicookers) {
            if (multicooker.getId() == id) {
                return multicooker;
            }
        }
        return null; 
    }
}
