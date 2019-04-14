package Stanok.Agenti;

import OSPABA.Agent;
import Stanok.Asistenti.PlanovacPrichoduZakaznikov;
import Stanok.Manazeri.ManazerOkolia;
import Stanok.Simulacia.Id;
import Stanok.Simulacia.Mc;
import Stanok.SimulaciaStanok;

public class AgentOkolia extends Agent {

    public AgentOkolia(int id, SimulaciaStanok mySim, Agent parent) {
        super(id, mySim, parent);

        new ManazerOkolia(Id.manazerOkolia, mySim, this);

        new PlanovacPrichoduZakaznikov(Id.planovacPrichoduZakaznikov, mySim, this);

        addOwnMessage(Mc.spustiPlanovaniePrichodov);
        addOwnMessage(Mc.planovaniePrichoduZakaznika);
        addOwnMessage(Mc.koniecObsluhyZakaznika);
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }

}
