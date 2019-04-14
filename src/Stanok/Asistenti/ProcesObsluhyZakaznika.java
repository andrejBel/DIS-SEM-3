package Stanok.Asistenti;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Process;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;
import Stanok.Agenti.AgentStanku;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;



public class ProcesObsluhyZakaznika extends Process {

    ExponentialRNG generatorObsluhyZakaznika_ = new ExponentialRNG(360.0);

    public ProcesObsluhyZakaznika(int id, Simulation mySim, AgentStanku myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void processMessage(MessageForm messageForm) {
        Sprava sprava = (Sprava) messageForm;
        switch (sprava.code()) {
            case Mc.start:
                messageForm.setCode(Mc.koniecObsluhyZakaznika);
                hold(generatorObsluhyZakaznika_.sample(), messageForm);
                break;
            case Mc.koniecObsluhyZakaznika:
                assistantFinished(messageForm);
                break;
        }

    }

    @Override
    public AgentStanku myAgent() {
        return (AgentStanku) super.myAgent();
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }
}
