package Simulacia.Model.Restauracia.Info;

public class StatistikaInfo {

    private String nazovStatistiky_;
    private String hodnotaStatistiky_;

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
