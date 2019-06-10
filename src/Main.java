import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        SimpleCSV input = new SimpleCSV(System.getProperty("user.home") +
                "\\IdeaProjects\\Kmean\\Info\\iris_all.csv", true);
        List<List<SimpleCSV.Element>> records = input.getRecords();
        List<List<Double>> data = new ArrayList<>();

        for (List<SimpleCSV.Element> record : records) {
            List<Double> xyz = new ArrayList<>();

            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).value instanceof Number) {
                    xyz.add((Double) record.get(i).value);
                }
            }

            data.add(xyz);
        }

        Scanner reader = new Scanner(System.in);
        System.out.print("Enter a `K` number followed by <enter>: ");
        int K = reader.nextInt();

        Kmean kmean = new Kmean(data);
        int it = 1;
        while(!kmean.clusterize(K) && it++ < 1000);
    }
}
