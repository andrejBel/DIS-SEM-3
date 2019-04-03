package Simulacia.Model.Restauracia.Info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PoBehuReplikacieInfo {

    public long cisloReplikacie_ = 0;
    public long celkovyPocetReplikacii_ = 0;
    public double casKoncaReplikacie_ = 0.0;
    public double priemernyCasCakaniaZakaznikov_ = 0.0;
    public int pocetCasnikov_ = 0;
    public int pocetKucharov_ = 0;

    public ObservableList<StatistikaInfo> statistiky_ = FXCollections.observableArrayList();

    public PoBehuReplikacieInfo(long cisloReplikacie, long pocetReplikacii, double casKoncaReplikacie, double priemernyCasCakaniaZakaznikov, int pocetCasnikov, int pocetKucharov) {
        this.cisloReplikacie_ = cisloReplikacie;
        this.celkovyPocetReplikacii_ = pocetReplikacii;
        this.casKoncaReplikacie_ = casKoncaReplikacie;
        this.priemernyCasCakaniaZakaznikov_ = priemernyCasCakaniaZakaznikov;
        this.pocetCasnikov_ = pocetCasnikov;
        this.pocetKucharov_ = pocetKucharov;
    }


}
