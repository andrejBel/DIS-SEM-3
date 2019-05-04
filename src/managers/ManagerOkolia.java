package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="3"
public class ManagerOkolia extends Manager {
	public ManagerOkolia(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentModelu", id="15", type="Notice"
	public void processInit(MessageForm message) {
		for (PlanovacPrichodovCestujucichNaZastavku planovac: myAgent().getPlanovacovePrichodov()) {
			Sprava copy = (Sprava) message.createCopy();
			copy.setAddressee(planovac);
			startContinualAssistant(copy);
		}
	}

	//meta! sender="PlanovacPrichodovCestujucichNaZastavku", id="163", type="Notice"
	public void processFinish(MessageForm message) {
		Sprava sprava = (Sprava) message;
		sprava.setAddressee(Id.agentModelu);
		sprava.setCode(Mc.prichodCestujucehoNaZastavku);
		notice(sprava);
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
			case Mc.init:
				processInit(message);
				break;

			case Mc.finish:
				processFinish(message);
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