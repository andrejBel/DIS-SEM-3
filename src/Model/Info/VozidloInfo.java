package Model.Info;

import GUI.TableColumnItem;
import Utils.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static GUI.TableColumnItem.IGNORE_MAX_WIDTH;

public class VozidloInfo {

    private long _idVozidla;
    private String _typVozidla;
    private double _kolkoSekundJazdi;
    private String _stavVozidla;
    private String _infoOStave;
    private double _casVstupuDoFrontuVozidielNaZastavke;
    private ObservableList<CestujuciInfo> _nastupujuci;
    private ObservableList<CestujuciInfo> _cestujuci;
    private ObservableList<CestujuciInfo> _vystupujuci;
    private int _pocetNastupujucich;
    private int _pocetCestujucich;
    private int _pocetVystupujucich;
    private int _celkovyPocet;
    private double _trzba;

    public static List<TableColumnItem<VozidloInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._idVozidla), "ID", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._typVozidla, "Typ vozidla", 95, 100),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._kolkoSekundJazdi), "Doba jazdy", 80, 80),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._stavVozidla, "Stav vozidla", 100, 100),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._infoOStave, "Podrobnosti o stave", 240, IGNORE_MAX_WIDTH),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetNastupujucich), "P. N", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetCestujucich), "P. C", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetVystupujucich), "P. V", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._celkovyPocet), "C. P.", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> Helper.FormatujDouble(vozidloInfo._trzba, 0), "Tržba", 30, 40)
    );
    public static List<TableColumnItem<VozidloInfo>> ATRIBUTY_FRONT_VOZIDIEL = Arrays.asList(
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._idVozidla), "ID", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._typVozidla, "Typ vozidla", 95, 100),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._kolkoSekundJazdi), "Doba jazdy", 80, 80),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._stavVozidla, "Stav vozidla", 100, 100),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._infoOStave, "Podrobnosti o stave", 240, IGNORE_MAX_WIDTH),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetNastupujucich), "P. N", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetCestujucich), "P. C", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetVystupujucich), "P. V", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._celkovyPocet), "C. P.", 30, 40),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._casVstupuDoFrontuVozidielNaZastavke), "Kedy začal čakať na Z.", 80, IGNORE_MAX_WIDTH)
    );


    public VozidloInfo(long idVozidla,
                       String typVozidla,
                       double kolkoSekundJazdi,
                       String stavVozidla,
                       String infoOStave,
                       double casVstupuDoFrontuVozidielNaZastavke,
                       double trzba,
                       int pocetNastupujucich,
                       int pocetCestujucich,
                       int pocetVystupujucich,
                       ObservableList<CestujuciInfo> nastupujuci,
                       ObservableList<CestujuciInfo> cestujuci,
                       ObservableList<CestujuciInfo> vystupujuci) {
        this._idVozidla = idVozidla;
        this._typVozidla = typVozidla;
        this._kolkoSekundJazdi = kolkoSekundJazdi;
        this._stavVozidla = stavVozidla;
        this._infoOStave = infoOStave;
        this._casVstupuDoFrontuVozidielNaZastavke = casVstupuDoFrontuVozidielNaZastavke;
        this._trzba = trzba;
        this._pocetNastupujucich = pocetNastupujucich;
        this._pocetCestujucich = pocetCestujucich;
        this._pocetVystupujucich = pocetVystupujucich;
        this._nastupujuci = nastupujuci;
        this._cestujuci = cestujuci;
        this._vystupujuci = vystupujuci;

        this._celkovyPocet = _pocetNastupujucich + _pocetCestujucich + _pocetVystupujucich;
    }

    public ObservableList<CestujuciInfo> getNastupujuci() {
        return _nastupujuci;
    }

    public ObservableList<CestujuciInfo> getCestujuci() {
        return _cestujuci;
    }

    public ObservableList<CestujuciInfo> getVystupujuci() {
        return _vystupujuci;
    }

    public long getIdVozidla() {
        return _idVozidla;
    }


}
