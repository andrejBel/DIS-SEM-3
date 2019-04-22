package continualAssistants;

import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="309"
public class NaplanujPresunVozidlaNaZastavku extends Process {
	public NaplanujPresunVozidlaNaZastavku(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPrepravy", id="312", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;

		myAgent().vlozVozidloDoFrontuVozidielCakajucichNaZastavke((Sprava) sprava.createCopy());
		sprava.setCode(Mc.presunVozidlo);
		hold(KONSTANTY.KOLKO_CAKA_PO_NASTUPE_VSETKYCH_CESTUJUCICH, sprava);
	}

	//meta! sender="AgentPrepravy", id="315", type="Notice"
	public void processPresunVozidlo(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default nemoze nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.presunVozidlo:
			processPresunVozidlo(message);
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
