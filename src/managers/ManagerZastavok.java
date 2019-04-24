package managers;

import Model.Cestujuci;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Vozidlo;
import Model.Zastavka;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import OSPDataStruct.SimQueue;
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

		Cestujuci cestujuci = new Cestujuci(mySim(), _indexerCestujucich++, sprava.getZastavkaKonfiguracie(), mySim().currentTime(), STAV_CESTUJUCI.CAKA_NA_ZASTAVKE);
		sprava.setCestujuci(cestujuci);

		myAgent().getZastavka(sprava.getZastavkaKonfiguracie().getNazovZastavky()).pridajCestujucehoNaZastavku(sprava);
		myAgent().zvysPocetCestujucich();
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation(); //TODO uncomment
			mySim().pridajUdalostCoPozastavilaSimulaciu("Príchod cestujúceho " + cestujuci.getIdCestujuceho() + " na zastávku: " + sprava.getZastavkaKonfiguracie().getNazovZastavky());
		}
		response(sprava);
	}

	//meta! sender="AgentPrepravy", id="258", type="Request"
	public void processCestujuciNaZastavke(MessageForm message) {
		Sprava sprava = (Sprava) message;
		ZastavkaKonfiguracia ktoraZastavka = sprava.getZastavkaKonfiguracie();
		Zastavka zastavka = myAgent().getZastavka(ktoraZastavka.getNazovZastavky());
		Vozidlo vozidlo = sprava.getVozidlo();
		SimQueue<Sprava> cestujuci = zastavka.getCestujuciNaZastavke();
		if (cestujuci.size() == 0) {
			sprava.setCestujuci(null);
		} else {
			Cestujuci prvyCakajuciCestujuci = cestujuci.peek().getCestujuci();
			if (prvyCakajuciCestujuci.jeOchotnyNastupit(vozidlo, mySim().currentTime())) {
				myAgent().pridajCasCakaniaCestujucehoNaZastavke(prvyCakajuciCestujuci.getCasCakaniaNaZastavke());
				zastavka.getPriemernyCasCakaniaCestujecehoNaZastavke().addSample(prvyCakajuciCestujuci.getCasCakaniaNaZastavke());
				sprava.setCestujuci(cestujuci.poll().getCestujuci());
			} else {
				sprava.setCestujuci(null);
			}
		}

		response(sprava);
	}

	//meta! sender="AgentPrepravy", id="319", type="Notice"
	public void processPrichodCestujucehoNaStadion(MessageForm message) {
		Sprava sprava = (Sprava) message;
		myAgent().getZastavka(KONSTANTY.STADION).pridajCestujucehoNaZastavku(sprava);
		Cestujuci cestujuci = sprava.getCestujuci();
		cestujuci.setStavCestujuci(STAV_CESTUJUCI.NA_STADIONE);
		myAgent().zvysPocetCestujucichNaStadione(cestujuci.getCasKoncaVystupovania() <= mySim().getCasZaciatkuZapasu());

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Príchod cestjúceho " + cestujuci.getIdCestujuceho() + " na štadión");
		}
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

			case Mc.prichodCestujucehoNaStadion:
				processPrichodCestujucehoNaStadion(message);
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