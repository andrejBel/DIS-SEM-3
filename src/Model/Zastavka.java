package Model;

import OSPDataStruct.SimQueue;
import Statistiky.WStatNamed;
import Stanok.SimulaciaStanok;

import java.util.Objects;

public class Zastavka {

    private String _nazovZastavky;
    private double _parameterExpPrichodyZakaznikov;
    private SimQueue<Zakaznik> _zakazniciNaZastavke;

    public Zastavka(String nazovZastavky, double parameterExpPrichodyZakaznikov, SimulaciaStanok simulacia) {
        this._nazovZastavky = nazovZastavky;
        this._parameterExpPrichodyZakaznikov = parameterExpPrichodyZakaznikov;
        this._zakazniciNaZastavke = new SimQueue<>(new WStatNamed(simulacia, "Priemerný počet zákazníkov na zastávke " + nazovZastavky));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zastavka zastavka = (Zastavka) o;
        return Objects.equals(_nazovZastavky, zastavka._nazovZastavky);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_nazovZastavky);
    }

    public String getNazovZastavky() {
        return _nazovZastavky;
    }

    public double getParameterExpPrichodyZakaznikov() {
        return _parameterExpPrichodyZakaznikov;
    }
}
