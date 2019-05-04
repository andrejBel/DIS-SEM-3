package Model;

import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Info.CestujuciInfo;
import OSPABA.Simulation;
import Utils.Helper;
import simulation.KONSTANTY;
import simulation.SimulaciaDopravy;

import static simulation.KONSTANTY.NULL_TIME;

public class Cestujuci {

    private final long _idCestujuceho;
    private final ZastavkaKonfiguracia _zastavka; // _zastavka na ktorej nastupil
    private STAV_CESTUJUCI _stavCestujuci;

    private SimulaciaDopravy _simulacia;
    private final double _casPrichoduNaZastavku;
    private double _casZaciatkuNastupovania = NULL_TIME;
    private double _casKoncaNastupovania = NULL_TIME;
    private double _casZaciatkuVystupovania = NULL_TIME;
    private double _casKoncaVystupovania = NULL_TIME; // cas prichoduNaStadion
    private Vozidlo _vozidlo = null; // _vozidlo, v ktorom sa vezie, viezol
    private Integer _indexNastupnychDveri = null;
    private Integer _indexVystupnychDveri = null;

    public Cestujuci(SimulaciaDopravy simulacia, long idCestujuceho, ZastavkaKonfiguracia zastavka, double casPrichoduNaZastavku, STAV_CESTUJUCI stavCestujuci) {
        this._simulacia = simulacia;
        this._idCestujuceho = idCestujuceho;
        this._zastavka = zastavka;
        this._casPrichoduNaZastavku = casPrichoduNaZastavku;
        this._stavCestujuci = stavCestujuci;
    }

    public long getIdCestujuceho() {
        return _idCestujuceho;
    }

    public ZastavkaKonfiguracia getZastavka() {
        return _zastavka;
    }

    public double getCasPrichoduNaZastavku() {
        return _casPrichoduNaZastavku;
    }

    public double getCasZaciatkuNastupovania() {
        return _casZaciatkuNastupovania;
    }

    public double getCasKoncaNastupovania() {
        return _casKoncaNastupovania;
    }

    public double getCasZaciatkuVystupovania() {
        return _casZaciatkuVystupovania;
    }

    public double getCasKoncaVystupovania() {
        return _casKoncaVystupovania;
    }

    public Vozidlo getVozidlo() {
        return _vozidlo;
    }

    public Integer getIndexVstupnychDveri() {
        return _indexNastupnychDveri;
    }

    public Integer getIndexVystupnychDveri() {
        return _indexVystupnychDveri;
    }

    public void setCasZaciatkuNastupovania(double casZaciatkuNastupovania) {
        this._casZaciatkuNastupovania = casZaciatkuNastupovania;
    }

    public void setCasKoncaNastupovania(double casKoncaNastupovania) {
        this._casKoncaNastupovania = casKoncaNastupovania;
    }

    public void setCasZaciatkuVystupovania(double casZaciatkuVystupovania) {
        this._casZaciatkuVystupovania = casZaciatkuVystupovania;
    }

    public void setCasKoncaVystupovania(double casKoncaVystupovania) {
        this._casKoncaVystupovania = casKoncaVystupovania;
    }

    public void setVozidlo(Vozidlo vozidlo) {
        if (_vozidlo != null) {
            throw new RuntimeException("Vozidlo je uz setnute");
        }
        this._vozidlo = vozidlo;
    }

    public void setIndexNastupnychDveri(Integer indexNastupnychDveri) {
        this._indexNastupnychDveri = indexNastupnychDveri;
    }

    public STAV_CESTUJUCI getStavCestujuci() {
        return _stavCestujuci;
    }

    public void setStavCestujuci(STAV_CESTUJUCI stavCestujuci) {
        this._stavCestujuci = stavCestujuci;
    }

    public void setIndexVytupnychDveri(Integer indexVytupnychDveri) {
        this._indexVystupnychDveri = indexVytupnychDveri;
    }

    public CestujuciInfo getCestujuciInfo() {
        return new CestujuciInfo(_idCestujuceho,
                _zastavka.getNazovZastavky(),
                _casPrichoduNaZastavku,
                this.getCasCakaniaNaZastavke(),
                _casZaciatkuNastupovania,
                _casKoncaNastupovania,
                _casZaciatkuVystupovania,
                _casKoncaVystupovania,
                _vozidlo == null ? "-" : String.valueOf(_vozidlo.getIdVozidla()),
                _indexNastupnychDveri,
                _indexVystupnychDveri,
                _stavCestujuci.getNazov()
        );
    }
    public double getCasCakaniaNaZastavke() {
        if (_casZaciatkuNastupovania == NULL_TIME) {
            double casCakania = _simulacia.currentTime() - this._casPrichoduNaZastavku;
            if (casCakania < 0) {
                throw new RuntimeException("Porusenie kauzality");
            }
            return casCakania;
        } else {
            return this._casZaciatkuNastupovania - this._casPrichoduNaZastavku;
        }
    }

    public boolean jeOchotnyNastupit(Vozidlo vozidlo) {
        if (vozidlo.getTypVozidla().isAutobus()) {
            return true;
        }
        double casCakania = getCasCakaniaNaZastavke();
        return casCakania > KONSTANTY.KEDY_JE_OCHOTNY_NASTUPIT_DO_MINIBUSU;
    }

}
