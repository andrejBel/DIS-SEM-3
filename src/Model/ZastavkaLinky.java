package Model;

public class ZastavkaLinky {

    private ZastavkaKonfiguracia _zastavka;
    private double _casPresunuNaDalsiuZastavku;
    private ZastavkaKonfiguracia _dalsiaZastavka;
    private double _vzdialenostKStadionu = -1;


    public ZastavkaLinky(ZastavkaKonfiguracia zastavka, double casPresunuNaDalsiuZastavku, ZastavkaKonfiguracia dalsiaZastavka) {
        if (zastavka == null || dalsiaZastavka == null) {
            throw new RuntimeException("Null");
        }
        this._zastavka = zastavka;
        this._casPresunuNaDalsiuZastavku = casPresunuNaDalsiuZastavku;
        this._dalsiaZastavka = dalsiaZastavka;
    }

    public ZastavkaKonfiguracia getZastavka() {
        return _zastavka;
    }

    public double getCasPresunuNaDalsiuZastavku() {
        return _casPresunuNaDalsiuZastavku;
    }

    public ZastavkaKonfiguracia getDalsiaZastavka() {
        return _dalsiaZastavka;
    }

    public double getVzdialenostKStadionu() {
        return _vzdialenostKStadionu;
    }

    public void setVzdialenostKStadionu(double vzdialenostKStadionu) {
        this._vzdialenostKStadionu = vzdialenostKStadionu;
    }

    @Override
    public String toString() {
        return "ZastavkaLinky{" +
                "_zastavka=" + _zastavka +
                ", _casPresunuNaDalsiuZastavku=" + _casPresunuNaDalsiuZastavku +
                ", _dalsiaZastavka=" + _dalsiaZastavka +
                ", _vzdialenostKStadionu=" + _vzdialenostKStadionu +
                '}';
    }
}
