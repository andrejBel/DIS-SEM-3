package Model;

import OSPABA.Entity;
import OSPABA.Simulation;

public abstract class SimulationEntity extends Entity {

    public SimulationEntity(Simulation mySim) {
        super(mySim);
    }

    public abstract void beforeReplication();

}
