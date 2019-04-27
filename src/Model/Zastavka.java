package Model;

import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import Statistiky.StatNamed;
import Statistiky.WStatNamed;
import simulation.Sprava;

public class Zastavka extends SimulationEntity {

    private ZastavkaKonfiguracia _zastavkaKonfiguracia;
    private SimQueue<Sprava> _cestujuciNaZastavke;
    private StatNamed _priemernyCasCakaniaCestujecehoNaZastavke;

    public Zastavka(Simulation mySim, ZastavkaKonfiguracia zastavkaKonfiguracia) {
        super(mySim);
        this._zastavkaKonfiguracia = zastavkaKonfiguracia;
        //this._cestujuciNaZastavke = new SimQueue<>(new WStatNamed(mySim, "Priemerný počet cestujúcich na zastávke " + zastavkaKonfiguracia.getNazovZastavky())); // TODO uncoment
        this._cestujuciNaZastavke = new SimQueue<>(new WStatNamed(mySim, "Priemerná dĺžka frontu Z. " + _zastavkaKonfiguracia.getNazovZastavky()));
        this._priemernyCasCakaniaCestujecehoNaZastavke = new StatNamed("Priemerný čas čakania cestujúceho Z. " + _zastavkaKonfiguracia.getNazovZastavky());
    }

    public ZastavkaKonfiguracia getZastavkaKonfiguracia() {
        return _zastavkaKonfiguracia;
    }

    public SimQueue<Sprava> getCestujuciNaZastavke() {
        return _cestujuciNaZastavke;
    }

    public StatNamed getPriemernyCasCakaniaCestujecehoNaZastavke() {
        return _priemernyCasCakaniaCestujecehoNaZastavke;
    }

    public void pridajCestujucehoNaZastavku(Sprava sprava) {
        _cestujuciNaZastavke.add(sprava);
    }

    @Override
    public void beforeReplication() {
        _cestujuciNaZastavke.clear();
        _priemernyCasCakaniaCestujecehoNaZastavke.clear();
    }


}
