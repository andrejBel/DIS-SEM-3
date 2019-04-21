package managers;

import Model.Cestujuci;
import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="87"
public class ManagerZastavok extends Manager {

	private long _indexerCestujucich;

	public ManagerZastavok(int id, Simulation mySim, Agent myAgent) {
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
		_indexerCestujucich = 1;
	}

	//meta! sender="AgentPrepravy", id="110", type="Request"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
	}

	//meta! sender="AgentPrepravy", id="98", type="Notice"
	public void processPrichodZakaznikaNaZastavku(MessageForm message) {
		Sprava sprava = (Sprava) message;

		//System.out.println("Cestujuci prisiel na zastavku: " + sprava.getZastavkaKonfiguracie().getNazovZastavky() + ", cas: " + Helper.FormatujSimulacnyCas(mySim().currentTime())); // TODO delete

		Cestujuci cestujuci = new Cestujuci(_indexerCestujucich++, sprava.getZastavkaKonfiguracie(), mySim().currentTime());
		sprava.setCestujuci(cestujuci);

		myAgent().getZastavka(sprava.getZastavkaKonfiguracie().getNazovZastavky()).pridajCestujucehoNaZastavku(sprava);
		myAgent().zvysPocetCestujucich();
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().setCoPozastaviloSimulaciu("Príchod cestujúceho " + cestujuci.getIdCestujuceho() + " na zastávku: " + sprava.getZastavkaKonfiguracie().getNazovZastavky());
		}
	}

	//meta! sender="AgentPrepravy", id="258", type="Request"
	public void processCestujuciNaZastavke(MessageForm message) {
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
			case Mc.prichodZakaznikaNaZastavku:
				processPrichodZakaznikaNaZastavku(message);
				break;

			case Mc.cestujuciNaZastavke:
				processCestujuciNaZastavke(message);
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