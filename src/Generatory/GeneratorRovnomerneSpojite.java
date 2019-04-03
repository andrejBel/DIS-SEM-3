package Generatory;

public class GeneratorRovnomerneSpojite extends Generator<Double> {

    private double min_;
    private  double max_;

    public GeneratorRovnomerneSpojite() {
        super();
        this.min_ = 0.0;
        this.max_ = 1.0;
    }

    public GeneratorRovnomerneSpojite(double min, double max) {
        super();
        this.min_ = min;
        this.max_ = max;
    }

    @Override
    public Double generuj() {
        return this.min_ + (this.max_ - this.min_) * this.generator_.nextDouble();
    }

}
