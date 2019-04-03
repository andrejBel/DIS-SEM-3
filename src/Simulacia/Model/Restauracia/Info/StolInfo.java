package Simulacia.Model.Restauracia.Info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StolInfo {

    private long idStola_ = -1;
    private ObservableList<String> info_ = FXCollections.observableArrayList();


    public long getIdStola() {
        return idStola_;
    }

    public StolInfo setIdStola(long idStola) {
        this.idStola_ = idStola;
        return this;
    }

    public ObservableList<String> getInfo() {
        return info_;
    }

}
