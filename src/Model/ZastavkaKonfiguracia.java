package Model;

import java.util.Objects;

public class ZastavkaKonfiguracia {

    private String _nazovZastavky;
    private int _maximalnyPocetCestujucich;
    private boolean _vystup;

    public ZastavkaKonfiguracia(String nazovZastavky, int maximalnyPocetCestujucich) {
        this._nazovZastavky = nazovZastavky;
        this._maximalnyPocetCestujucich = maximalnyPocetCestujucich;
        this._vystup = false;
    }

    public boolean isVystup() {
        return _vystup;
    }

    public void setVystupnaZastavka() {
        _vystup = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZastavkaKonfiguracia zastavkaKonfiguracia = (ZastavkaKonfiguracia) o;
        return Objects.equals(_nazovZastavky, zastavkaKonfiguracia._nazovZastavky);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_nazovZastavky);
    }

    public String getNazovZastavky() {
        return _nazovZastavky;
    }

    public double getMaximalnyPocetCestujucich() {
        return _maximalnyPocetCestujucich;
    }
}
