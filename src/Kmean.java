import javax.swing.*;
import java.sql.SQLOutput;
import java.util.*;

public class Kmean {
    Map<String, List<List<Double>>> data;
    double xCentroid;
    double yCentroid;
//    double zCentroid;
//    double tCentroid;
    Map<String, List<Double>> centroidMap = new HashMap<>();

    Kmean(Map<String, List<List<Double>>> data) {
        this.data = data;
    }

    public Map<String, List<Double>> centroid() {
        for (List<List<Double>> row :
                data.values()) {
            List<Double> xyz = new ArrayList<>();
            xCentroid = 0;
            yCentroid = 0;
//            zCentroid = 0;
//            tCentroid = 0;
            for (int i = 0; i < row.size(); i++) {
                List<Double> onexyz = row.get(i);
                xCentroid += onexyz.get(0);
                yCentroid += onexyz.get(1);
//                zCentroid += onexyz.get(2);
//                tCentroid += onexyz.get(3);
            }

            System.out.println(row.size());
            xyz.add(xCentroid/row.size());
            xyz.add(yCentroid/row.size());
//            xyz.add(zCentroid/row.size());
//            xyz.add(tCentroid/row.size());
            centroidMap.put(getKey(data, row), xyz);
        }
        return centroidMap;
    }

    public void distanceToCentroid(Map<String, List<Double>> centroidMap, List<Double> xyz, String name) {
        double sum = 0;
        double result = 0;
        Map<String, Double> distanceMap = new HashMap<>();
        for (List<Double> xyzCentroid :
                centroidMap.values()) {
            for (int i = 0; i < xyz.size(); i++) {
                sum += Math.pow((xyzCentroid.get(i) - xyz.get(i)), 2);
            }
            result = Math.sqrt(sum);
            distanceMap.put(getKey(centroidMap, xyzCentroid), result);
        }
        Optional<Map.Entry<String, Double>> minEntry = distanceMap.entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue));
        System.out.println(getKey(distanceMap,minEntry.get().getValue()) + " " + minEntry.get().getValue());
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}


