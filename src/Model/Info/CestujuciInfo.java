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
    private double _casZaciatkuNastupovania;
    private double _casKoncaNastupovania;
    private double _casZaciatkuVystupovania;
    private double _casKoncaVystupovania; // cas prichoduNaStadion
    private String _vozidlo; // _vozidlo, v ktorom sa vezie, viezol
    private Integer _dvereKtorymiNastupuje;
    private Integer _dvereKtorymiVystupuje;
    private String _stavCestujuci;

    public static List<TableColumnItem<CestujuciInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> String.valueOf(vozidloInfo._id), "ID", 50, 100),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> vozidloInfo._zastavkaNaKtoruPrisiel, "Zastávka", 50, 100),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._casPrichoduNaZastavku), "Čas príchodu", 70, 120),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._casZaciatkuNastupovania), "Čas začiatku nastupovania", 70, 120),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._casKoncaNastupovania), "Čas konca nastupovania", 70, 120),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._casZaciatkuVystupovania), "Čas začiatku vystupovania", 70, 120),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._casKoncaVystupovania), "Čas konca vystupovania", 70, 120),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> vozidloInfo._vozidlo, "Vozidlo", 70, 120),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> vozidloInfo._dvereKtorymiNastupuje == null ? "-" : String.valueOf(vozidloInfo._dvereKtorymiNastupuje), "Nástupné dvere"),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> vozidloInfo._dvereKtorymiVystupuje == null ? "-" : String.valueOf(vozidloInfo._dvereKtorymiVystupuje), "Výstupné  dvere"),
            new TableColumnItem<CestujuciInfo>(vozidloInfo -> vozidloInfo._stavCestujuci, "Stav cestujúceho")
    );

    public CestujuciInfo(long id,
                         String zastavkaNaKtoruPrisiel,
                         double casPrichoduNaZastavku,
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
        this._casZaciatkuNastupovania = casZaciatkuNastupovania;
        this._casKoncaNastupovania = casKoncaNastupovania;
        this._casZaciatkuVystupovania = casZaciatkuVystupovania;
        this._casKoncaVystupovania = casKoncaVystupovania;
        this._vozidlo = vozidlo;
        this._dvereKtorymiNastupuje = dvereKtorymiNastupuje;
        this._dvereKtorymiVystupuje = dvereKtorymiVystupuje;
        this._stavCestujuci = stavCestujuci;
    }
}
