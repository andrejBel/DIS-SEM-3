package GUI.Controller;

import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.KonfiguraciaVozidiel;
import Model.VozidloKonfiguracia;
import Utils.Helper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import simulation.SimulaciaDopravy;
import simulation.SimulaciaWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CKonfiguracie extends CWindowBase {

    @FXML
    private JFXComboBox<Integer> comboBoxHodiny;

    @FXML
    private JFXComboBox<Integer> comboBoxMinuty;

    @FXML
    private JFXComboBox<Integer> comboBoxSekundy;

    @FXML
    private JFXTextField textFieldNazovSuboru;

    @FXML
    private JFXComboBox<TYP_VOZIDLA> comboBoxTypVozidla;

    @FXML
    private JFXComboBox<TYP_LINKY> comboBoxLinky;

    @FXML
    private JFXComboBox<PREVADZKA_LINIEK> comboBoxPrevadzkaLinky;

    @FXML
    private JFXButton buttonPridajKonfiguraciu;

    @FXML
    private JFXButton buttonOdstranKonfiguracie;

    @FXML
    private JFXButton buttonUlozKonfiguraciu;

    @FXML
    private JFXButton buttonNacitajKonfiguraciu;

    @FXML
    private TableView<VozidloKonfiguracia> tableViewKonfiguracie;

    private SimpleBooleanProperty isTypVozidlaOk = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isTypLinkyOk = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isPrevadzkaLinkyOk = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty isNazovSuboruOkOK = new SimpleBooleanProperty(false);



    private TYP_VOZIDLA typVozidla_ = null;
    private TYP_LINKY typLinky_ = null;
    private PREVADZKA_LINIEK prevadzkaLiniek_ = null;

    private FileChooser.ExtensionFilter extFilter_;
    private FileChooser fileChooser_;
    private DirectoryChooser directoryChooser_;
    private SimulaciaDopravy _simulacia;

    public CKonfiguracie(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);
        setModality(Modality.WINDOW_MODAL);
        _simulacia = simulaciaWrapper.getSimulaciaDopravy();

        extFilter_ = new FileChooser.ExtensionFilter("Comma separeted files (*.csv)", "*.csv");
        fileChooser_ = new FileChooser();
        directoryChooser_ = new DirectoryChooser();
        fileChooser_.getExtensionFilters().add(extFilter_);

        fileChooser_.setTitle("Vybrať súbor na načítanie konfigurácie");
        directoryChooser_.setTitle("Vybrať priečinok na uloženie konfigurácie");

        fileChooser_.setInitialDirectory(new File(System.getProperty("user.dir")));
        directoryChooser_.setInitialDirectory(new File(System.getProperty("user.dir")));

        for (int i = 0; i < 24; i++) {
            comboBoxHodiny.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            comboBoxMinuty.getItems().add(i);
            comboBoxSekundy.getItems().add(i);
        }
        resetComboBoxesTime();


        comboBoxTypVozidla.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TYP_VOZIDLA typVozidla = newValue;
            isTypVozidlaOk.set(typVozidla != null);
            this.typVozidla_ = typVozidla;
        });

        comboBoxTypVozidla.setConverter(new StringConverter<TYP_VOZIDLA>() {
            @Override
            public String toString(TYP_VOZIDLA object) {
                if (object == null) {
                    return "";
                }
                return object.getNazov();
            }

            @Override
            public TYP_VOZIDLA fromString(String string) {
                return null;
            }
        });

        comboBoxTypVozidla.getItems().addAll(TYP_VOZIDLA.values());

        comboBoxLinky.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TYP_LINKY typLinky = newValue;
            isTypLinkyOk.set(typLinky != null);
            this.typLinky_ = typLinky;
        });

        comboBoxLinky.setConverter(new StringConverter<TYP_LINKY>() {
            @Override
            public String toString(TYP_LINKY object) {
                if (object == null) {
                    return "";
                }
                return object.getNazovLinky();
            }

            @Override
            public TYP_LINKY fromString(String string) {
                return null;
            }
        });

        comboBoxLinky.getItems().addAll(TYP_LINKY.values());


        comboBoxPrevadzkaLinky.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            PREVADZKA_LINIEK prevadzkaLiniek = newValue;
            isPrevadzkaLinkyOk.set(prevadzkaLiniek != null);
            this.prevadzkaLiniek_ = prevadzkaLiniek;
        });

        comboBoxPrevadzkaLinky.setConverter(new StringConverter<PREVADZKA_LINIEK>() {
            @Override
            public String toString(PREVADZKA_LINIEK object) {
                if (object == null) {
                    return "";
                }
                return object.getNazov();
            }

            @Override
            public PREVADZKA_LINIEK fromString(String string) {
                return null;
            }
        });
        comboBoxPrevadzkaLinky.getItems().addAll(PREVADZKA_LINIEK.values());

        Helper.DecorateComboBoxWithValidator(comboBoxTypVozidla, isTypVozidlaOk);
        Helper.DecorateComboBoxWithValidator(comboBoxLinky, isTypLinkyOk);
        Helper.DecorateComboBoxWithValidator(comboBoxPrevadzkaLinky, isPrevadzkaLinkyOk);


        Helper.DecorateTextFieldWithValidator(textFieldNazovSuboru, isNazovSuboruOkOK);

        Helper.PridajTabulkeStlpce(tableViewKonfiguracie, VozidloKonfiguracia.ATRIBUTY);
        tableViewKonfiguracie.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        buttonPridajKonfiguraciu.setOnAction(event -> {
            if (Helper.DisableButton(buttonPridajKonfiguraciu, Arrays.asList(isTypLinkyOk, isTypVozidlaOk), () -> {

                comboBoxLinky.validate();
                comboBoxTypVozidla.validate();
            })) {
                return;
            }
            int hodiny = comboBoxHodiny.getSelectionModel().getSelectedItem();
            int minuty = comboBoxMinuty.getSelectionModel().getSelectedItem();
            int sekundy = comboBoxSekundy.getSelectionModel().getSelectedItem();
            double casPrijazduKPrvejZastavke = hodiny * Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund() + minuty * Helper.CASOVE_JEDNOTKY.MINUTA.getPocetSekund() + sekundy; // TODO

            this.tableViewKonfiguracie.getItems().add(new VozidloKonfiguracia(typVozidla_, typLinky_, casPrijazduKPrvejZastavke));
            this.tableViewKonfiguracie.getItems().sort(
                    Comparator.comparingDouble(VozidloKonfiguracia::getCasPrijazduNaPrvuZastavku)
                    .thenComparing((v1, v2) -> TYP_LINKY.GetComparator().compare(v1.getTypLinky(), v2.getTypLinky()))
                    .thenComparing((v1, v2) -> TYP_VOZIDLA.GetComparator().compare(v1.getTypVozidla(), v2.getTypVozidla()))
            );

            buttonPridajKonfiguraciu.disableProperty().unbind();
            buttonPridajKonfiguraciu.setDisable(false);


        });



        buttonOdstranKonfiguracie.setOnAction(event -> {
            ObservableList<VozidloKonfiguracia> selectedRows = tableViewKonfiguracie.getSelectionModel().getSelectedItems();
// we don't want to iterate on same collection on with we remove items
            tableViewKonfiguracie.getItems().removeAll(selectedRows);
        });

        buttonUlozKonfiguraciu.setOnAction(event -> {
            if (Helper.DisableButton(buttonUlozKonfiguraciu, Arrays.asList(isNazovSuboruOkOK, isPrevadzkaLinkyOk), () -> {
                textFieldNazovSuboru.validate();
                comboBoxPrevadzkaLinky.validate();
            })) {
                return;
            }
            File file = directoryChooser_.showDialog(stage);
            if (file != null) {
                String directoryPath = file.getAbsolutePath();
                String fileName = textFieldNazovSuboru.getText();
                if (!fileName.endsWith(".csv") ) {
                    fileName = fileName + ".csv";
                }
                String path = directoryPath + File.separator + fileName;
                System.out.println(path);

                if (_simulacia.ulozKonfiguraciuVozidiel(path, getKonfiguracie())) {
                    showSuccessDialog("Konfigurácia uložená");
                } else {
                    showWarningDialog("Konfigurácia nebola uložená");
                }
                clearAllItems();
            }


        });

        buttonNacitajKonfiguraciu.setOnAction(event -> {
            File file = fileChooser_.showOpenDialog(stage);
            if (file != null) {
                String filePath = file.getAbsolutePath();
                System.out.println(filePath);
                KonfiguraciaVozidiel konfiguracia = new KonfiguraciaVozidiel();
                if (_simulacia.nacitajKonfiguraciuVozidiel(filePath, konfiguracia)) {
                    konfiguraciaToGUI(konfiguracia);
                    showSuccessDialog("Konfigurácia úspešne načítaná");
                } else {
                    showWarningDialog("Konfigurácia nebola uložená");
                }
                clearAllItems();
            }
        });

        setOnOpen(() -> {
            clearAllItems();
        });

    }

    private void resetComboBoxesTime() {
        comboBoxHodiny.getSelectionModel().select(0);
        comboBoxSekundy.getSelectionModel().select(0);
        comboBoxMinuty.getSelectionModel().select(0);
    }

    private void clearAllItems() {
        buttonPridajKonfiguraciu.disableProperty().unbind();
        buttonPridajKonfiguraciu.setDisable(false);

        buttonUlozKonfiguraciu.disableProperty().unbind();
        buttonUlozKonfiguraciu.setDisable(false);

        comboBoxTypVozidla.disableProperty().unbind();
        comboBoxTypVozidla.setDisable(false);

        comboBoxLinky.disableProperty().unbind();
        comboBoxLinky.setDisable(false);

        comboBoxPrevadzkaLinky.disableProperty().unbind();
        comboBoxPrevadzkaLinky.setDisable(false);


        textFieldNazovSuboru.setText("");

        comboBoxTypVozidla.getSelectionModel().select(-1);
        comboBoxLinky.getSelectionModel().select(-1);


        textFieldNazovSuboru.resetValidation();

        comboBoxLinky.resetValidation();
        comboBoxTypVozidla.resetValidation();
        resetComboBoxesTime();

        //tableViewKonfiguracie.getItems().clear();


    }

    @Override
    protected String getViewFileName() {
        return "konfiguracie.fxml";
    }

    @Override
    public String getViewName() {
        return "Konfigurácie";
    }

    public KonfiguraciaVozidiel getKonfiguracie() {
        ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>();
        konfiguracie.addAll(tableViewKonfiguracie.getItems());
        return new KonfiguraciaVozidiel(prevadzkaLiniek_, konfiguracie);
    }

    public void konfiguraciaToGUI(KonfiguraciaVozidiel konfiguracia) {
        tableViewKonfiguracie.getItems().clear();
        tableViewKonfiguracie.getItems().addAll(konfiguracia.getKonfiguraciaVozidiel());
        comboBoxPrevadzkaLinky.getSelectionModel().select(konfiguracia.getPrevadzkaLiniek());

    }

}
