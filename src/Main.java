import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        SimpleCSV input = new SimpleCSV("C:\\Users\\Lukasz Tolpa\\IdeaProjects\\Kmean\\Info\\dane_testowe.csv", true);
        List<List<SimpleCSV.Element>> records = input.getRecords();
        Map<String, List<List<Double>>> test = new HashMap<>();

        for (List<SimpleCSV.Element> record :
                records) {
            List<Double> xyz = new ArrayList<>();

            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).value instanceof Number) {
                    xyz.add((Double) record.get(i).value);
                } else if (!record.get(i).name.equals("nr")) {
                    try {
                        List<List<Double>> pom = test.get(record.get(i).value);
                        pom.add(xyz);
                        test.put((String) record.get(i).value, pom);
                    } catch (NullPointerException a) {
                        List<List<Double>> pom = new ArrayList<>();
                        pom.add(xyz);
                        test.put((String) record.get(i).value, pom);
                    }
                }
            }
        }

        Kmean kmean = new Kmean(test);
        for (List<List<Double>> row:
             test.values()){
            for (int i = 0 ; i < row.size() ; i++)
            kmean.distanceToCentroid(kmean.centroid(),row.get(i));
        }
        System.out.println(test);

    }
}
