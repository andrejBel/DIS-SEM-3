package Generatory;

public class GeneratorRovnomerneDiskretne extends Generator<Integer> {


    private int min_;
    private int max_;

    public GeneratorRovnomerneDiskretne() {
        super();
        this.min_ = 0;
        this.max_ = 1;
    }

    public GeneratorRovnomerneDiskretne(int min, int max) {
        super();
        this.min_ = min;
        this.max_ = max;
    }

    @Override
    public Integer generuj() {
        return this.min_ + this.generator_.nextInt(this.max_ - this.min_ + 1);
    }

}
