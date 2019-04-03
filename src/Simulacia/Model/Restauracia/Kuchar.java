package Simulacia.Model.Restauracia;


public class Kuchar {

    private long indexKuchara_;
    private boolean obsadeny_ = false;
    private double casKedyBolKucharUstanovenyZaVolneho_ = 0.0;
    private double casKedyBolCasnikUstanovenyZaObsadeneho_ = 0.0;
    private double casVolny_ = 0.0;
    private double casObsadeny_ = 0.0;
    private double casZaciatkuPripravyJedla_ = 0.0;
    private double casKoncaPripravyJedla_ = 0.0;
    private Zakaznik zakaznikKtoremuJedloSaRobi_ = null;


    public Kuchar(long indexKuchara) {
        this.indexKuchara_ = indexKuchara;
    }

    public long getIndexKuchara() {
        return indexKuchara_;
    }

    public void clear() {

        obsadeny_ = false;
        casKedyBolKucharUstanovenyZaVolneho_ = 0.0;
        casKedyBolCasnikUstanovenyZaObsadeneho_ = 0.0;
        casVolny_ = 0.0;
        casObsadeny_ = 0.0;
        casZaciatkuPripravyJedla_ = 0.0;
        casKoncaPripravyJedla_ = 0.0;
        zakaznikKtoremuJedloSaRobi_ = null;
    }

    public double kolkoJeKucharVolny(double cas) {
        if (obsadeny_) {
            return casVolny_;
        }
        return casVolny_ + (cas - casKedyBolKucharUstanovenyZaVolneho_);
    }

    public double kolkoPercentCasuJeVolny(double cas) {
        return (kolkoJeKucharVolny(cas) / cas) * 100.0;
    }

    public double kolkoJeKucharObsadeny(double cas) {
        if (obsadeny_) {
            return casObsadeny_ + (cas - casKedyBolCasnikUstanovenyZaObsadeneho_);
        } else {
            return casObsadeny_;
        }
    }

    public void setObsadeny(double cas) {
        obsadeny_ = true;

        casKedyBolCasnikUstanovenyZaObsadeneho_ = cas;

        casVolny_ += cas - casKedyBolKucharUstanovenyZaVolneho_;
    }

    public void setVolny(double cas) {
        obsadeny_ = false;

        casKedyBolKucharUstanovenyZaVolneho_ = cas;

        casObsadeny_ += cas - casKedyBolCasnikUstanovenyZaObsadeneho_;
    }

    public void setZakaznikKtoremuJedloSaRobi(Zakaznik zakaznikKtoremuJedloSaRobi, double cas) {
        this.zakaznikKtoremuJedloSaRobi_ = zakaznikKtoremuJedloSaRobi;
        if (zakaznikKtoremuJedloSaRobi == null) {
            casZaciatkuPripravyJedla_ = 0.0;
            casKoncaPripravyJedla_ = 0.0;
        } else {
            casZaciatkuPripravyJedla_ = cas;
            casKoncaPripravyJedla_ = cas + zakaznikKtoremuJedloSaRobi_.getDlzkaVyhotoveniaJedla();
        }
    }

    public double getCasZaciatkuPripravyJedla() {
        return casZaciatkuPripravyJedla_;
    }

    public double getCasKoncaPripravyJedla() {
        return casKoncaPripravyJedla_;
    }

    public double kolkoPercentPripravovanehoJedlaJeHotoveho(double cas) {
        double percenta = ( (cas - casZaciatkuPripravyJedla_) / (casKoncaPripravyJedla_ - casZaciatkuPripravyJedla_) ) * 100;
        return  percenta;
    }

    public Zakaznik getZakaznikKtoremuJedloSaRobi() {
        return zakaznikKtoremuJedloSaRobi_;
    }

    public boolean getObsadeny() {
        return obsadeny_;
    }
}
