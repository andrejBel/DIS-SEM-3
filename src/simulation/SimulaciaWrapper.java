package simulation;

import Stanok.SimulaciaStanok;

public class SimulaciaWrapper {

    private SimulaciaStanok simulaciaStanok_ = new SimulaciaStanok();

    private SimulaciaDopravy _simulaciaDopravy = new SimulaciaDopravy();

    public SimulaciaStanok getSimulaciaStanok() { return simulaciaStanok_ ;}

    public SimulaciaDopravy getSimulaciaDopravy() {
        return _simulaciaDopravy;
    }
}
