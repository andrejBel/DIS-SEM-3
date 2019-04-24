package Model.Enumeracie;

import java.util.Comparator;

public enum TYP_VOZIDLA {

    AUTOBUS_TYP_1("Autobus typ 1" ,186, 4, 545000, true),
    AUTOBUS_TYP_2("Autobus typ 2", 107, 3, 320000, true),
    MINIBUS("Minibus", 8, 1, 0, false);

    private String _nazov;
    private int _kapacita;
    private int _pocetDveri;
    private int _obstaraciaCena;
    private boolean _autobus;

    TYP_VOZIDLA(String nazov, int kapacita, int pocetDveri, int obstaraciaCena, boolean autobus) {
        this._nazov = nazov;
        this._kapacita = kapacita;
        this._pocetDveri = pocetDveri;
        this._obstaraciaCena = obstaraciaCena;
        this._autobus = autobus;
    }

    public String getNazov() {
        return _nazov;
    }

    public int getKapacita() {
        return _kapacita;
    }

    public int getPocetDveri() {
        return _pocetDveri;
    }

    public int getObstaraciaCena() {
        return _obstaraciaCena;
    }

    public boolean isAutobus() {
        return _autobus;
    }

    public static TYP_VOZIDLA GetTypVozidlaNaZakladeNazvu(String nazov) {
        for (TYP_VOZIDLA typVozidla: TYP_VOZIDLA.values()) {
            if (typVozidla.getNazov().equals(nazov)) {
                return typVozidla;
            }
        }
        return null;
    }

    public static Comparator<TYP_VOZIDLA> GetComparator() {
        return Comparator.naturalOrder();
    }

}
