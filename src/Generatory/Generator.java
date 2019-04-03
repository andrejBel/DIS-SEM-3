package Generatory;

import java.util.Random;

public abstract class Generator<T extends Number> {

    private static Random SEED_GENERATOR = new Random(); // TODO daj prec seed,  v ostrej verzii
    private static Object LOCK = new Object();

    protected Random generator_;

    public Generator() {
        synchronized (LOCK) {
            this.generator_ = new Random(SEED_GENERATOR.nextLong());
        }
    }

    public abstract T generuj();


}
