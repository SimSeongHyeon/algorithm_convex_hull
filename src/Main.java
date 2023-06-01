import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static List<Point> findConvexHull(List<Point> points) {
        List<Point> convexHull = new ArrayList<>();

        // Find the leftmost point
        Point startPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            if (currentPoint.x < startPoint.x) {
                startPoint = currentPoint;
            }
        }

        Point currentPoint = startPoint;
        Point nextPoint;

        do {
            convexHull.add(currentPoint);
            nextPoint = points.get(0);

            for (int i = 1; i < points.size(); i++) {
                if (points.get(i) == currentPoint) {
                    continue;
                }

                int crossProduct = crossProduct(currentPoint, nextPoint, points.get(i));
                if (nextPoint == currentPoint || crossProduct > 0 ||
                        (crossProduct == 0 && distance(currentPoint, points.get(i)) > distance(currentPoint, nextPoint))) {
                    nextPoint = points.get(i);
                }
            }

            currentPoint = nextPoint;
        } while (currentPoint != startPoint);

        return convexHull;
    }

    private static int crossProduct(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
    }

    private static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }

    public static void main(String[] args) {
        File inputFile = new File("points_ch.txt");
        List<Point> points = new ArrayList<>();
        int[] a = new int[1000];
        int[] b = new int[1000];
        int i = 0;

        try {
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                String[] coordinates = line.split(" ");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);

                a[i] = Integer.parseInt(coordinates[0]);
                b[i] = Integer.parseInt(coordinates[1]);

                i++;
                points.add(new Point(x, y));
            }
            scanner.close();

            List<Point> convexHull = findConvexHull(points);

            File outputFile = new File("output.asu");
            PrintWriter writer = new PrintWriter(outputFile);

            writer.println("%Animal 2.0");
            writer.println("title \"CWNU 2023 Algorithm Animal Assignment\"");
            writer.println("author \"Sim, Seong Hyeon 20163060 <ssh5707@naver.com>\"\n");

            writer.println("{");

            Point p1;
            Point p2;

            for(int j = 1; j <= a.length; j++) {
                int cnt = 0;
                for (int k = 0; k < convexHull.size(); k++) {
                    p1 = convexHull.get(k);
                    if (a[j - 1] == p1.x && b[j - 1] == p1.y || j == 1000) {
                        writer.println("circle \"c" + j + "\" (" + a[j - 1] + "," + b[j - 1] + ") radius 3 filled");
                        cnt = 1;
                        break;
                    }
                }
                if (cnt != 1) writer.println("circle \"c" + j + "\" (" + a[j - 1] + "," + b[j - 1] + ") radius 1 filled");
            }

            writer.println("}\n");

            int k = 1;

            for (int j = 0; j < convexHull.size() - 1; j++) {
                p1 = convexHull.get(j);
                p2 = convexHull.get(j + 1);
                writer.println("line " + "\"l" + k + "\" (" + p1.x + "," + p1.y + ") (" + p2.x + "," + p2.y + ")");
                k++;
            }

            Point pi = convexHull.get(convexHull.size() - 1);
            Point pj = convexHull.get(0);
            writer.println("line " + "\"l" + k +"\" (" + pi.x + "," + pi.y + ") (" + pj.x + "," + pj.y + ")");
            //test
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
