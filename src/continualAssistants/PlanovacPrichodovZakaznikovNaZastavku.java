package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="136"
public class PlanovacPrichodovZakaznikovNaZastavku extends Scheduler {
	public PlanovacPrichodovZakaznikovNaZastavku(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolia", id="148", type="Notice"
	public void processStart(MessageForm message) {
	}

	//meta! sender="AgentOkolia", id="139", type="Notice"
	public void processPrichodZakaznikaNaZastavku(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.prichodZakaznikaNaZastavku:
			processPrichodZakaznikaNaZastavku(message);
		break;

		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentOkolia myAgent() {
		return (AgentOkolia)super.myAgent();
	}

}
