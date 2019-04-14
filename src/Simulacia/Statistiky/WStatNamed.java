package Simulacia.Statistiky;

import OSPABA.Simulation;
import OSPStat.WStat;

public class WStatNamed extends WStat {

    private String name_;

    public WStatNamed(Simulation sim, String name) {
        super(sim);
        this.name_ = name;
    }

    public WStatNamed(WStatNamed original) {
        super(original);
        this.name_ = original.name_;
    }

    public String getName() {
        return name_;
    }

}
