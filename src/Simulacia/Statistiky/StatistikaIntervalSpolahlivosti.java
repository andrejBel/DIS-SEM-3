package Simulacia.Statistiky;

public class StatistikaIntervalSpolahlivosti implements IStatistika {

    public static class VysledokIntervaluSpolahlivosti {

        public VysledokIntervaluSpolahlivosti(double lavostrannyInterval, double priemer, double pravostrannyInterval) {
            this.lavostrannyInterval_ = lavostrannyInterval;
            this.priemer_ = priemer;
            this.pravostrannyInterval_ = pravostrannyInterval;
        }

        public double lavostrannyInterval_;
        public double priemer_;
        public double pravostrannyInterval_;

    }

    private double suma_;
    private double sumaNaDruhu_;
    private long pocet_;
    private final static double Z90percent = 1.645;
    private String nazovStatistiky_;

    public StatistikaIntervalSpolahlivosti(String nazovStatistiky) {
        this.nazovStatistiky_ = nazovStatistiky;
        clear();
    }

    public void pridaj(double hodnota) {
        suma_ += hodnota;
        sumaNaDruhu_ += (hodnota * hodnota);
        pocet_++;
    }

    private double vypocitajSmerodajnuOdchylku() {
        if (pocet_ == 0) {
            return 0.0;
        }
        double priemer = (suma_ / (double) pocet_);
        double rozptyl = (sumaNaDruhu_ / (double) pocet_) - (priemer * priemer);
        return Math.pow(rozptyl, 0.5);
    }

    public VysledokIntervaluSpolahlivosti pocitajIntervalSpolahlivosti() {
        if (pocet_ == 0) {
            return new VysledokIntervaluSpolahlivosti(0,0,0);
        }

        double priemer = (suma_ / (double) pocet_);
        double smerodajnaOdchylka = vypocitajSmerodajnuOdchylku();
        double posunIntervalu = (Z90percent * smerodajnaOdchylka) / Math.sqrt((double) pocet_);
        return new VysledokIntervaluSpolahlivosti(priemer - posunIntervalu, priemer, priemer + posunIntervalu);
    }

    public double getPriemer() {
        return pocet_ > 0 ? suma_ / ((double) pocet_) : 0.0;
    }

    public String getIntervalSpolahlivosti() {
        VysledokIntervaluSpolahlivosti intervaluSPolahlivosti = pocitajIntervalSpolahlivosti();
        return String.format("(%.6f, %.6f ± %.6f, %.6f)", intervaluSPolahlivosti.lavostrannyInterval_, intervaluSPolahlivosti.priemer_, intervaluSPolahlivosti.pravostrannyInterval_ - intervaluSPolahlivosti.priemer_ ,intervaluSPolahlivosti.pravostrannyInterval_);
    }

    public String getNazovStatistiky() {
        return nazovStatistiky_;
    }

    @Override
    public void clear() {
        suma_ = 0.0;
        sumaNaDruhu_ = 0.0;
        pocet_ = 0;
    }

    @Override
    public String toString() {
        VysledokIntervaluSpolahlivosti intervaluSPolahlivosti = pocitajIntervalSpolahlivosti();
        return this.nazovStatistiky_ + ": " + String.format("(%.6f, %.6f ± %.6f, %.6f)", intervaluSPolahlivosti.lavostrannyInterval_, intervaluSPolahlivosti.priemer_, intervaluSPolahlivosti.pravostrannyInterval_ - intervaluSPolahlivosti.priemer_ ,intervaluSPolahlivosti.pravostrannyInterval_);
    }
}
