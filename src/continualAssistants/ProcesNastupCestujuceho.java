package continualAssistants;

import Model.Cestujuci;
import Model.Vozidlo;
import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="368"
public class ProcesNastupCestujuceho extends Process {

	public ProcesNastupCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentZastavok", id="370", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;

		myAgent().getProcesNastupuCestujucich().nastupCestujuceho( sprava, (casNastupu, spravaCopy) -> {
			hold(casNastupu, spravaCopy);
			return null;
		});

	}

	//meta! sender="AgentZastavok", id="379", type="Notice"
	public void processCestujuciNastupil(MessageForm message) {
		Sprava sprava = (Sprava) message;
		myAgent().getProcesNastupuCestujucich().cestujuciNastupil(sprava);
		assistantFinished(sprava);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.start:
			processStart(message);
		break;

		case Mc.cestujuciNastupil:
			processCestujuciNastupil(message);
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

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

}