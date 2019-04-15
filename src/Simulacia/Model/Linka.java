package Simulacia.Model;

import Simulacia.Model.Enumeracie.TYP_LINKY;

import java.util.ArrayList;
import java.util.List;

public class Linka {

    private ArrayList<ZastavkaLinky> _zastavky;
    private TYP_LINKY _typLinky;

    public Linka(List<ZastavkaLinky> zastavky, TYP_LINKY typLinky) {
        this._zastavky = new ArrayList<>(zastavky);
        this._typLinky = typLinky;
    }
}
