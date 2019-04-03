package Simulacia.Statistiky;

import Simulacia.Jadro.SimulacneJadro;

public abstract class Statistika implements IStatistika {

    SimulacneJadro jadro_;

    public Statistika(SimulacneJadro jadro_) {
        this.jadro_ = jadro_;
    }

}
