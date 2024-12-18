package org.multicookers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MulticookerList extends MulticookerCollection {

    public MulticookerList() {
    }
    private List<Multicooker> multicookers = new ArrayList<>();

    @Override
    public void addMulticooker(Multicooker multicooker) {
        multicookers.add(multicooker);
    }

    @Override
    public void removeMulticooker(int id) {
        multicookers.removeIf(h -> h.getId() == id);
    }

    @Override
    public List<Multicooker> getAllMulticookers() {
        return multicookers;
    }
    public void sortByPrice() {
        multicookers.sort(Comparator.comparingDouble(Multicooker::getPrice));
    }

    public void sortByCapacity() {
        multicookers.sort(Comparator.comparingInt(Multicooker::getCapacity));
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
