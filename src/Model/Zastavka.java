package Model;

import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import Statistiky.WStatNamed;
import simulation.Sprava;

public class Zastavka extends SimulationEntity {

    private ZastavkaKonfiguracia _zastavkaKonfiguracia;
    private SimQueue<Sprava> _cestujuciNaZastavke;

    public Zastavka(Simulation mySim, ZastavkaKonfiguracia zastavkaKonfiguracia) {
        super(mySim);
        this._zastavkaKonfiguracia = zastavkaKonfiguracia;
        this._cestujuciNaZastavke = new SimQueue<>(new WStatNamed(mySim, "Priemerný počet cestujúcich na zastávke " + zastavkaKonfiguracia.getNazovZastavky()));
    }

    public ZastavkaKonfiguracia getZastavkaKonfiguracia() {
        return _zastavkaKonfiguracia;
    }

    public SimQueue<Sprava> getCestujuciNaZastavke() {
        return _cestujuciNaZastavke;
    }

    public void pridajCestujucehoNaZastavku(Sprava sprava) {
        _cestujuciNaZastavke.add(sprava);
    }

    @Override
    public void beforeReplication() {
        _cestujuciNaZastavke.clear();
    }
}
