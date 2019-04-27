package Model.Info;

import Statistiky.StatistikaInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BehSimulacieInfo {

    public int _cisloReplikacie;
    public double _priemernyCasCakaniaNaZastavke;
    public double _percentoCestujuciochPrichadzajucichPoZaciatku;
    public ObservableList<StatistikaInfo> statistiky_ = FXCollections.observableArrayList();
    public ObservableList<StatistikaInfo> statistikyZastavky_ = FXCollections.observableArrayList();
    public ObservableList<StatistikaInfo> statistikyVozidla_ = FXCollections.observableArrayList();

}
