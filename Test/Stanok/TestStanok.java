package Stanok;

import org.junit.Before;
import org.junit.Test;

public class TestStanok {

    SimulaciaStanok simulaciaStanok_;

    @Before
    public void initialize() {
        simulaciaStanok_ = new SimulaciaStanok();
    }

    @Test
    public void spustiSimulaciu() {
        simulaciaStanok_.simulate(100, 9E7);
    }

}
