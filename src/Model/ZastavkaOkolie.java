package Model;

public class ZastavkaOkolie {

    private ZastavkaKonfiguracia _zastavkaKonfiguracia;
    private double _casZaciatkuGenerovaniaPrichodovCestujucich;
    private double _casKoncaGenerovaniaPrichodovCestujucich;

    public ZastavkaOkolie(ZastavkaKonfiguracia zastavkaKonfiguracia, double casZaciatkuGenerovaniaPrichodovCestujucich, double casKoncaGenerovaniaPrichodovCestujucich) {
        this._zastavkaKonfiguracia = zastavkaKonfiguracia;
        this._casZaciatkuGenerovaniaPrichodovCestujucich = casZaciatkuGenerovaniaPrichodovCestujucich;
        this._casKoncaGenerovaniaPrichodovCestujucich = casKoncaGenerovaniaPrichodovCestujucich;
    }

    public ZastavkaKonfiguracia getZastavkaKonfiguracia() {
        return _zastavkaKonfiguracia;
    }

    public double getCasZaciatkuGenerovaniaPrichodovCestujucich() {
        return _casZaciatkuGenerovaniaPrichodovCestujucich;
    }

    public double getCasKoncaGenerovaniaPrichodovCestujucich() {
        return _casKoncaGenerovaniaPrichodovCestujucich;
    }

    public double getParameterExponencialnehoRozdelenia() {
        return (_casKoncaGenerovaniaPrichodovCestujucich - _casZaciatkuGenerovaniaPrichodovCestujucich) / _zastavkaKonfiguracia.getMaximalnyPocetCestujucich();
    }

}
