package Stanok.Agenti;

import OSPABA.Agent;


import Stanok.Manazeri.ManazerModelu;
import Stanok.Simulacia.Id;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;

public class AgentModelu extends Agent {

    public AgentModelu(int id, SimulaciaStanok mySim, Agent parent) {
        super(id, mySim, parent);

        new ManazerModelu(Id.manazerModelu, mySim(), this);

        addOwnMessage(Mc.init);
        addOwnMessage(Mc.prichodZakaznika);
        addOwnMessage(Mc.koniecObsluhyZakaznika);

    }

    public void spustiSimulaciu() {
        Sprava message = new Sprava(mySim());
        message.setCode(Mc.init);
        message.setAddressee(this);
        manager().notice(message);
    }


    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }



}
