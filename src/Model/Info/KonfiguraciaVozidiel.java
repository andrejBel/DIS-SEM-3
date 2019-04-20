package Model.Info;

import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.VozidloKonfiguracia;

import java.util.ArrayList;

public class KonfiguraciaVozidiel {

    private PREVADZKA_LINIEK _prevadzkaLiniek;
    private ArrayList<VozidloKonfiguracia> _konfiguraciaVozidiel;

    public KonfiguraciaVozidiel(PREVADZKA_LINIEK prevadzkaLiniek, ArrayList<VozidloKonfiguracia> konfiguraciaVozidil) {
        this._prevadzkaLiniek = prevadzkaLiniek;
        this._konfiguraciaVozidiel = konfiguraciaVozidil;
    }

    public KonfiguraciaVozidiel() {
        _prevadzkaLiniek = null;
        _konfiguraciaVozidiel = new ArrayList<>();
    }

    public PREVADZKA_LINIEK getPrevadzkaLiniek() {
        return _prevadzkaLiniek;
    }

    public void setPrevadzkaLiniek(PREVADZKA_LINIEK prevadzkaLiniek) {
        this._prevadzkaLiniek = prevadzkaLiniek;
    }

    public ArrayList<VozidloKonfiguracia> getKonfiguraciaVozidiel() {
        return _konfiguraciaVozidiel;
    }

    public void setKonfiguraciaVozidiel(ArrayList<VozidloKonfiguracia> konfiguraciaVozidil) {
        this._konfiguraciaVozidiel = konfiguraciaVozidil;
    }
}
