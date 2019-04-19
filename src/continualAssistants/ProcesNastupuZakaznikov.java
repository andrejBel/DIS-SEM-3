package continualAssistants;

import OSPABA.*;
import Utils.Helper;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="177"
public class ProcesNastupuZakaznikov extends Process {
	public ProcesNastupuZakaznikov(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPrepravy", id="188", type="Notice"
	public void processZakazniciNastupili(MessageForm message) {
		assistantFinished(message);
	}

	//meta! sender="AgentPrepravy", id="180", type="Notice"
	public void processStart(MessageForm message) {
		message.setCode(Mc.zakazniciNastupili);
		hold(Helper.CASOVE_JEDNOTKY.MINUTA.getPocetSekund(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.zakazniciNastupili:
			processZakazniciNastupili(message);
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
	public AgentPrepravy myAgent() {
		return (AgentPrepravy)super.myAgent();
	}

}
