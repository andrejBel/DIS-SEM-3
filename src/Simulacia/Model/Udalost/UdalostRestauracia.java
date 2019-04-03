package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Restauracia;
import Simulacia.SimulaciaRestauracia;
import Simulacia.Udalost.Udalost;

public abstract class UdalostRestauracia extends Udalost {

    protected SimulaciaRestauracia simulacia_;
    protected Restauracia restauracia_;

    public UdalostRestauracia(double casUdalosti, SimulacneJadro simulacneJadro) {
        super(casUdalosti, simulacneJadro);
        simulacia_ = (SimulaciaRestauracia) simulacneJadro;
        restauracia_ = simulacia_.getRestauracia();
    }

}
