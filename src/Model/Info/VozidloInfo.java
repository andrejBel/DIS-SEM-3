package Model.Info;

import GUI.TableColumnItem;
import Utils.Helper;


import java.util.Arrays;
import java.util.List;

import static GUI.TableColumnItem.IGNORE_MAX_WIDTH;

public class VozidloInfo {

    private long _idVozidla;
    private String _typVozidla;
    private double _kolkoSekundJazdi;
    private String _stavVozidla;
    private String _infoOStave;
    private int _pocetCestujucich;

    public static List<TableColumnItem<VozidloInfo>> ATRIBUTY = Arrays.asList(
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._idVozidla), "ID", 40, 60),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._typVozidla, "Typ vozidla", 100, 160),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> Helper.FormatujSimulacnyCas(vozidloInfo._kolkoSekundJazdi), "Doba jazdy", 80, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._stavVozidla, "Stav vozidla", 100, 120),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> vozidloInfo._infoOStave, "Porobnosti o stave", 300, IGNORE_MAX_WIDTH),
            new TableColumnItem<VozidloInfo>(vozidloInfo -> String.valueOf(vozidloInfo._pocetCestujucich), "Počet cestujúcich", 100, 120)
    );

    public VozidloInfo(long idVozidla, String typVozidla, double kolkoSekundJazdi, String stavVozidla, String infoOStave, int pocetCestujucich) {
        this._idVozidla = idVozidla;
        this._typVozidla = typVozidla;
        this._kolkoSekundJazdi = kolkoSekundJazdi;
        this._stavVozidla = stavVozidla;
        this._infoOStave = infoOStave;
        this._pocetCestujucich = pocetCestujucich;
    }

    public long getIdVozidla() {
        return _idVozidla;
    }
}
