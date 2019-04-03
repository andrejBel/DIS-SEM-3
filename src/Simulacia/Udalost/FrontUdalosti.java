package Simulacia.Udalost;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FrontUdalosti {

    int pocitacId_ = 0;

    private PriorityBlockingQueue<Udalost> frontUdalosti_ = new PriorityBlockingQueue<>();

    public FrontUdalosti() {
    }

    public synchronized void planujUdalost(Udalost udalost) {
        udalost.setId_(pocitacId_++);
        //System.out.println("planovana: " + udalost);
        this.frontUdalosti_.add(udalost);
    }

    public Udalost getNasledujucaUdalost() {
        return frontUdalosti_.poll();
    }

    public Udalost ukazNasledujucaUdalost() {
        return frontUdalosti_.peek();
    }

    public boolean isPrazdny() {
        return frontUdalosti_.isEmpty();
    }

    public PriorityBlockingQueue<Udalost> getFrontUdalosti() {
        return frontUdalosti_;
    }

    public void vynulujPocitadlo() {
        pocitacId_ = 0;
    }

}
