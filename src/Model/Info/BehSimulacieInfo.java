package Model.Info;

import Statistiky.StatistikaInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BehSimulacieInfo {

    public int _cisloReplikacie;
    public double _priemernyCasCakaniaNaZastavke;
    public ObservableList<StatistikaInfo> statistiky_ = FXCollections.observableArrayList();

}
