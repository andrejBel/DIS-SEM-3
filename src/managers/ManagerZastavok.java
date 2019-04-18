package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="87"
public class ManagerZastavok extends Manager {
	public ManagerZastavok(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentPrepravy", id="110", type="Request"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
	}

	//meta! sender="AgentPrepravy", id="98", type="Notice"
	public void processPrichodZakaznikaNaZastavku(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.prichodZakaznikaNaZastavku:
			processPrichodZakaznikaNaZastavku(message);
		break;

		case Mc.prichodVozidlaNaZastavku:
			processPrichodVozidlaNaZastavku(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentZastavok myAgent() {
		return (AgentZastavok)super.myAgent();
	}

}