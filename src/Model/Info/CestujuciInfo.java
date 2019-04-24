package Model.Info;

import GUI.TableColumnItem;
import Model.Vozidlo;
import Utils.Helper;

import java.util.Arrays;
import java.util.List;

import static GUI.TableColumnItem.IGNORE_MAX_WIDTH;

public class CestujuciInfo {

    private long _id;
    private String _zastavkaNaKtoruPrisiel;
    private double _casPrichoduNaZastavku;
    private double _casCakaniaNaZastavke;
    private double _casZaciatkuNastupovania;
    private double _casKoncaNastupovania;
    private double _casZaciatkuVystupovania;
    private double _casKoncaVystupovania; // cas prichoduNaStadion
    private String _vozidlo; // _vozidlo, v ktorom sa vezie, viezol
    private Integer _dvereKtorymiNastupuje;
    private Integer _dvereKtorymiVystupuje;
    private String _stavCestujuci;

    public static List<TableColumnItem<CestujuciInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> String.valueOf(cestujuciInfo._id), "ID", 45, 50),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> cestujuciInfo._zastavkaNaKtoruPrisiel, "Z", 35, 35),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> Helper.FormatujSimulacnyCas(cestujuciInfo._casPrichoduNaZastavku), "Čas príchodu", 80, 90),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> Helper.FormatujSimulacnyCas(cestujuciInfo._casCakaniaNaZastavke), "Čas čakania na zastávke", 80, 90),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> Helper.FormatujSimulacnyCas(cestujuciInfo._casZaciatkuNastupovania, false), "Začiatok nastupovania", 120, IGNORE_MAX_WIDTH),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> Helper.FormatujSimulacnyCas(cestujuciInfo._casKoncaNastupovania, false), "Koniec nastupovania", 120, IGNORE_MAX_WIDTH),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> Helper.FormatujSimulacnyCas(cestujuciInfo._casZaciatkuVystupovania, false), "Začiatok vystupovania", 120, IGNORE_MAX_WIDTH),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> Helper.FormatujSimulacnyCas(cestujuciInfo._casKoncaVystupovania, false), "Koniec vystupovania", 120, IGNORE_MAX_WIDTH),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> cestujuciInfo._vozidlo, "Vozidlo", 70, 70),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> cestujuciInfo._dvereKtorymiNastupuje == null ? "-" : String.valueOf(cestujuciInfo._dvereKtorymiNastupuje), "Nástupné dvere"),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> cestujuciInfo._dvereKtorymiVystupuje == null ? "-" : String.valueOf(cestujuciInfo._dvereKtorymiVystupuje), "Výstupné  dvere"),
            new TableColumnItem<CestujuciInfo>(cestujuciInfo -> cestujuciInfo._stavCestujuci, "Stav cestujúceho")
    );


    public CestujuciInfo(long id,
                         String zastavkaNaKtoruPrisiel,
                         double casPrichoduNaZastavku,
                         double casCakaniaNaZastavke,
                         double casZaciatkuNastupovania,
                         double casKoncaNastupovania,
                         double casZaciatkuVystupovania,
                         double casKoncaVystupovania,
                         String vozidlo,
                         Integer dvereKtorymiNastupuje,
                         Integer dvereKtorymiVystupuje,
                         String stavCestujuci
    ) {
        this._id = id;
        this._zastavkaNaKtoruPrisiel = zastavkaNaKtoruPrisiel;
        this._casPrichoduNaZastavku = casPrichoduNaZastavku;
        this._casCakaniaNaZastavke = casCakaniaNaZastavke;
        this._casZaciatkuNastupovania = casZaciatkuNastupovania;
        this._casKoncaNastupovania = casKoncaNastupovania;
        this._casZaciatkuVystupovania = casZaciatkuVystupovania;
        this._casKoncaVystupovania = casKoncaVystupovania;
        this._vozidlo = vozidlo;
        this._dvereKtorymiNastupuje = dvereKtorymiNastupuje;
        this._dvereKtorymiVystupuje = dvereKtorymiVystupuje;
        this._stavCestujuci = stavCestujuci;
    }

    public long getId() {
        return _id;
    }

    public String getZastavkaNaKtoruPrisiel() {
        return _zastavkaNaKtoruPrisiel;
    }
}
