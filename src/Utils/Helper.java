package Utils;

import GUI.TableColumnItem;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static javafx.beans.binding.Bindings.createBooleanBinding;

public class Helper {

    public static enum CASOVE_JEDNOTKY {
        ROK(86400 * 365),
        DEN(86400),
        HODINA(3600),
        MINUTA(60)
        ;


        CASOVE_JEDNOTKY(double pocetSekund) {
            this.pocetSekund_ = pocetSekund;
        }

        private double pocetSekund_;

        public double getPocetSekund() {
            return pocetSekund_;
        }
    }

    public static final String EMPTY_WARNING_MESSAGE = "Vstup musí byť zadaný";
    private static final String WARNING_MESSAGE_NUMBER = "Vstup musí byť celé číslo";
    private static final String WARNING_MESSAGE_DOUBLE_NUMBER = "Vstup musí byť desatinné číslo";


    private Helper() {
    }

    public static void nastavVypnutieOstatnych(JFXCheckBox checkBox, JFXCheckBox... checkBoxes) {
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (JFXCheckBox check: checkBoxes) {
                    check.setSelected(false);
                }
            }
        });
    }

    public static void DecorateTextFieldWithValidator(JFXTextField textField, SimpleBooleanProperty propertyToBind) {
        textField.getValidators().add(new RequiredFieldValidator() {
            {
                setMessage(EMPTY_WARNING_MESSAGE);
            }
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean validateResult = textField.validate();
            propertyToBind.set(validateResult);
        });
    }

    public static void DecorateTextFieldWithValidator(JFXTextField textField, SimpleBooleanProperty propertyToBind, int maximumlength, String atributeName) {
        textField.getValidators().addAll(new RequiredFieldValidator() {
            {
                setMessage(EMPTY_WARNING_MESSAGE);
            }
        }, new ValidatorBase() {
            @Override
            protected void eval() {
                TextInputControl textField = (TextInputControl) this.srcControl.get();
                String text = textField.getText();
                if (text != null && text.length() > 0) {
                    if (text.length() <= maximumlength) {
                        this.hasErrors.set(false);
                    } else {
                        setMessage(atributeName + " musí mať" + maximumlength + " znakov");
                        this.hasErrors.set(true);
                    }

                } else {
                    this.hasErrors.set(false);
                }
            }
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText().length() > maximumlength) {
                String s = textField.getText().substring(0, maximumlength);
                textField.setText(s);
            }
            boolean validateResult = textField.validate();
            propertyToBind.set(validateResult);
        });
    }

    public static void DecorateNumberTextFieldWithValidator(JFXTextField textField, SimpleBooleanProperty propertyToBind) {
        textField.getValidators().addAll(new RequiredFieldValidator() {
            {
                setMessage(EMPTY_WARNING_MESSAGE);
            }
        }, new ValidatorBase() {
            {
                setMessage(WARNING_MESSAGE_NUMBER);
            }

            @Override
            protected void eval() {
                TextInputControl textField = (TextInputControl) this.srcControl.get();
                String text = textField.getText();
                try {
                    this.hasErrors.set(false);
                    if (!text.isEmpty()) {
                        Long.parseLong(text);
                    }
                } catch (Exception var4) {
                    this.hasErrors.set(true);
                }
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            boolean validateResult = textField.validate();
            propertyToBind.set(validateResult);
        });

    }

    public static void DecorateDoubleTextFieldWithValidator(JFXTextField textField, SimpleBooleanProperty propertyToBind) {
        textField.getValidators().addAll(new RequiredFieldValidator() {
            {
                setMessage(EMPTY_WARNING_MESSAGE);
            }
        }, new DoubleValidator() {
            {
                setMessage(WARNING_MESSAGE_DOUBLE_NUMBER);
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d)?")) {
                textField.setText(newValue.replaceAll("[^\\d\\.]", ""));
            }
            boolean validateResult = textField.validate();
            propertyToBind.set(validateResult);
        });

    }

    public static boolean DisableButton(JFXButton button, List<ReadOnlyBooleanProperty> simpleBooleanProperties, Runnable validationCheck) {
        if (!button.isDisable() && simpleBooleanProperties.stream().anyMatch(simpleBooleanProperty -> !simpleBooleanProperty.get())) {
            if (validationCheck != null) {
                validationCheck.run();
            }
            button.disableProperty().unbind();
            button.disableProperty().bind(createBooleanBinding(
                    () -> simpleBooleanProperties.stream().anyMatch(simpleBooleanProperty -> !simpleBooleanProperty.get()), (Observable[]) simpleBooleanProperties.toArray()
            ));
            return true;
        }
        return false;
    }

    public static <T> void NaplnTabulkuDatami(TableView<T> tabulka, ArrayList<T> data) {
        ObservableList<T> items = tabulka.getItems();
        items.clear();
        for (T item : data) {
            items.add(item);
        }
    }

    public static void InstallCopyPasteHandler(TableView<?> table) {

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setOnKeyPressed(new TableKeyEventHandler());
    }

    public static void SetActionOnEnter(List<JFXTextField> jfxTextFields, Runnable action) {
        jfxTextFields.forEach(jfxTextField -> jfxTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                action.run();
            }
        }));
    }

    public static <T> void SetRowFactory(TableView<T> table, Function<T, Boolean> isUniqueFunction) {
        table.setRowFactory(new Callback<TableView<T>, TableRow<T>>() {
            @Override
            public TableRow<T> call(TableView<T> param) {
                return new TableRow<T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setStyle("");
                            return;
                        }
                        if (isUniqueFunction.apply(item)) {
                            setStyle("-fx-background-color: #00BCD4 ;");
                        } else {
                            setStyle("");
                        }
                    }
                };
            }
        });
    }

    /**
     * Copy/Paste keyboard event handler.
     * The handler uses the keyEvent's source for the clipboard data. The source must be of type TableView.
     */
    private static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCompination.match(keyEvent)) {

                if (keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    copySelectionToClipboard((TableView<?>) keyEvent.getSource());

                    System.out.println("Selection copied to clipboard");

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }

        }

    }

    public static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = -1;

        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            Object cell = table.getColumns().get(col).getCellData(row);

            // null-check: provide empty string for nulls
            if (cell == null) {
                cell = "";
            }

            // determine whether we advance in a row (tab) or a column
            // (newline).
            if (prevRow == row) {

                clipboardString.append('\t');

            } else if (prevRow != -1) {

                clipboardString.append('\n');

            }

            // create string from cell
            String text = cell.toString();

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

    public static String FormatujSimulacnyCas(double cas) {
        int dni = (int) (cas / CASOVE_JEDNOTKY.DEN.getPocetSekund());

        double zvysok = cas - (dni * CASOVE_JEDNOTKY.DEN.getPocetSekund());

        int hodiny = (int) (zvysok / CASOVE_JEDNOTKY.HODINA.getPocetSekund());
        zvysok -= (hodiny * CASOVE_JEDNOTKY.HODINA.getPocetSekund());

        int minuty = (int) (zvysok / CASOVE_JEDNOTKY.MINUTA.getPocetSekund());

        int sekundy = (int) (zvysok - ((double)minuty * CASOVE_JEDNOTKY.MINUTA.getPocetSekund()));

        if (dni > 0) {
            return String.format("%d, %02d : %02d : %02d"  ,dni, hodiny, minuty, sekundy );
        } else {
            return String.format("%02d : %02d : %02d"  , hodiny, minuty, sekundy );
        }

    }

    public static String FormatujDouble(double cislo) {
        return FormatujDouble(cislo, 4);
    }

    public static String FormatujDouble(double cislo, int pocetDesatinnychMiest) {
        return String.format("%." + pocetDesatinnychMiest + "f", cislo);
    }

    public static <T> void PridajTabulkeStlpce(TableView<T> table, TableColumnItem<T>... attributes) {
        table.getColumns().clear();
        for (TableColumnItem atribut: attributes) {
            TableColumn<T, String> tableColumn = new TableColumn<>(atribut.viewName_);
            tableColumn.setCellValueFactory(param -> new SimpleStringProperty((String) atribut.spracuj_.apply(param.getValue())));
            table.getColumns().add(tableColumn);
        }
    }

    public static <T> void PridajTabulkeStlpce(TableView<T> table, List<TableColumnItem<T>> attributes) {
        PridajTabulkeStlpce(table, (TableColumnItem<T>[]) attributes.toArray());

    }

}