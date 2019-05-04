package continualAssistants;

import Model.Cestujuci;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Vozidlo;
import Model.Zastavka;
import OSPABA.*;
import OSPRNG.TriangularRNG;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;

import java.util.function.BiFunction;

//meta! id="325"
public class ProcesNastupuCestujucich extends Scheduler {

	private TriangularRNG _triangularRNG = new TriangularRNG(0.6, 1.2, 4.2, SimulaciaDopravy.GENERATOR_NASAD); // todo uncomment
	private UniformContinuousRNG _uniformContinuousRNG = new UniformContinuousRNG(6.0, 10.0, SimulaciaDopravy.GENERATOR_NASAD);


	public ProcesNastupuCestujucich(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentZastavok", id="328", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();
		Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());

		if (zastavka.getCestujuciNaZastavke().size() == 0) {
			// nemam koho vylozit, koncim
			assistantFinished(sprava);
			return;
		}

		boolean aspon1Nastupil = false;
		while (jeSplnenaPodmienkaPreNastupenie(sprava)) {
			aspon1Nastupil = true;
			nastupCestujucehoZaciatok(sprava);
		}

		if (!aspon1Nastupil) {
			assistantFinished(sprava);
			return;
		}
	}

	//meta! sender="AgentZastavok", id="332", type="Notice"
	public void processCestujuciNastupil(MessageForm message) {
		Sprava sprava = (Sprava) message;

		nastupCestujucehoKoniec(sprava);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.cestujuciNastupil:
			processCestujuciNastupil(message);
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

	public double getCasNastupuNaZakladeTypu(TYP_VOZIDLA typVozidla) {
		//return 1.0;

		//todo uncomment
		if (typVozidla == TYP_VOZIDLA.MINIBUS) {
			return _uniformContinuousRNG.sample();
		} else {
			return _triangularRNG.sample();
		}

	}

	// return cas nastupu
	public void nastupCestujuceho(Sprava sprava, BiFunction<Double, Sprava, Void> holdFunction) {
		Sprava copy = (Sprava) sprava.createCopy();
		Vozidlo vozidlo = copy.getVozidlo();
		Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
		if (zastavka.getCestujuciNaZastavke().size() == 0) {
			throw new RuntimeException("Na zastavke musi niekto byt!!!");
		}

		copy.setCestujuci(zastavka.getCestujuciNaZastavke().poll().getCestujuci());
		Cestujuci cestujuci = copy.getCestujuci();
		if (!cestujuci.jeOchotnyNastupit(vozidlo)) {
			throw new RuntimeException("Cestujuci musi byt ochotny nastupit!!!");
		}
		Integer volneDvere = vozidlo.obsadPrvyIndexVolnychDveri();

		if (volneDvere == null) {
			System.out.println("cur time: " + mySim().currentTime());
			throw new RuntimeException("Dvere musia byt volne");
		}

		if (!vozidlo.jeVolneMiestoVoVozidle()) {
			throw new RuntimeException("Vo vozidle musi byt miesto");
		}
		cestujuci.setIndexNastupnychDveri(volneDvere);
		cestujuci.setVozidlo(vozidlo);
		cestujuci.setStavCestujuci(STAV_CESTUJUCI.NASTUPUJE_DO_VOZIDLA);

		myAgent().pridajCasCakaniaCestujucehoNaZastavke(cestujuci.getCasCakaniaNaZastavke());
		zastavka.getPriemernyCasCakaniaCestujecehoNaZastavke().addSample(cestujuci.getCasCakaniaNaZastavke());

		vozidlo.pridajCestujucehoNaNastup(copy);

		double casNastupu = getCasNastupuNaZakladeTypu(vozidlo.getTypVozidla());
		copy.setCode(Mc.cestujuciNastupil);
		cestujuci.setCasZaciatkuNastupovania(mySim().currentTime());
		cestujuci.setCasKoncaNastupovania(mySim().currentTime() + casNastupu);
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Cestujúci " + cestujuci.getIdCestujuceho() + " nastupuje do vozidla: " + vozidlo.getIdVozidla());
		}
		holdFunction.apply(casNastupu, copy);
	}

	private void nastupCestujucehoZaciatok(Sprava sprava) {
		nastupCestujuceho( sprava, (casNastupu, spravaCopy) -> {
			hold(casNastupu, spravaCopy);
			return null;
		});

	}

	public void cestujuciNastupil(Sprava sprava) {
		Vozidlo vozidlo = sprava.getVozidlo();
		int indexVstupnychDveri = sprava.getCestujuci().getIndexVstupnychDveri();
		vozidlo.uvolniPouzivaneDvere(indexVstupnychDveri);
		Cestujuci cestujuci = sprava.getCestujuci();

		vozidlo.odstranCestujucehoNaNastup(cestujuci.getIdCestujuceho());
		vozidlo.pridajCestujucehoDoVozidla((Sprava) sprava.createCopy());
		cestujuci.setStavCestujuci(STAV_CESTUJUCI.VEZIE_SA_VO_VOZIDLE);
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Cestujúci " + cestujuci.getIdCestujuceho() + " nastúpil do vozidla " + vozidlo.getIdVozidla());
		}
	}

	private void nastupCestujucehoKoniec(Sprava sprava) {
		cestujuciNastupil(sprava);

		Vozidlo vozidlo = sprava.getVozidlo();
		if (jeSplnenaPodmienkaPreNastupenie(sprava)) {
			nastupCestujucehoZaciatok(sprava);
		} else {
			if (vozidlo.getPocetNastupujucichCestujucich() == 0) {
				assistantFinished(sprava);
			}
		}
	}

	public boolean jeSplnenaPodmienkaPreNastupenie(Sprava sprava) {
		Vozidlo vozidlo = sprava.getVozidlo();
		Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
		return  vozidlo.jeVolneMiestoVoVozidle() &&
				vozidlo.suVolneDvere() &&
				zastavka.getCestujuciNaZastavke().size() > 0 &&
				zastavka.getCestujuciNaZastavke().peek().getCestujuci().jeOchotnyNastupit(vozidlo);
	}

}