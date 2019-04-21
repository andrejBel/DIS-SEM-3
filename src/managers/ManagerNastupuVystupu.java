package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="106"
public class ManagerNastupuVystupu extends Manager {
	public ManagerNastupuVystupu(int id, Simulation mySim, Agent myAgent) {
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

	//meta! userInfo="Removed from model"
	public void processNastupVystupZakaznika(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		switch (message.code()) {
		}
	}

	//meta! sender="AgentPrepravy", id="213", type="Request"
	public void processNastupCestujuceho(MessageForm message) {
	}

	//meta! sender="AgentPrepravy", id="217", type="Request"
	public void processVystupCestujuceho(MessageForm message) {
	}

	//meta! sender="ProcessNastupuCestujuceho", id="233", type="Notice"
	public void processFinishProcessNastupuCestujuceho(MessageForm message) {
	}

	//meta! sender="ProcessVystupuCestujuceho", id="241", type="Notice"
	public void processFinishProcessVystupuCestujuceho(MessageForm message) {
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.vystupCestujuceho:
			processVystupCestujuceho(message);
		break;

		case Mc.nastupCestujuceho:
			processNastupCestujuceho(message);
		break;

		case Mc.finish:
			switch (message.sender().id()) {
			case Id.processNastupuCestujuceho:
				processFinishProcessNastupuCestujuceho(message);
			break;

			case Id.processVystupuCestujuceho:
				processFinishProcessVystupuCestujuceho(message);
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
	public AgentNastupuVystupu myAgent() {
		return (AgentNastupuVystupu)super.myAgent();
	}

}