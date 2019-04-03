package Simulacia.Model.Restauracia.Info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class CasnikInfo {

    private long idCasnika_ = -1;
    private ObservableList<String> info_ = FXCollections.observableArrayList();

    public CasnikInfo() {
    }

    public long getIdCasnika() {
        return idCasnika_;
    }

    public CasnikInfo setIdCasnika(long idCasnika) {
        this.idCasnika_ = idCasnika;
        return this;
    }

    public ObservableList<String> getInfo() {
        return info_;
    }

}
