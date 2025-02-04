package Model.Info;

import Statistiky.StatistikaInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class BehReplikacieInfo {

    public double _priemernyCasCakaniaCestujucehoNaZastavke;
    public double _priemernyCasCakaniaCestujucehoNaAutobus;
    public double _priemernyCasCakaniaCestujucehoNaMinibus;



    public ObservableList<StatistikaInfo> statistiky_ = FXCollections.observableArrayList();
    public ObservableList<VozidloInfo> vozidlaInfo_ = FXCollections.observableArrayList();
    public HashMap<String, ZastavkaInfo> zastavkyInfo_ = new HashMap<>();

}

