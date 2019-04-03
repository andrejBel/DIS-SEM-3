package Generatory;

public class GeneratorTrojuholnikove extends Generator<Double> {

    private double min_;
    private double max_;
    private double mode_;

    public GeneratorTrojuholnikove(Double min, Double mode, Double max) {
        super();
        this.min_ = min;
        this.mode_ = mode;
        this.max_ = max;
    }

    @Override
    public Double generuj() {
        double p = this.generator_.nextDouble();
        double q = 1.0 - p;
        return p <= (this.mode_ - this.min_) / (this.max_ - this.min_) ? this.min_ + Math.sqrt((this.max_ - this.min_) * (this.mode_ - this.min_) * p) : this.max_ - Math.sqrt((this.max_ - this.min_) * (this.max_ - this.mode_) * q);
    }

}
