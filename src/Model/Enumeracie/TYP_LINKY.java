package Model.Enumeracie;

import java.util.Comparator;

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

    public String getSkratka() {
        return String.valueOf(_nazovLinky.charAt(_nazovLinky.length() - 1));
    }

    public static Comparator<TYP_LINKY> GetComparator() {
        return Comparator.naturalOrder();
    }

}
