package Model;

import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import Statistiky.StatNamed;
import Statistiky.WStatNamed;
import simulation.Sprava;

import java.util.LinkedList;

public class Zastavka extends SimulationEntity {

    private ZastavkaKonfiguracia _zastavkaKonfiguracia;
    private SimQueue<Sprava> _cestujuciNaZastavke;
    private LinkedList<Sprava> _vozidlaNaZastavke;
    private StatNamed _priemernyCasCakaniaCestujecehoNaZastavke;
    private int _pocetCestujucich;

    public Zastavka(Simulation mySim, ZastavkaKonfiguracia zastavkaKonfiguracia) {
        super(mySim);
        this._zastavkaKonfiguracia = zastavkaKonfiguracia;
        //this._cestujuciNaZastavke = new SimQueue<>(new WStatNamed(mySim, "Priemerný počet cestujúcich na zastávke " + zastavkaKonfiguracia.getNazovZastavky())); // TODO uncoment
        this._cestujuciNaZastavke = new SimQueue<>(new WStatNamed(mySim, "Priemerná dĺžka frontu Z. " + _zastavkaKonfiguracia.getNazovZastavky()));
        this._vozidlaNaZastavke = new LinkedList<>();
        this._priemernyCasCakaniaCestujecehoNaZastavke = new StatNamed("Priemerný čas čakania cestujúceho Z. " + _zastavkaKonfiguracia.getNazovZastavky());
        this._pocetCestujucich = 0;
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
        _pocetCestujucich++;
    }

    public int getPocetCestujucich() {
        return _pocetCestujucich;
    }

    @Override
    public void beforeReplication() {
        _cestujuciNaZastavke.clear();
        _vozidlaNaZastavke.clear();
        _priemernyCasCakaniaCestujecehoNaZastavke.clear();
        this._pocetCestujucich = 0;
    }

    public void vlozVozidloDoFrontuVozidielCakajucichNaZastavke(Sprava spravaSVozidlom) {
        if (spravaSVozidlom.getVozidlo().isVozidloVoFronteVozidielCakajucichNaZastavke()) {
            throw new RuntimeException("Vozidlo uz je vo fronte!!!");
        }
        for (Sprava sprava: _vozidlaNaZastavke) {
            if (sprava.getVozidlo().getIdVozidla() == spravaSVozidlom.getVozidlo().getIdVozidla()) {
                throw new RuntimeException("Nemozno vlozit vozidlo, ktore uz caka");
            }
        }
        _vozidlaNaZastavke.add(spravaSVozidlom);
        spravaSVozidlom.getVozidlo().setVozidloVoFronteVozidielCakajucichNaZastavke(true);
        spravaSVozidlom.getVozidlo().setCasVstupuDoFrontuVozidielNaZastavke(mySim().currentTime());
    }

    public Sprava getPrveVolneVozidloZFrontuVozidielCakajucichNaZastavke() {
        for (Sprava sprava: _vozidlaNaZastavke) {
            if (sprava.getVozidlo().suVolneDvere() && sprava.getVozidlo().jeVolneMiestoVoVozidle()) {
                return (Sprava) sprava.createCopy();
            }
        }
        return null;
    }

    public boolean odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(Sprava spravaSVozidlom) {

        int indexVozidlaVoFronte = -1;
        int index = 0;
        for (Sprava sprava: _vozidlaNaZastavke) {
            if (sprava.getVozidlo().getIdVozidla() == spravaSVozidlom.getVozidlo().getIdVozidla()) {
                indexVozidlaVoFronte = index;
                break;
            }
            index++;
        }
        if (indexVozidlaVoFronte != -1) {
            _vozidlaNaZastavke.remove(indexVozidlaVoFronte);
            spravaSVozidlom.getVozidlo().setVozidloVoFronteVozidielCakajucichNaZastavke(false);
            return true;
        }
        return false;
    }

    public LinkedList<Sprava> getVozidlaNaZastavke() {
        return _vozidlaNaZastavke;
    }
}
