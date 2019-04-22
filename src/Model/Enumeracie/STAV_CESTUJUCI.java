package Model.Enumeracie;

public enum STAV_CESTUJUCI {

    CAKA_NA_ZASTAVKE("Čaká na zastávke"),
    NASTUPUJE_DO_VOZIDLA("Nastupuje"),
    VEZIE_SA_VO_VOZIDLE("Vezie sa"),
    VYSTUPUJE_Z_VOZIDLA("Vystupuje"),
    NA_STADIONE("Na štadióne")
    ;
    private String _nazov;

    STAV_CESTUJUCI(String nazov) {
        this._nazov = nazov;
    }

    public String getNazov() {
        return _nazov;
    }
}
