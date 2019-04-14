package Stanok.Manazeri;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import Stanok.Agenti.AgentModelu;
import Stanok.Agenti.AgentStanku;
import Stanok.Simulacia.Id;
import Stanok.Simulacia.Mc;
import Stanok.Simulacia.Sprava;
import Stanok.SimulaciaStanok;

public class ManazerStanku extends Manager {

    public ManazerStanku(int id, SimulaciaStanok mySim, AgentStanku myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void processMessage(MessageForm messageForm) {
        //addOwnMessage(Mc.obsluzZakaznika);
        //addOwnMessage(Mc.koniecObsluhyZakaznika);
        Sprava sprava;
        switch (messageForm.code()) {
            case Mc.obsluzZakaznika:
                sprava = (Sprava) messageForm;
                sprava.setCasVstupu(mySim().currentTime());

                if (myAgent().isObsadeny()) {
                    myAgent().getZakaznici().add(sprava);
                } else {
                    obsluzZakaznika(sprava);
                }
            break;
            case Mc.finish:
                switch (messageForm.sender().id()) {
                    case Id.procesObsluhyZakaznika: {
                        myAgent().setObsadeny(false);
                        if (myAgent().getZakaznici().size() > 0) {
                            obsluzZakaznika(myAgent().getZakaznici().poll());
                        }
                        messageForm.setAddressee(Id.agentModelu);
                        messageForm.setCode(Mc.koniecObsluhyZakaznika);
                        response(messageForm);

                    }
                }
        }
    }

    @Override
    public SimulaciaStanok mySim() {
        return (SimulaciaStanok) super.mySim();
    }

    @Override
    public AgentStanku myAgent() {
        return (AgentStanku) super.myAgent();
    }

    private void obsluzZakaznika(Sprava sprava) {
        if (!myAgent().isObsadeny()) {
            sprava.setCasKoncaCakania(mySim().currentTime());
            sprava.setAddressee(Id.procesObsluhyZakaznika);
            myAgent().setObsadeny(true);
            startContinualAssistant(sprava);
        } else {
            throw new RuntimeException("Nemozes obsadit obsadeneho pracovnika");
        }

    }

}
