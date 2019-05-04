package continualAssistants;

import Model.Vozidlo;
import Model.Zastavka;
import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="348"
public class PlanovacPresunuVozidlaNaDalsiuZastavku extends Process {
	public PlanovacPresunuVozidlaNaDalsiuZastavku(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentZastavok", id="363", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();

		Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());

		zastavka.vlozVozidloDoFrontuVozidielCakajucichNaZastavke((Sprava) sprava.createCopy());

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu( "Vozidlo " + vozidlo.getIdVozidla() + " čaká na zastávke " + zastavka.getZastavkaKonfiguracia().getNazovZastavky());
		}

		sprava.setCode(Mc.presunVozidlo);
		hold(KONSTANTY.KOLKO_CAKA_PO_NASTUPE_VSETKYCH_CESTUJUCICH, sprava);
	}

	//meta! sender="AgentZastavok", id="376", type="Notice"
	public void processPresunVozidlo(MessageForm message) {
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

		case Mc.presunVozidlo:
			processPresunVozidlo(message);
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