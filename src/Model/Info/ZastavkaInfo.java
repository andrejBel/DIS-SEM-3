package Model.Info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ZastavkaInfo {
    private ObservableList<CestujuciInfo> cestujuciInfo_;
    private ObservableList<VozidloInfo> vozidloInfo_;

    public ZastavkaInfo(ObservableList<CestujuciInfo> cestujuciInfo, ObservableList<VozidloInfo> vozidloInfo) {
        this.cestujuciInfo_ = cestujuciInfo;
        this.vozidloInfo_ = vozidloInfo;
    }

    public ObservableList<CestujuciInfo> getCestujuciInfo() {
        return cestujuciInfo_;
    }

    public ObservableList<VozidloInfo> getVozidloInfo() {
        return vozidloInfo_;
    }


}
