package Model;

import Model.Enumeracie.TYP_LINKY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Linka {

    private ArrayList<ZastavkaLinky> _zastavky;
    private TYP_LINKY _typLinky;

    public Linka(List<ZastavkaLinky> zastavky, TYP_LINKY typLinky) {
        this._zastavky = new ArrayList<>(zastavky);
        this._typLinky = typLinky;
    }

    public ArrayList<ZastavkaLinky> getZastavky() {
        return _zastavky;
    }

    public TYP_LINKY getTypLinky() {
        return _typLinky;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Linka linka = (Linka) o;
        return _typLinky == linka._typLinky;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_typLinky);
    }

    @Override
    public String toString() {
        return "Linka{" +
                "_zastavky=" + _zastavky +
                ", _typLinky=" + _typLinky +
                '}';
    }
}
