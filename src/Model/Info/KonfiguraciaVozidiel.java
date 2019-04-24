package Model.Info;

import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.VozidloKonfiguracia;
import Utils.Helper;

import java.text.DecimalFormat;
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

    public String getCsvFormat() {
        DecimalFormat decimalFormat = new DecimalFormat(".0000");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(_prevadzkaLiniek.getNazov() + "\n");
        for (int indexKonfiguracie = 0; indexKonfiguracie < _konfiguraciaVozidiel.size(); indexKonfiguracie++) {
            VozidloKonfiguracia konfiguracia = _konfiguraciaVozidiel.get(indexKonfiguracie);
            stringBuilder.append(konfiguracia.getTypVozidla().getNazov() + Helper.DEFAULT_SEPARATOR +
                    konfiguracia.getTypLinky().getNazovLinky() + Helper.DEFAULT_SEPARATOR +
                    decimalFormat.format(konfiguracia.getCasPrijazduNaPrvuZastavku()) + Helper.DEFAULT_SEPARATOR +
                    (indexKonfiguracie == _konfiguraciaVozidiel.size() - 1 ? "" : "\n") );
        }
        return stringBuilder.toString();
    }

}
