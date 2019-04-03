package Simulacia.Model.Restauracia.Enumeracie;

public enum TypCinnosti {

    PRIJATIE_OBJEDNAVKY_A_USADENIE("Prijatie objednavky a usadenie"),
    PRINESENIE_JEDAL("Prinesenie jedal zakaznikom"),
    PREVZATIE_PLATBY("Prevzatie platby");

    private String popisCinnosti_;

    TypCinnosti(String popisCinnosti_) {
        this.popisCinnosti_ = popisCinnosti_;
    }

    public String getPopisCinnosti() {
        return popisCinnosti_;
    }
}
