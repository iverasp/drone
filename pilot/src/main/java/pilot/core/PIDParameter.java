package pilot.core;

public class PIDParameter {

    private static PIDParameter instance = null;

    protected  PIDParameter()  {}

    public static PIDParameter getInstance() {
        if (instance == null) {
            instance = new PIDParameter();
        }
        return instance;
    }

    private double p;
    private double i;
    private double d;

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
