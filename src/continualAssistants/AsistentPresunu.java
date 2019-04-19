package continualAssistants;

import Model.Vozidlo;
import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="118"
public class AsistentPresunu extends Scheduler {
	public AsistentPresunu(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPohybu", id="157", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();
		sprava.setCode(Mc.prichodVozidlaNaZastavku);
		double casPotrebnyNaPresunKDalsejZastavke = vozidlo.getCasPotrebnyPrePresunKDalsejZastavke();

		vozidlo.vykonajPresunKDalsejZastavke();

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().setCoPozastaviloSimulaciu("Začiatok presunu vozidla " + vozidlo.getIdVozidla() + ": " + vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky() + "->" + vozidlo.getNasledujucaZastavka().getNazovZastavky());
		}

		hold(casPotrebnyNaPresunKDalsejZastavke, sprava);
	}

	//meta! sender="AgentPohybu", id="121", type="Notice"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
		Sprava sprava = (Sprava) message;
		sprava.getVozidlo().ukonciPresun();
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

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}
}
