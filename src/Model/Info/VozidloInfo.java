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
    private int _pocetNastupujucich;
    private int _pocetCestujucich;
    private int _pocetVystupujucich;
    private int _celkovyPocet;
    private ObservableList<CestujuciInfo> _nastupujuci;
    private ObservableList<CestujuciInfo> _cestujuci;
    private ObservableList<CestujuciInfo> _vystupujuci;

    public static List<TableColumnItem<VozidloInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._idVozidla), "ID", 40, 60),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._typVozidla, "Typ vozidla", 100, 160),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._kolkoSekundJazdi), "Doba jazdy", 80, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._stavVozidla, "Stav vozidla", 100, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._infoOStave, "Porobnosti o stave", 300, IGNORE_MAX_WIDTH),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetNastupujucich), "Počet nastupujúcich", 100, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetCestujucich), "Počet cestujúcich", 100, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetVystupujucich), "Počet vystupujúcich", 100, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._celkovyPocet), "Celkový počet", 100, 120)
    );

    public VozidloInfo(long idVozidla,
                       String typVozidla,
                       double kolkoSekundJazdi,
                       String stavVozidla,
                       String infoOStave,
                       int pocetNastupujucich,
                       int pocetCestujucich,
                       int pocetVystupujucich,
                       ArrayList<CestujuciInfo> nastupujuci,
                       ArrayList<CestujuciInfo> cestujuci,
                       ArrayList<CestujuciInfo> vystupujuci) {
        this._idVozidla = idVozidla;
        this._typVozidla = typVozidla;
        this._kolkoSekundJazdi = kolkoSekundJazdi;
        this._stavVozidla = stavVozidla;
        this._infoOStave = infoOStave;
        this._pocetNastupujucich = pocetNastupujucich;
        this._pocetCestujucich = pocetCestujucich;
        this._pocetVystupujucich = pocetVystupujucich;
        this._celkovyPocet = pocetNastupujucich + pocetCestujucich + pocetVystupujucich;
        this._nastupujuci = FXCollections.observableArrayList(nastupujuci);
        this._cestujuci = FXCollections.observableArrayList(cestujuci);
        this._vystupujuci = FXCollections.observableArrayList(vystupujuci);
    }

    public long getIdVozidla() {
        return _idVozidla;
    }
}
