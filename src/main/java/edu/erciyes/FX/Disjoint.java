package edu.erciyes.FX;

import java.util.HashMap;
import java.util.Map;

public class Disjoint {
    private Map<String, String> parent = new HashMap<>();
    private Map<String, Integer> rank = new HashMap<>();

    public void makeSet(String item) {
        parent.put(item, item);
        rank.put(item, 0);
    }

    public String find(String item) {
        if (!parent.get(item).equals(item)) {
            parent.put(item, find(parent.get(item)));
        }
        return parent.get(item);
    }

    public void union(String item1, String item2) {
        String root1 = find(item1);
        String root2 = find(item2);
        if (!root1.equals(root2)) {
            if (rank.get(root1) > rank.get(root2)) {
                parent.put(root2, root1);
            } else if (rank.get(root1) < rank.get(root2)) {
                parent.put(root1, root2);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank.get(root1) + 1);
            }
        }
    }
}
