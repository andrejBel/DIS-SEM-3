package Simulacia.Model.Restauracia;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Statistiky.StatistikaVazenyPriemer;

import java.util.ArrayList;

public class Stol {

    public static class ParametreStola {

        public int maximalnyPocetZakaznikovPriStole_;
        public int kolkoStolov_;

        public ParametreStola(int maximalnyPocetZakaznikovPriStole, int kolkoStolov) {
            this.maximalnyPocetZakaznikovPriStole_ = maximalnyPocetZakaznikovPriStole;
            this.kolkoStolov_ = kolkoStolov;
        }
    }

    public static class StatistikaStolov extends StatistikaVazenyPriemer {
        public int pocetStolov_;

        public StatistikaStolov(SimulacneJadro jadro_, String nazovStatistiky, int pocetZakaznikovPriStole) {
            super(jadro_, nazovStatistiky);
            this.pocetStolov_ = pocetZakaznikovPriStole;
        }

        public int getMaximalnyPocetZakaznikovPriStole() {
            return pocetStolov_;
        }

        @Override
        public void clear() {
            super.clear(this.pocetStolov_);
        }
    }

    private final int maximalnyPocetMiest_;
    private final long idStolu_;
    private ArrayList<Zakaznik> zakazniciPriStole_ = new ArrayList<>();
    private int pocetPripravenychJedal_ = 0;
    private double casZaradeniaDoFrontuUsadenie_ = 0.0;
    private double casZaradeniaDoFrontuOdneseniaJedal_ = 0.0;
    private double casZaradeniaDoFrontuZaplatenie_ = 0.0;
    private StolStav stavStola_ = StolStav.VOLNY;


    public Stol(int maximalnyPocetMiest, long idStolu) {
        this.maximalnyPocetMiest_ = maximalnyPocetMiest;
        this.idStolu_ = idStolu;
    }

    public int getMaximalnyPocetMiest() {
        return maximalnyPocetMiest_;
    }

    public long getIdStolu() {
        return idStolu_;
    }

    public ArrayList<Zakaznik> getZakazniciPriStole() {
        return zakazniciPriStole_;
    }

    public void pridajZakaznika(Zakaznik zakaznik) {
        if (zakazniciPriStole_.size() < maximalnyPocetMiest_) {
            this.zakazniciPriStole_.add(zakaznik.setStolPridelenyZakaznikovy(this));
        } else {
            throw new RuntimeException("Pri stole je viac ako povoleny pocet zakaznikov");
        }
    }

    public void clear() {
        for (Zakaznik zakaznik: zakazniciPriStole_) {
            zakaznik.setStolPridelenyZakaznikovy(null);
        }
        zakazniciPriStole_.clear();
        pocetPripravenychJedal_ = 0;
        casZaradeniaDoFrontuUsadenie_ = 0.0;
        casZaradeniaDoFrontuOdneseniaJedal_ = 0.0;
        casZaradeniaDoFrontuZaplatenie_ = 0.0;
        stavStola_ = StolStav.VOLNY;
    }

    public void zvysPocetPripravenychJedal() {
        if (pocetPripravenychJedal_ == zakazniciPriStole_.size()) {
            throw new RuntimeException("Ani toto by sa nemalo stat");
        }
        pocetPripravenychJedal_++;
    }

    public int getPocetPripravenychJedal() {
        return pocetPripravenychJedal_;
    }

    public boolean majuVsetciZakazniciNachystaneJedlo() {
        return this.pocetPripravenychJedal_ == this.zakazniciPriStole_.size();
    }

    public double getCasZaradeniaDoFrontuUsadenie() {
        return casZaradeniaDoFrontuUsadenie_;
    }

    public void setCasZaradeniaDoFrontuUsadenie(double casZaradeniaDoFrontuUsadenie) {
        this.casZaradeniaDoFrontuUsadenie_ = casZaradeniaDoFrontuUsadenie;
    }

    public double getCasZaradeniaDoFrontuOdneseniaJedal() {
        return casZaradeniaDoFrontuOdneseniaJedal_;
    }

    public void setCasZaradeniaDoFrontuOdneseniaJedal(double casZaradeniaDoFrontuOdneseniaJedal) {
        this.casZaradeniaDoFrontuOdneseniaJedal_ = casZaradeniaDoFrontuOdneseniaJedal;
    }

    public double getCasZaradeniaDoFrontuZaplatenie() {
        return casZaradeniaDoFrontuZaplatenie_;
    }

    public void setCasZaradeniaDoFrontuZaplatenie(double casZaradeniaDoFrontuZaplatenie) {
        this.casZaradeniaDoFrontuZaplatenie_ = casZaradeniaDoFrontuZaplatenie;
    }

    public StolStav getStavStola() {
        return stavStola_;
    }

    public Stol setStavStola_(StolStav stavStola) {
        this.stavStola_ = stavStola;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "Stol{" +
                "maximalnyPocetMiest_=" + maximalnyPocetMiest_ +
                ", idStolu_=" + idStolu_ +
                ", Pocet zakaznikov pri stole: " + zakazniciPriStole_.size() +
                ", pocetPripravenychJedal_=" + pocetPripravenychJedal_ +
                '}'
        );
        return stringBuilder.toString();
    }
}
