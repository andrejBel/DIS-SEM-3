package continualAssistants;

import Model.Vozidlo;
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
		Sprava sprava = (Sprava) message;

		Vozidlo vozidlo = sprava.getVozidlo();
		if (vozidlo == null) {
			throw new RuntimeException("Vozidlo musi byt nastavene");
		}

		double casPrichoduNaPrvuZastavku = vozidlo.getCasPrijazduNaPrvuZastavku();
		if (mySim().currentTime() != 0.0) {
			throw new RuntimeException("Pri planovani vyjazdu musi byt cas simulacie 0!!!");
		}

		sprava.setCode(Mc.prichodVozidlaNaZastavku);
		hold(casPrichoduNaPrvuZastavku, sprava);
	}

	//meta! sender="AgentPohybu", id="124", type="Notice"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
		Sprava sprava = (Sprava) message;
		sprava.getVozidlo().vykonajPrijazdKPrvejZastavke();
		assistantFinished(message);
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
