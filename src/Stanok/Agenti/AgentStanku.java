package Stanok.Agenti;

import OSPABA.Agent;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import Stanok.Asistenti.ProcesObsluhyZakaznika;
import Stanok.Manazeri.ManazerStanku;
import Stanok.Simulacia.Id;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;

public class AgentStanku extends Agent {

    private boolean obsadeny_ = false;
    private SimQueue<Sprava> zakaznici_;

    public AgentStanku(int id, SimulaciaStanok mySim, Agent parent) {
        super(id, mySim, parent);

        new ManazerStanku(Id.manazerStanku, mySim, this);

        new ProcesObsluhyZakaznika(Id.procesObsluhyZakaznika, mySim, this);
        zakaznici_ = new SimQueue<>(new WStat(mySim));

        addOwnMessage(Mc.obsluzZakaznika);
        addOwnMessage(Mc.koniecObsluhyZakaznika);
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        zakaznici_.clear();
        obsadeny_ = false;
    }

    public boolean isObsadeny() {
        return obsadeny_;
    }

    public void setObsadeny(boolean obsadeny) {
        this.obsadeny_ = obsadeny;
    }

    public SimQueue<Sprava> getZakaznici() {
        return zakaznici_;
    }
}

