package Generatory;

public class GeneratorExponencialne extends Generator<Double> {

    private double mean;

    public GeneratorExponencialne(double mean) {
        super();
        this.mean = mean;
    }

    @Override
    public Double generuj() {
        return (-(Math.log(1.0 - generator_.nextDouble()) / (1.0 / mean)));
    }

}
