package GUI.Controller;

import GUI.CAS_UPDATU;
import GUI.IObserver;
import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Info.*;
import Simulacia.Model.Restauracia.StavObjektov;
import Simulacia.SimulaciaRestauracia;
import Simulacia.SimulaciaWrapper;
import Utils.Helper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;

public class CStavObjektov extends CWindowBase implements IObserver {

    @FXML
    private JFXButton buttonPokracuj;

    @FXML
    private JFXComboBox<CasnikInfo> comboBoxCasnici;

    @FXML
    private TableView<String> tableViewCasnici;

    @FXML
    private TableColumn<String, String> tableColumCasniciInfo;

    @FXML
    private JFXComboBox<KucharInfo> comboBoxKuchari;

    @FXML
    private TableView<String> tableViewKuchari;

    @FXML
    private TableColumn<String, String> tableColumKuchariInfo;

    @FXML
    private JFXComboBox<StolInfo> comboBoxStoly;

    @FXML
    private TableView<String> tableViewStoly;

    @FXML
    private TableColumn<String, String> tableColumStolyInfo;

    @FXML
    private TableView<UdalostInfo> tableViewKalendarUdalosti;

    @FXML
    private TableColumn<UdalostInfo, String> tableColumnCasUdalosti;

    @FXML
    private TableColumn<UdalostInfo, String> tableColumnNazovUdalosti;

    @FXML
    private TableColumn<UdalostInfo, String> tableColumnDodatocneInfo;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistiky;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnNazovStatistiky;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnHodnotaStatistiky;
    
    @FXML
    private TableView<String> tableViewFrontNaObsluhu;

    @FXML
    private TableColumn<String, String> tableColumFrontNaObsluhu;

    @FXML
    private TableView<String> tableViewFrontJedal;

    @FXML
    private TableColumn<String, String> tableColumFrontJedal;

    @FXML
    private TableView<String> tableViewFrontOdnesenieJedla;

    @FXML
    private TableColumn<String, String> tableColumFrontOdnesenieJedla;

    @FXML
    private TableView<String> tableViewFrontZaplatenie;

    @FXML
    private TableColumn<String, String> tableColumFrontZaplatenie;


    public CStavObjektov(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);
        setModality(Modality.APPLICATION_MODAL);
        simulaciaWrapper.getSimulacia().registrujObservera(this);

        comboBoxCasnici.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CasnikInfo casnikInfo = newValue;
            if (casnikInfo != null) {
                tableViewCasnici.setItems(casnikInfo.getInfo());
            }
        });

        comboBoxCasnici.setConverter(new StringConverter<CasnikInfo>() {
            @Override
            public String toString(CasnikInfo casnikInfo) {
                if (casnikInfo == null) {
                    return "";
                }
                return casnikInfo.getIdCasnika() + "";
            }

            @Override
            public CasnikInfo fromString(String string) {
                return null;
            }
        });

        comboBoxKuchari.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            KucharInfo kucharInfo = newValue;
            if (kucharInfo != null) {
                tableViewKuchari.setItems(kucharInfo.getInfo());
            }
        });

        comboBoxKuchari.setConverter(new StringConverter<KucharInfo>() {
            @Override
            public String toString(KucharInfo kucharInfo) {
                if (kucharInfo != null) {
                    return kucharInfo.getIdKuchara() + "";
                }
                return "";
            }

            @Override
            public KucharInfo fromString(String string) {
                return null;
            }
        });

        comboBoxStoly.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            StolInfo stol = newValue;
            if (stol != null) {
                tableViewStoly.setItems(stol.getInfo());
            }
        });

        comboBoxStoly.setConverter(new StringConverter<StolInfo>() {
            @Override
            public String toString(StolInfo stol) {
                if (stol != null) {
                    return stol.getIdStola() + "";
                }
                return  "";
            }

            @Override
            public StolInfo fromString(String string) {
                return null;
            }
        });

        tableColumCasniciInfo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        tableColumKuchariInfo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        tableColumStolyInfo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        tableColumFrontNaObsluhu.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        tableColumFrontJedal.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        tableColumFrontOdnesenieJedla.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        tableColumFrontZaplatenie.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));

        
        
        tableColumnCasUdalosti.setCellValueFactory(param -> {
            return new SimpleStringProperty(Helper.FormatujSimulacnyCas(param.getValue().casUdalosti_ + SimulaciaRestauracia.POSUN_CASU_NA_OTVORENIE_RESTAURACIE));
        });
        tableColumnNazovUdalosti.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().nazovUdalosti_));
        tableColumnDodatocneInfo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().info_));

        tableColumnNazovStatistiky.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNazovStatistiky()));
        tableColumnHodnotaStatistiky.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHodnotaStatistiky()));
        
        buttonPokracuj.setOnAction(event -> {
            //nacitajStavObjektov(simulaciaWrapper.getSimulacia(), true);
            simulaciaWrapper.resume();
        });


    }


    @Override
    protected String getViewFileName() {
        return "stavObjektov.fxml";
    }

    @Override
    public String getViewName() {
        return "Stav objektov";
    }

    @Override
    public void update(SimulacneJadro simulacneJadro, CAS_UPDATU casUpdatu) {
        SimulaciaRestauracia simulacia = (SimulaciaRestauracia) simulacneJadro;

        if (isActive()) {
            if (casUpdatu == CAS_UPDATU.POCAS_SIMULACIE_REPLIKACIE) {
                nacitajStavObjektov(simulacia, true);
            }

        }
        if (casUpdatu == CAS_UPDATU.PO_SIMULACII) {
            tableViewKalendarUdalosti.getItems().clear();
            tableViewStatistiky.getItems().clear();
            tableViewCasnici.getItems().clear();
            tableViewKuchari.getItems().clear();
            tableViewStoly.getItems().clear();

            tableViewFrontNaObsluhu.getItems().clear();
            tableViewFrontJedal.getItems().clear();
            tableViewFrontOdnesenieJedla.getItems().clear();
            tableViewFrontZaplatenie.getItems().clear();
        }

    }


    public void nacitajStavObjektov(SimulaciaRestauracia simulacia, boolean pamatajPoslednyStav) {
        Platform.runLater(() -> {
            StavObjektov stavObjektov = simulacia.getAktualnyStavObjektovSimulacie();
            this.tableViewKalendarUdalosti.setItems(FXCollections.observableArrayList(stavObjektov.getKalendarUdalosti()));
            this.tableViewStatistiky.setItems(FXCollections.observableArrayList(stavObjektov.getStatistikyReplikacie().statistiky_));
            
            CasnikInfo poslednyZvolenyCasnik = pamatajPoslednyStav ? comboBoxCasnici.getSelectionModel().selectedItemProperty().get() : null;

            comboBoxCasnici.setItems(FXCollections.observableArrayList(stavObjektov.getCasniciInfo()));
            if (poslednyZvolenyCasnik != null) {
                comboBoxCasnici.getSelectionModel().select((int) poslednyZvolenyCasnik.getIdCasnika() - 1);
                //tableViewCasnici.setItems(poslednyZvolenyCasnik.getInfo());
            }

            KucharInfo poslednyZvolenyKuchar = pamatajPoslednyStav ? comboBoxKuchari.getSelectionModel().selectedItemProperty().get() : null;

            comboBoxKuchari.setItems(FXCollections.observableArrayList(stavObjektov.getKuchariInfo()));
            if (poslednyZvolenyKuchar != null) {
                comboBoxKuchari.getSelectionModel().select((int) poslednyZvolenyKuchar.getIdKuchara() - 1);
            }

            StolInfo posledneInfoOStole = pamatajPoslednyStav ? comboBoxStoly.getSelectionModel().selectedItemProperty().get() : null;

            comboBoxStoly.setItems(FXCollections.observableArrayList(stavObjektov.getStolyInfo()));
            if (posledneInfoOStole != null) {
                comboBoxStoly.getSelectionModel().select((int) posledneInfoOStole.getIdStola() - 1);
            }


            tableViewFrontNaObsluhu.setItems(FXCollections.observableArrayList(stavObjektov.getFrontStolyCakajuceNaObsluhu().getInformacieOFronte()));
            tableViewFrontJedal.setItems(FXCollections.observableArrayList(stavObjektov.getFrontZakazniciCakajuciNaObsluhu().getInformacieOFronte()));
            tableViewFrontOdnesenieJedla.setItems(FXCollections.observableArrayList(stavObjektov.getFrontStolyCakajuceNaDonesenieJedla().getInformacieOFronte()));
            tableViewFrontZaplatenie.setItems(FXCollections.observableArrayList(stavObjektov.getFrontStolyCakajuceNaZaplatenie().getInformacieOFronte()));

        });

    }

}
