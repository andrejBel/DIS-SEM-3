package Stanok.Manazeri;

import OSPABA.Manager;
import OSPABA.MessageForm;
import Stanok.Agenti.AgentModelu;
import Stanok.Simulacia.Id;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;

public class ManazerModelu extends Manager {

    public ManazerModelu(int id, SimulaciaStanok mySim, AgentModelu myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

    }

    @Override
    public void processMessage(MessageForm messageForm) {
        switch (messageForm.code()) {
            case Mc.init:
                messageForm.setAddressee(mySim().findAgent(Id.agentOkolia));
                messageForm.setCode(Mc.spustiPlanovaniePrichodov);
                notice(messageForm);
                break;
            case Mc.prichodZakaznika:
                messageForm.setCode(Mc.obsluzZakaznika);
                messageForm.setAddressee(mySim().findAgent(Id.agentStanku));
                request(messageForm);
                break;
            case Mc.koniecObsluhyZakaznika:
                Sprava sprava = (Sprava) messageForm;
                mySim().priemernyCasCakaniaZakaznikaRep_.addSample(sprava.getCasCakania());
                break;
        }
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }

    @Override
    public AgentModelu myAgent() {
        return (AgentModelu) super.myAgent();
    }

}
