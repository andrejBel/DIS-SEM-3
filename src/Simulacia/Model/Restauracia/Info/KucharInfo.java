package Simulacia.Model.Restauracia.Info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KucharInfo {

    private long idKuchara_ = -1;
    private ObservableList<String> info_ = FXCollections.observableArrayList();

    public KucharInfo() {
    }

    public long getIdKuchara() {
        return idKuchara_;
    }

    public KucharInfo setIdKuchara(long idKuchara) {
        this.idKuchara_ = idKuchara;
        return this;
    }

    public ObservableList<String> getInfo() {
        return info_;
    }

}
