package Simulacia.SystemoveUdalosti;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Udalost.TypUdalosti;
import Simulacia.Udalost.Udalost;

public class PosunCasu extends Udalost {

    public PosunCasu(double casUdalosti, SimulacneJadro simulacneJadro) {
        super(casUdalosti, simulacneJadro);
    }

    @Override
    public void vykonaj() {

        if (casUdalosti_ + getJadro().getJednotkovyPosunSimulacnehoCasu() < getJadro().getKoncovyCasReplikacie()) {
            getJadro().planujUdalost(this.setCasUdalosti(casUdalosti_ + getJadro().getJednotkovyPosunSimulacnehoCasu()));
        } else {
            if (getJadro().isChladenie() && getJadro().getPocetUdalostiVoFronteUdalosti() != 0) {
                getJadro().planujUdalost(this.setCasUdalosti(casUdalosti_ + getJadro().getJednotkovyPosunSimulacnehoCasu()));
            }
        }
        if (getJadro().getSpomalenieCasu() > 0) {
            try {
                Thread.sleep(getJadro().getSpomalenieCasu());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.POSUN_CASU;
    }

    public String getDodatocneInfo() {
        return "";
    }

}
