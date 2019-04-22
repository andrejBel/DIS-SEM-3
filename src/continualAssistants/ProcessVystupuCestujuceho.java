package continualAssistants;

import Model.Cestujuci;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Vozidlo;
import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="224"
public class ProcessVystupuCestujuceho extends Process {

	private TriangularRNG _triangularRNG = new TriangularRNG(0.6, 1.2, 4.2, SimulaciaDopravy.GENERATOR_NASAD); // todo uncoment
	//private TriangularRNG _triangularRNG = new TriangularRNG(60.0, 120.0, 420.0, SimulaciaDopravy.GENERATOR_NASAD); // TODO remove

	public ProcessVystupuCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentNastupuVystupu", id="230", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();
		Cestujuci cestujuci = sprava.getCestujuci();
		double casVystupu = getCasVystupuNaZakladeTypuVozidla(vozidlo.getTypVozidla());
		cestujuci.setCasZaciatkuVystupovania(mySim().currentTime());
		cestujuci.setCasKoncaVystupovania(mySim().currentTime() + casVystupu);

		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Cestujúci " + cestujuci.getIdCestujuceho() + " vystupuje z vozidla: " + vozidlo.getIdVozidla());
		}

		sprava.setCode(Mc.koniecVystupu);
		hold(casVystupu, sprava);
	}

	//meta! sender="AgentNastupuVystupu", id="235", type="Notice"
	public void processKoniecVystupu(MessageForm message) {
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("default vetva by nemala nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.koniecVystupu:
			processKoniecVystupu(message);
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
	public AgentNastupuVystupu myAgent() {
		return (AgentNastupuVystupu)super.myAgent();
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	private double getCasVystupuNaZakladeTypuVozidla(TYP_VOZIDLA typVozidla) {
		if (typVozidla == TYP_VOZIDLA.MINIBUS) {
			return 4.0;
		} else {
			return _triangularRNG.sample();
		}
	}

}
