package managers;

import OSPABA.*;
import Utils.Helper;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

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

	//meta! sender="AgentPohybu", id="94", type="Notice"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
		message.setAddressee(Id.procesNastupuZakaznikov);
		startContinualAssistant(message);
	}

	//meta! sender="AgentModelu", id="97", type="Notice"
	public void processPrichodZakaznikaNaZastavkuAgentModelu(MessageForm message) {
		Sprava sprava = (Sprava) message;
		sprava.setAddressee(Id.agentZastavok);
		notice(sprava);
	}

	//meta! sender="AgentZastavok", id="258", type="Response"
	public void processCestujuciNaZastavke(MessageForm message) {
	}

	//meta! sender="AgentNastupuVystupu", id="213", type="Response"
	public void processNastupCestujuceho(MessageForm message) {
	}

	//meta! sender="AgentNastupuVystupu", id="217", type="Response"
	public void processVystupCestujuceho(MessageForm message) {
	}

	//meta! sender="AgentZastavok", id="98", type="Response"
	public void processPrichodZakaznikaNaZastavkuAgentZastavok(MessageForm message) {
	}


    public void processFinishProcesNastupuZakaznikov(MessageForm message) {
        message.setAddressee(mySim().findAgent(Id.agentPohybu));
        message.setCode(Mc.presunVozidlaNaDalsiuZastavku);
        notice(message);
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

			case Mc.cestujuciNaZastavke:
				processCestujuciNaZastavke(message);
				break;

			case Mc.vystupCestujuceho:
				processVystupCestujuceho(message);
				break;

			case Mc.prichodZakaznikaNaZastavku:
				switch (message.sender().id()) {
					case Id.agentModelu:
						processPrichodZakaznikaNaZastavkuAgentModelu(message);
						break;

					case Id.agentZastavok:
						processPrichodZakaznikaNaZastavkuAgentZastavok(message);
						break;
				}
				break;

			case Mc.nastupCestujuceho:
				processNastupCestujuceho(message);
				break;

			case Mc.prichodVozidlaNaZastavku:
				processPrichodVozidlaNaZastavku(message);
				break;
			case Mc.finish:
				switch (message.sender().id()) {
					case Id.procesNastupuZakaznikov:
						processFinishProcesNastupuZakaznikov(message);
					break;
			}
			break;
			default:
				processDefault(message);
				break;
		}
	}

	//meta! tag="end"

	@Override
	public AgentPrepravy myAgent() {
		return (AgentPrepravy)super.myAgent();
	}

}