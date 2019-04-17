package Model.Enumeracie;

public enum TYP_LINKY {

    LINKA_A("Linka A"),
    LINKA_B("Linka B"),
    LINKA_C("Linka C"),
    ;

    private String nazovLinky_;

    TYP_LINKY(String nazovLinky_) {
        this.nazovLinky_ = nazovLinky_;
    }
}
