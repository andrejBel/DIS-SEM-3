package Generatory;

public class EmpirickeDiskretneParameter {

    private int min_;
    private int max_;
    private double pravdepodobnostNastatia_;

    public EmpirickeDiskretneParameter(int min, int max, double pravdepodobnostNastatia) {
        this.min_ = min;
        this.max_ = max;
        this.pravdepodobnostNastatia_ = pravdepodobnostNastatia;
    }

    public int getMin() {
        return min_;
    }

    public int getMax() {
        return max_;
    }

    public double getPravdepodobnostNastatia() {
        return pravdepodobnostNastatia_;
    }
}
