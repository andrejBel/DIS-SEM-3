package managers;

import OSPABA.*;
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
		{
			Sprava copy = (Sprava) message.createCopy();
			copy.setAddressee(mySim().findAgent(Id.agentZastavok));
			//notice(copy); // TODO zaregistrovat spravu agentovi a odkomentovat
		}
		{
			Sprava copy = (Sprava) message.createCopy();
			copy.setAddressee(mySim().findAgent(Id.agentNastupuVystupu));
			//notice(copy); // TODO zaregistrovat spravu agentovi a odkomentovat
		}
	}

	//meta! sender="AgentPohybu", id="94", type="Notice"
	public void processPrichodVozidlaNaZastavkuAgentPohybu(MessageForm message) {
        message.setAddressee(Id.procesNastupuZakaznikov);
        startContinualAssistant(message);
	}

	//meta! sender="AgentZastavok", id="110", type="Response"
	public void processPrichodVozidlaNaZastavkuAgentZastavok(MessageForm message) {
	}

	//meta! sender="AgentModelu", id="97", type="Notice"
	public void processPrichodZakaznikaNaZastavku(MessageForm message) {
	}

	//meta! sender="AgentNastupuVystupu", id="111", type="Response"
	public void processNastupVystupZakaznika(MessageForm message) {
	}

    public void processFinishProcesNastupuZakaznikov(MessageForm message) {
        message.setAddressee(mySim().findAgent(Id.agentPohybu));
        message.setCode(Mc.presunVozidlaNaDalsiuZastavku);
        notice(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
			case Mc.prichodVozidlaNaZastavku:
				switch (message.sender().id()) {
					case Id.agentZastavok:
						processPrichodVozidlaNaZastavkuAgentZastavok(message);
						break;

					case Id.agentPohybu:
						processPrichodVozidlaNaZastavkuAgentPohybu(message);
						break;
				}
				break;

			case Mc.prichodZakaznikaNaZastavku:
				processPrichodZakaznikaNaZastavku(message);
				break;

			case Mc.init:
				processInit(message);
				break;

			case Mc.finish:
				switch (message.sender().id()) {

					case Id.procesNastupuZakaznikov:
						processFinishProcesNastupuZakaznikov(message);
						break;
				}
				break;

			case Mc.nastupVystupZakaznika:
				processNastupVystupZakaznika(message);
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