package Simulacia.Model.Restauracia.Enumeracie;

public enum StolStav {


    CAKAJUCI_NA_OBJEDNAVKU("Čakanie na objednávku"),
    OBJEDNAVANIE("Objednávanie"),
    CAKAJUCI_NA_PRIPRAVU_JEDLA("Čakanie na prípravu jedla"),
    CAKANIE_NA_DONESENIE_JEDLA("Čakanie na donesenie jedla"),
    CASNIK_DONASA_JEDLO("Čašník nesie jedlo z kuchyne"),
    JEDENIE("Jedenie"),
    CAKAJUCI_NA_ZAPLATENIE("Čakanie na zaplatenie"),
    PLATENIE("Platenie"),
    VOLNY("Voľný")
    ;

    private String nazov_;

    StolStav(String nazov) {
        this.nazov_ = nazov;
    }

    public String getNazov() {
        return nazov_;
    }
}
