public class Planet {
    // current x position
    public double xxPos;
    // current y position
    public double yyPos;
    // current velocity in the x direction
    public double xxVel;
    // current velocity in the y direction
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet (double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet (Planet p) {
        this (p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    public double calcDistance (Planet p) {
        return Math.sqrt((p.xxPos - this.xxPos) * (p.xxPos - this.xxPos) + (p.yyPos - this.yyPos) * (p.yyPos - this.yyPos));
    }

    // G 只能被赋值一次
    private static final double G = 6.67e-11;

    public double calcForceExertedBy (Planet p) {
        double r = calcDistance(p);
        return G * this.mass * p.mass / (r * r);
    }

    public double calcForceExertedByX (Planet p) {
        return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
    }

    public double calcForceExertedByY (Planet p) {
        return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);
    }

    public double calcNetForceExertedByX (Planet[] allPlanets) {
        double NetForceX = 0;
        for (int i = 0; i < allPlanets.length; i++) {
            if (this.equals(allPlanets[i])) {
                continue;
            }
            NetForceX += this.calcForceExertedByX(allPlanets[i]);
        }
        return NetForceX;
    }

    public double calcNetForceExertedByY (Planet[] allPlanets) {
        double NetForceY = 0;
        for (Planet s : allPlanets) {
            if (this.equals(s)) {
                continue;
            }
            NetForceY += this.calcForceExertedByY(s);
        }
        return NetForceY;
    }

    public void update (double dt, double fX, double fY) {
        double aX = fX / this.mass;
        double aY = fY / this.mass;
        this.xxVel = this.xxVel + dt * aX;
        this.yyVel = this.yyVel + dt * aY;
        this.xxPos = this.xxPos + this.xxVel * dt;
        this.yyPos = this.yyPos + this.yyVel * dt;
    }

    public void draw () {
        StdDraw.picture(xxPos, yyPos, "images/" + this.imgFileName);
    }

}