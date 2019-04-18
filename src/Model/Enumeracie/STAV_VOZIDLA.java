package Model.Enumeracie;

public enum STAV_VOZIDLA {


    CAKA_NA_VYJAZD("Čaká na výjazd"),
    POHYB("Pohybuje sa"),
    CAKANIE_NA_ZASTAVKE("Caka na zastavke")
    ;

    private String _stav;

    STAV_VOZIDLA(String stav) {
        this._stav = stav;
    }

    public String getStav() {
        return _stav;
    }
}
