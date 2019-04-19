package Model;

public class ZastavkaLinky {

    private ZastavkaKonfiguracia _zastavka;
    private double _casPresunuNaDalsiuZastavku;
    private ZastavkaKonfiguracia _dalsiaZastavka;

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
}
