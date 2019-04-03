package Simulacia;


import Utils.Helper;
import org.junit.Before;
import org.junit.Test;

public class SimulaciaRestauraciaTest {

    SimulaciaRestauracia simulacia_;

    @Before
    public void initialize() {
        simulacia_ = new SimulaciaRestauracia(Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund() *9,1, 0, 100000, 1, true, false, true, false, 4, 15);
    }

    @Test
    public void spustiSimulaciu() {
        simulacia_.start();
    }

}
