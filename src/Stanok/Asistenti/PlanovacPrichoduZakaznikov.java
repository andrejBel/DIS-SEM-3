package Stanok.Asistenti;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;
import Stanok.Agenti.AgentOkolia;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;

public class PlanovacPrichoduZakaznikov extends Scheduler {

    ExponentialRNG generatorPrichoduZakaznikov_ = new ExponentialRNG(420.0D);

    public PlanovacPrichoduZakaznikov(int id, SimulaciaStanok mySim, AgentOkolia myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void processMessage(MessageForm messageForm) {
        switch (messageForm.code()){
            case Mc.start:
                messageForm.setCode(Mc.planovaniePrichoduZakaznika);
                hold(generatorPrichoduZakaznikov_.sample(), messageForm);
                break;
            case Mc.planovaniePrichoduZakaznika:
                Sprava copy = (Sprava) (messageForm).createCopy();
                assistantFinished(messageForm);
                hold(generatorPrichoduZakaznikov_.sample(), copy);
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
