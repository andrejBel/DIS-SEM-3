package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="116"
public class AsistentVyjazdu extends Scheduler {
	public AsistentVyjazdu(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPohybu", id="154", type="Notice"
	public void processStart(MessageForm message) {
	}

	//meta! sender="AgentPohybu", id="124", type="Notice"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.start:
			processStart(message);
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
	public AgentPohybu myAgent() {
		return (AgentPohybu)super.myAgent();
	}

}
