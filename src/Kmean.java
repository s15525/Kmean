import java.util.*;
import java.util.stream.Collectors;

public class Kmean {
    class Point {
        double[] data;
        int centroid = -1;

        Point(double[] _data) {
            data = _data;
        }
        public double Distance(Point p) {
            double n = 0;

            for(int i = 0; i < data.length; i++) {
                n += Math.pow(data[i] - p.data[i], 2);
            }

            return Math.sqrt(n);
        }
        public Point Copy() {
            Point copy = new Point(data.clone());
            copy.centroid = centroid;

            return copy;
        }

        @Override
        public String toString() {
            return Arrays.toString(data);
        }
    }

    private List<Point> data = new ArrayList<>();
    private List<Point> centroids = new ArrayList<>();
    private List<List<Double>> maxMins = new ArrayList<>();
    private int dimensions;

    Kmean(List<List<Double>> _data) {
        for (List<Double> row : _data) {
            for(int i = 0; i < row.size(); i++) {
                if(maxMins.size() <= i) {
                    maxMins.add(Arrays.asList(0., 0., 0.));
                }
                List<Double> maxmin = maxMins.get(i);
                if(row.get(i) > maxmin.get(0)) maxmin.set(0, row.get(i));
                else if(row.get(i) < maxmin.get(1)) maxmin.set(1, row.get(i));

                maxmin.set(2, maxmin.get(2) + row.get(i) / _data.size());

                maxMins.set(i, maxmin);
            }

            data.add(new Point(row.stream().mapToDouble(d -> d).toArray()));
        }

        dimensions = data.get(0).data.length;
    }

    private int iteration = 1;
    private boolean ended = false;
    public boolean clusterize(int k) {
        if(ended == true) return false;

        // set random centroid points if dont exist
        if(centroids.size() == 0) {
            for (int ki = 0; ki < k; ki++) {
                int r = (int) Math.floor(Math.random() * data.size());
                Point rand = data.get(r).Copy();

                centroids.add(rand);
                centroids.get(ki).centroid = ki;
            }
        }

        List<Point> centroidsCopy =
                centroids.stream().map(Point::Copy).collect(Collectors.toList());

        for(Point point : data) {
            double minDistance = 1000000000;

            for(Point centroid : centroids) {
                double dist = point.Distance(centroid);
                if(dist < minDistance) {
                    point.centroid = centroid.centroid;
                    minDistance = dist;
                }
            }
        }

        double distSum = 0;
        for(Point centroid : centroids) {
            double[] sum = new double[dimensions];
            int points = 0;

            for(Point point : data) {
                if(point.centroid == centroid.centroid) {
                    distSum += centroid.Distance(point);
                    points++;
                    for(int dim = 0; dim < dimensions; dim++) {
                        sum[dim] += point.data[dim];
                    }
                }
            }

            for(int dim = 0; dim < dimensions; dim++) {
                sum[dim] /= points;
                centroid.data[dim] = sum[dim];
            }
        }

        System.out.println("Clusters distances sum: " + distSum);

        double diff = 0;
        for(int i = 0; i < centroidsCopy.size(); i++) {
            diff += centroids.get(i).Distance(centroidsCopy.get(i));
        }

        if(diff == 0) {
            System.out.println("Ended k-mean clusterization in " + iteration + " " +
                    "iterations.\n");
            for(Point centroid : centroids) {
                System.out.print("Cluster" + centroid.centroid + ": ");
                for(Point point : data) {
                    if (point.centroid == centroid.centroid) {
                        System.out.print(point + ", ");
                    }
                }
                System.out.println();
            }

            ended = true;
        }
        else {
            System.out.println("End of " + iteration++ + ". iteration\n");
        }
        return false;
    }
}


