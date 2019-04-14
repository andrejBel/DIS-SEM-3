package Stanok.Manazeri;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import Stanok.Agenti.AgentModelu;
import Stanok.Agenti.AgentOkolia;
import Stanok.Simulacia.Id;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;

public class ManazerOkolia extends Manager {

    public ManazerOkolia(int id, SimulaciaStanok mySim, Agent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void processMessage(MessageForm messageForm) {

        switch (messageForm.code()) {
            case Mc.spustiPlanovaniePrichodov:
                Sprava sprava = new Sprava(mySim());
                sprava.setAddressee(Id.planovacPrichoduZakaznikov);
                startContinualAssistant(sprava);
                break;

            case Mc.finish:
            switch (messageForm.sender().id()) {
                case Id.planovacPrichoduZakaznikov:
                    messageForm.setCode(Mc.prichodZakaznika);
                    messageForm.setAddressee(Id.agentModelu);
                    notice(messageForm);
                    break;
            }
            break;
        }
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }

    @Override
    public AgentOkolia myAgent() {
        return (AgentOkolia) super.myAgent();
    }

}
