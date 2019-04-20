import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.KonfiguraciaVozidiel;
import Model.VozidloKonfiguracia;
import Utils.Helper;
import org.junit.Before;
import org.junit.Test;
import simulation.SimulaciaDopravy;

import java.util.ArrayList;
import java.util.Arrays;

public class TestSimulaciaDopravy {

    SimulaciaDopravy _simulaciaDopravy;

    @Before
    public void initialize() {
        _simulaciaDopravy = new SimulaciaDopravy();
    }

    @Test
    public void spustiSimulaciu() {

        _simulaciaDopravy.setKonfiguracia((
                new KonfiguraciaVozidiel(PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA, new ArrayList<>(Arrays.asList(

                        new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_C, 90.0)
                )))
        ));
        _simulaciaDopravy.simulate(100, Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund() * 3);
    }
}
