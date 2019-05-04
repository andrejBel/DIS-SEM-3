package managers;

import Model.Cestujuci;
import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Vozidlo;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="5"
public class ManagerPrepravy extends Manager {
    public ManagerPrepravy(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="AgentModelu", id="92", type="Notice"
    public void processInit(MessageForm message) {
        {
            Sprava copy = (Sprava) message.createCopy();
            copy.setAddressee(mySim().findAgent(Id.agentPohybu));
            notice(copy);
        }
    }

    //meta! sender="AgentModelu", id="97", type="Notice"
    public void processPrichodCestujucehoNaZastavku(MessageForm message) {
        Sprava sprava = (Sprava) message;
        sprava.setAddressee(Id.agentZastavok);
        notice(sprava);
    }

    private void processPrichodVozidlaNaZastavkuAgentZastavok(MessageForm message) {
        Sprava copy = (Sprava) message; // TODO rozhodnutie ci kopia ci nie
        Vozidlo vozidlo = copy.getVozidlo();
        if (vozidlo.getPocetNastupujucichCestujucich() != 0) {
            throw new RuntimeException("Vozidlo sanesmie hybat, proces nastupu neukonceny");
        }
        if (vozidlo.getPocetVystupujucichCestujucich() != 0) {
            throw new RuntimeException("Vozidlo sanesmie hybat, proces vystupu neukonceny");
        }

        copy.setAddressee(mySim().findAgent(Id.agentPohybu));
        copy.setCode(Mc.presunVozidlaNaDalsiuZastavku);
        notice(copy);
    }

    private void processPrichodVozidlaNaZastavkuAgentPohybu(MessageForm message) {
        Sprava sprava = (Sprava) message;
        //sprava.setCode(Mc.prichodVozidlaNaZastavku); TODO remove
        sprava.setAddressee(Id.agentZastavok);
        request(sprava);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        System.out.println(message.code());
        throw new RuntimeException("Default vetva by nemala nikdy nastat");
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.init:
                processInit(message);
                break;

            case Mc.prichodVozidlaNaZastavku:
                switch (message.sender().id()) {
                    case Id.agentPohybu:
                        processPrichodVozidlaNaZastavkuAgentPohybu(message);
                        break;

                    case Id.agentZastavok:
                        processPrichodVozidlaNaZastavkuAgentZastavok(message);
                        break;
                        default:
                        processDefault(message);
                        break;
                }
                break;

            case Mc.prichodCestujucehoNaZastavku:
                processPrichodCestujucehoNaZastavku(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }


    //meta! tag="end"


    //meta! tag="end"

    @Override
    public AgentPrepravy myAgent() {
        return (AgentPrepravy) super.myAgent();
    }

    @Override
    public SimulaciaDopravy mySim() {
        return (SimulaciaDopravy) super.mySim();
    }
}