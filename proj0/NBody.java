public class NBody {
    public static double readRadius (String s) {
        In in = new In (s);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets (String s) {
        In in = new In (s);
        int numPlanets = in.readInt();
        in.readDouble();
        Planet[] allPlanets = new Planet[numPlanets];
        for (int i = 0; i < numPlanets; i++) {
            allPlanets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return allPlanets;
    }

    public static void main (String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        Double radius = readRadius(filename);
        Planet[] allPlanets = readPlanets(filename);

        StdDraw.enableDoubleBuffering();

        StdDraw.setScale(-radius, radius);

        for (double t = 0; t <= T; t += dt) {
            Double[] xForces = new Double[allPlanets.length];
            Double[] yForces = new Double[allPlanets.length];

            for (int i = 0; i < allPlanets.length; i++) {
                xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
                yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
            }

            for (int i = 0; i < allPlanets.length; i++) {
                allPlanets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");

            for (Planet p : allPlanets) {
                p.draw();
            }

            StdDraw.show();
            // waits 10 milliseconds
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", allPlanets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < allPlanets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    allPlanets[i].xxPos, allPlanets[i].yyPos, allPlanets[i].xxVel,
                    allPlanets[i].yyVel, allPlanets[i].mass, allPlanets[i].imgFileName);
        }
    }
}
