package Model.Enumeracie;

public enum TYP_VOZIDLA {

    AUTOBUS_TYP_1("autobus typ 1" ,186, 4, 545000),
    AUTOBUS_TYP_2("autobus typ 2", 107, 3, 320000),
    MINIBUS("minibus", 8, 1, 0);

    private String _nazov;
    private int _kapacita;
    private int _pocetDveri;
    private int _obstaraciaCena;

    TYP_VOZIDLA(String nazov, int kapacita, int pocetDveri, int obstaraciaCena) {
        this._nazov = nazov;
        this._kapacita = kapacita;
        this._pocetDveri = pocetDveri;
        this._obstaraciaCena = obstaraciaCena;
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

    public static TYP_VOZIDLA GetTypVozidlaNaZakladeNazvu(String nazov) {
        for (TYP_VOZIDLA typVozidla: TYP_VOZIDLA.values()) {
            if (typVozidla.getNazov().equals(nazov)) {
                return typVozidla;
            }
        }
        return null;
    }

}
