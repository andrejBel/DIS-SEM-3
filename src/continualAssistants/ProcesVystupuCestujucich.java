package continualAssistants;

import Model.Cestujuci;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Vozidlo;
import Model.Zastavka;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;

//meta! id="336"
public class ProcesVystupuCestujucich extends Scheduler {

	private TriangularRNG _triangularRNG = new TriangularRNG(0.6, 1.2, 4.2, SimulaciaDopravy.GENERATOR_NASAD); // todo uncoment

	public ProcesVystupuCestujucich(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentZastavok", id="339", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();

		Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
		// ratam s tym, ze nejaky cestujuci vystupuje
		if (vozidlo.getCestujuciVoVozidle().size() == 0) {
			throw new RuntimeException("Vo vozidle musi byt aspon 1 cestujuci!!!");
		}

		vozidlo.casZaciakuVystupu = mySim().currentTime();
		while (vozidlo.suVolneDvere() && vozidlo.getPocetCestujucichVAutobuseBezNastupujusichAVystupujucich() > 0) {
			vystupCestujucehoZaciatok(sprava);
		}

	}

	//meta! sender="AgentZastavok", id="343", type="Notice"
	public void processCestujuciVystupil(MessageForm message) {
		Sprava sprava = (Sprava) message;
		vystupCestujucehoKoniec(sprava);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.cestujuciVystupil:
			processCestujuciVystupil(message);
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
	public AgentZastavok myAgent() {
		return (AgentZastavok)super.myAgent();
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	private double getCasVystupuNaZakladeTypuVozidla(TYP_VOZIDLA typVozidla) {
		//return 1.0;
		// TODo uncomment
		if (typVozidla == TYP_VOZIDLA.MINIBUS) {
			return 4.0;
		} else {
			return _triangularRNG.sample();
		}
	}

	private void vystupCestujucehoZaciatok(final Sprava sprava) {
		Sprava copy = (Sprava) sprava.createCopy();
		Vozidlo vozidlo = copy.getVozidlo();
		Integer indexVolnychDveri = vozidlo.obsadPrvyIndexVolnychDveri();
		if (indexVolnychDveri == null) {
			throw new RuntimeException("Dvere musia byt volne!!!");
		}
		if (vozidlo.getCelkovyPocetCestujucichVoVozidle() <= 0) {
			throw new RuntimeException("Vo vozidle musi byt niekto!!!");
		}

		Cestujuci cestujuci = vozidlo.odstranCestujucehoVoVozidle().getCestujuci();
		copy.setCestujuci(cestujuci);
		cestujuci.setStavCestujuci(STAV_CESTUJUCI.VYSTUPUJE_Z_VOZIDLA);
		cestujuci.setIndexVytupnychDveri(indexVolnychDveri);
		vozidlo.pridajCestujucehoNaVystup(copy);

		double casVystupu = getCasVystupuNaZakladeTypuVozidla(vozidlo.getTypVozidla());
		cestujuci.setCasZaciatkuVystupovania(mySim().currentTime());
		cestujuci.setCasKoncaVystupovania(mySim().currentTime() + casVystupu);

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Cestujúci " + cestujuci.getIdCestujuceho() + " vystupuje z vozidla: " + vozidlo.getIdVozidla());
		}

		copy.setCode(Mc.cestujuciVystupil);
		hold(casVystupu, copy);

	}

	private void vystupCestujucehoKoniec(Sprava sprava) {
		Vozidlo vozidlo = sprava.getVozidlo();
		Cestujuci cestujuci = sprava.getCestujuci();
		vozidlo.uvolniPouzivaneDvere(cestujuci.getIndexVystupnychDveri());
		vozidlo.odstranCestujucehoNaVystup(cestujuci.getIdCestujuceho());

		Zastavka zastavka = myAgent().getZastavka(KONSTANTY.STADION);
		zastavka.pridajCestujucehoNaZastavku(sprava);

		cestujuci.setStavCestujuci(STAV_CESTUJUCI.NA_STADIONE);
		myAgent().zvysPocetCestujucichNaStadione(cestujuci.getCasKoncaVystupovania() <= mySim().getCasZaciatkuZapasu());

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Príchod cestujúceho " + cestujuci.getIdCestujuceho() + " na štadión");
		}

		if (vozidlo.getPocetCestujucichVAutobuseBezNastupujusichAVystupujucich() > 0) {
			vystupCestujucehoZaciatok(sprava);
		} else if (vozidlo.getCelkovyPocetCestujucichVoVozidle() == 0) {
			// vsetci vystupili
			vozidlo.casKoncaVystupu = mySim().currentTime();
			//System.out.println("Cas vystupu cestujucich vozidla" +  vozidlo.getIdVozidla() + ": " + vozidlo.getCasVystupovania());

			assistantFinished(sprava);
		}

	}

}