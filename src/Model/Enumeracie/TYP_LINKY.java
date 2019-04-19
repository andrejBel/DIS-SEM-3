package Model.Enumeracie;

public enum TYP_LINKY {

    LINKA_A("Linka A"),
    LINKA_B("Linka B"),
    LINKA_C("Linka C"),
    ;

    private String _nazovLinky;

    TYP_LINKY(String nazovLinky) {
        this._nazovLinky = nazovLinky;
    }

    public String getNazovLinky() {
        return _nazovLinky;
    }

    public static TYP_LINKY GetTypLinkyNaZakladeNazvu(String nazov) {
        for (TYP_LINKY typLinky: TYP_LINKY.values()) {
            if (typLinky.getNazovLinky().equals(nazov)) {
                return typLinky;
            }
        }
        return null;
    }

}
