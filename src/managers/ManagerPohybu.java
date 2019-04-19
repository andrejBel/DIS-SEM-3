package managers;

import Model.Vozidlo;
import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="86"
public class ManagerPohybu extends Manager {
	public ManagerPohybu(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentPrepravy", id="96", type="Notice"
	public void processInit(MessageForm message) {
		for (Vozidlo vozidlo: myAgent().getVozidla()) {
			Sprava copy = (Sprava) message.createCopy();
			copy.setVozidlo(vozidlo);
			copy.setAddressee(Id.asistentVyjazdu);
			startContinualAssistant(copy);
		}
	}

	//meta! sender="AgentPrepravy", id="101", type="Notice"
	public void processPresunVozidlaNaDalsiuZastavku(MessageForm message) {

		message.setAddressee(Id.asistentPresunu);
		startContinualAssistant(message);
	}

	//meta! sender="AsistentVyjazdu", id="160", type="Notice"
	public void processFinishAsistentVyjazdu(MessageForm message) {
		Sprava sprava = (Sprava) message;
		message.setAddressee(mySim().findAgent(Id.agentPrepravy));

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			Vozidlo vozidlo = sprava.getVozidlo();
			mySim().setCoPozastaviloSimulaciu("Pristavenie vozidla " + vozidlo.getIdVozidla() + " k zastávke: " + vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
		}

		message.setCode(Mc.prichodVozidlaNaZastavku);
		notice(message);
	}

	//meta! sender="AsistentPresunu", id="161", type="Notice"
	public void processFinishAsistentPresunu(MessageForm message) {
		Sprava sprava = (Sprava) message;
		message.setAddressee(mySim().findAgent(Id.agentPrepravy));

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			Vozidlo vozidlo = sprava.getVozidlo();
			mySim().setCoPozastaviloSimulaciu("Príchod vozidla " + vozidlo.getIdVozidla() + " k zastávke: " + vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
		}

		message.setCode(Mc.prichodVozidlaNaZastavku);
		notice(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("do defaultu by si nemal dostat spravu");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.presunVozidlaNaDalsiuZastavku:
			processPresunVozidlaNaDalsiuZastavku(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			switch (message.sender().id()) {
			case Id.asistentVyjazdu:
				processFinishAsistentVyjazdu(message);
			break;

			case Id.asistentPresunu:
				processFinishAsistentPresunu(message);
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
	public AgentPohybu myAgent() {
		return (AgentPohybu)super.myAgent();
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}
}
