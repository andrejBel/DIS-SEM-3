package Model;

import GUI.TableColumnItem;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.CestujuciInfo;
import Utils.Helper;

import java.util.Arrays;
import java.util.List;

public class VozidloKonfiguracia {

    private final TYP_VOZIDLA _typVozidla;
    private final TYP_LINKY _typLinky;
    private double _casPrijazduNaPrvuZastavku;

    public VozidloKonfiguracia(TYP_VOZIDLA typVozidla, TYP_LINKY typLinky, double casPrijazduNaPrvuZastavku) {
        this._typVozidla = typVozidla;
        this._typLinky = typLinky;
        this._casPrijazduNaPrvuZastavku = casPrijazduNaPrvuZastavku;
    }

    public static List<TableColumnItem<VozidloKonfiguracia>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<VozidloKonfiguracia>(vozidloKonfiguracia -> Helper.FormatujSimulacnyCas(vozidloKonfiguracia._casPrijazduNaPrvuZastavku), "Čas príjazdu k prvej zastávke", 150, TableColumnItem.IGNORE_MAX_WIDTH),
            new TableColumnItem<VozidloKonfiguracia>(vozidloKonfiguracia -> vozidloKonfiguracia._typVozidla.getNazov(), "Typ vozidla"),
            new TableColumnItem<VozidloKonfiguracia>(vozidloKonfiguracia -> vozidloKonfiguracia._typLinky.getNazovLinky(), "Linka")

    );

    public TYP_VOZIDLA getTypVozidla() {
        return _typVozidla;
    }

    public TYP_LINKY getTypLinky() {
        return _typLinky;
    }

    public double getCasPrijazduNaPrvuZastavku() {
        return _casPrijazduNaPrvuZastavku;
    }

    public void setCasPrijazduNaPrvuZastavku(double casPrijazduNaPrvuZastavku) {
        this._casPrijazduNaPrvuZastavku = casPrijazduNaPrvuZastavku;
    }
}
