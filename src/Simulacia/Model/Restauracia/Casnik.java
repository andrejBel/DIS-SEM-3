package Simulacia.Model.Restauracia;


import Simulacia.Model.Restauracia.Enumeracie.TypCinnosti;

public class Casnik {

    private final long idCasnika_;
    private Stol obsluhovanyStol_ = null;
    private boolean obsadeny_ = false;

    private double casKedyBolCasnikUstanovenyZaVolneho_ = 0.0;
    private double casKedyBolCasnikUstanovenyZaObsadeneho_ = 0.0;

    private double casVolny_ = 0.0;
    private double casObsadeny_ = 0.0;
    private TypCinnosti vykonavanaCinnost_ = null;

    public Casnik(long idCasnika_) {
        this.idCasnika_ = idCasnika_;
    }

    public long getIdCasnika() {
        return idCasnika_;
    }

    public void clear() {
        this.obsluhovanyStol_ = null;
        obsadeny_ = false;
        casKedyBolCasnikUstanovenyZaVolneho_ = 0.0;
        casKedyBolCasnikUstanovenyZaObsadeneho_ = 0.0;
        casVolny_ = 0.0;
        casObsadeny_ = 0.0;
        vykonavanaCinnost_ = null;
    }

    public Stol getObsluhovanyStol() {
        return obsluhovanyStol_;
    }

    public Casnik setObsluhovanyStol(Stol obsluhovanyStol) {
        this.obsluhovanyStol_ = obsluhovanyStol;
        return this;
    }

    public TypCinnosti getVykonavanaCinnost() {
        return vykonavanaCinnost_;
    }

    public Casnik setVykonavanaCinnost(TypCinnosti vykonavanaCinnost) {
        this.vykonavanaCinnost_ = vykonavanaCinnost;
        return this;
    }

    public double kolkoJeCasnikVolny(double cas) {
        if (obsadeny_) {
            return casVolny_;
        }
        return casVolny_ + (cas - casKedyBolCasnikUstanovenyZaVolneho_);
    }

    public double kolkoPercentCasuJeVolny(double cas) {
        return (kolkoJeCasnikVolny(cas) / cas) * 100.0;
    }

    public double kolkoJeCasnikObsadeny(double cas) {
        if (obsadeny_) {
            return casObsadeny_ + (cas - casKedyBolCasnikUstanovenyZaObsadeneho_);
        } else {
            return casObsadeny_;
        }
    }

    public void setObsadeny(double cas) {
        obsadeny_ = true;

        casKedyBolCasnikUstanovenyZaObsadeneho_ = cas;

        casVolny_ += cas - casKedyBolCasnikUstanovenyZaVolneho_;
    }

    public void setVolny(double cas) {
        obsadeny_ = false;

        casKedyBolCasnikUstanovenyZaVolneho_ = cas;

        casObsadeny_ += cas - casKedyBolCasnikUstanovenyZaObsadeneho_;
    }

    public boolean getObsadeny() {
        return obsadeny_;
    }

    @Override
    public String toString() {
        return "Casnik{" +
                "idCasnika_=" + idCasnika_ +
                ", obsluhovanyStol_=" + obsluhovanyStol_.getIdStolu() +
                ", obsadeny_=" + obsadeny_ +
                ", casKedyBolCasnikUstanovenyZaVolneho_=" + casKedyBolCasnikUstanovenyZaVolneho_ +
                ", casKedyBolCasnikUstanovenyZaObsadeneho_=" + casKedyBolCasnikUstanovenyZaObsadeneho_ +
                ", casVolny_=" + casVolny_ +
                ", casObsadeny_=" + casObsadeny_ +
                ", vykonavanaCinnost_=" + vykonavanaCinnost_.getPopisCinnosti() +
                '}';
    }
}
