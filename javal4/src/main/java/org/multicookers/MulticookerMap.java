package org.multicookers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MulticookerMap extends MulticookerCollection {
    private Map<Integer, Multicooker> multicookerMap = new HashMap<>();

    @Override
    public void addMulticooker(Multicooker multicooker) {
        multicookerMap.put(multicooker.getId(), multicooker);
    }

    @Override
    public void removeMulticooker(int id) {
        multicookerMap.remove(id);
    }

    @Override
    public List<Multicooker> getAllMulticookers() {
        return new ArrayList<>(multicookerMap.values());
    }

    public void sortById() {
        multicookerMap = new TreeMap<>(multicookerMap);
    }
}
