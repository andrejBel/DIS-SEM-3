package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="224"
public class ProcessVystupuCestujuceho extends Process {
	public ProcessVystupuCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentNastupuVystupu", id="230", type="Notice"
	public void processStart(MessageForm message) {
	}

	//meta! sender="AgentNastupuVystupu", id="235", type="Notice"
	public void processKoniecVystupu(MessageForm message) {
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
		case Mc.koniecVystupu:
			processKoniecVystupu(message);
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
	public AgentNastupuVystupu myAgent() {
		return (AgentNastupuVystupu)super.myAgent();
	}

}
