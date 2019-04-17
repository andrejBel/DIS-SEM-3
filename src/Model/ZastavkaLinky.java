package Model;

public class ZastavkaLinky {

    private Zastavka _zastavka;
    private double _casPresunuNaDalsiuZastavku;
    private Zastavka _dalsiaZastavka;

    public ZastavkaLinky(Zastavka zastavka, double casPresunuNaDalsiuZastavku, Zastavka dalsiaZastavka) {
        if (zastavka == null || dalsiaZastavka == null) {
            throw new RuntimeException("Null");
        }
        this._zastavka = zastavka;
        this._casPresunuNaDalsiuZastavku = casPresunuNaDalsiuZastavku;
        this._dalsiaZastavka = dalsiaZastavka;
    }

    public Zastavka getZastavka() {
        return _zastavka;
    }

    public double getCasPresunuNaDalsiuZastavku() {
        return _casPresunuNaDalsiuZastavku;
    }

    public Zastavka getDalsiaZastavka() {
        return _dalsiaZastavka;
    }
}
