package Simulacia.Model.Restauracia.Info;

import GUI.TableColumnItem;

import java.util.Arrays;
import java.util.List;

public class StatistikaInfo {


    private String nazovStatistiky_;
    private String hodnotaStatistiky_;


    public static List<TableColumnItem<StatistikaInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<StatistikaInfo>(statistikaInfo -> statistikaInfo.nazovStatistiky_, "Názov štatistiky"),
            new TableColumnItem<StatistikaInfo>(statistikaInfo -> statistikaInfo.hodnotaStatistiky_, "Hodnota štatistiky")
    );

    public StatistikaInfo(String nazovStatistiky, String hodnotaStatistiky) {
        this.nazovStatistiky_ = nazovStatistiky;
        this.hodnotaStatistiky_ = hodnotaStatistiky;
    }

    public String getNazovStatistiky() {
        return nazovStatistiky_;
    }

    public String getHodnotaStatistiky() {
        return hodnotaStatistiky_;
    }



}
