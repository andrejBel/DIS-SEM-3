package Simulacia.Model.Udalost;

public enum TypUdalosti {

    POSUN_CASU("Posun casu"),
    PRICHOD_ZAKAZNIKA("Prichod zakaznika do systemu"),
    ZACIATOK_USADENIA("Zaciatok usadenia"),
    KONIEC_USADENIA("Koniec usadenia"),
    ZACIATOK_PRIPRAVY_JEDAL("Zaciatok pripravy jedal"),
    KONIEC_PRIPRAVY_JEDAL("Koniec pripravy jedal"),
    ZACIATOK_ODNESENIA_JEDAL("Zaciatok odnesenia jedal"),
    KONIEC_ODNESENIA_JEDAL("Koniec odnesenia jedal"),
    KONIEC_JEDENIA("Koniec jedenia"),
    ZACIATOK_PLATENIA("Zaciatok platenia"),
    KONIEC_PLATENIA("Koniec platenia. Odchod zakaznika zo systemu."),
    ;

    private String nazovUdalosti_;

    TypUdalosti(String nazovUdalosti) {
        this.nazovUdalosti_ = nazovUdalosti;
    }

    public String getNazovUdalosti() {
        return nazovUdalosti_;
    }
}
