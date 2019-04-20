package Model.Enumeracie;

public enum PREVADZKA_LINIEK {

    PO_NASTUPENI_ODCHADZA("Po nastúpení odchádza"),
    PO_NASTUPENI_CAKA("Po nastúpení čaká")
    ;

    private String _nazov;

    PREVADZKA_LINIEK(String nazov) {
        this._nazov = nazov;
    }

    public String getNazov() {
        return _nazov;
    }

    public static PREVADZKA_LINIEK GetPrevadzkuLiniekPodlaNazvu(String nazov) {
        for (PREVADZKA_LINIEK prevadzkaLiniek: PREVADZKA_LINIEK.values()) {
            if (prevadzkaLiniek.getNazov().equals(nazov)) {
                return prevadzkaLiniek;
            }
        }
        return null;
    }

}
