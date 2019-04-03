package Simulacia.Model.Restauracia.Enumeracie;

public enum Pokrm {

    CAESAR_SALAT(0.3, "Caesar salat"),
    PENNE_SALAT(0.35, "Penne salat"),
    CELOZRNNE_SPAGETY(0.2, "Celozrnne spagety"),
    SYTY_SALAT(0.15, "Syty salat");


    Pokrm(double pObjednania, String popis) {
        this.pObjednania_ = pObjednania;
        this.popis_ = popis;
    }

    private double pObjednania_;
    private String popis_;

    public double getPObjednania() {
        return pObjednania_;
    }

    public String getPopis() {
        return popis_;
    }

}
