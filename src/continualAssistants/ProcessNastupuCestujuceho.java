package continualAssistants;

import Model.Cestujuci;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Vozidlo;
import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.TriangularRNG;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="221"
public class ProcessNastupuCestujuceho extends Process {

	private TriangularRNG _triangularRNG = new TriangularRNG(0.6, 1.2, 4.2, SimulaciaDopravy.GENERATOR_NASAD); // todo uncomment
	//private TriangularRNG _triangularRNG = new TriangularRNG(60.0, 120.0, 420.0, SimulaciaDopravy.GENERATOR_NASAD); // todo uncoment
	private UniformContinuousRNG _uniformContinuousRNG = new UniformContinuousRNG(6.0, 10.0, SimulaciaDopravy.GENERATOR_NASAD);

	public ProcessNastupuCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentNastupuVystupu", id="239", type="Notice"
	public void processKoniecNastupu(MessageForm message) {
		assistantFinished(message);
	}

	//meta! sender="AgentNastupuVystupu", id="229", type="Notice"
	public void processStart(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();
		Cestujuci cestujuci = sprava.getCestujuci();
		RNG<Double> generator = getGeneratorNaZakaldeTypu(vozidlo.getTypVozidla());

		double casNastupu = generator.sample();
		sprava.setCode(Mc.koniecNastupu);
		cestujuci.setCasZaciatkuNastupovania(mySim().currentTime());
		cestujuci.setCasKoncaNastupovania(mySim().currentTime() + casNastupu);
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			mySim().pridajUdalostCoPozastavilaSimulaciu("Cestujúci " + cestujuci.getIdCestujuceho() + " nastupuje do vozidla: " + vozidlo.getIdVozidla());
		}
		hold(casNastupu, sprava);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("default vetva by nemala nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.start:
			processStart(message);
		break;

		case Mc.koniecNastupu:
			processKoniecNastupu(message);
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

	private RNG<Double> getGeneratorNaZakaldeTypu(TYP_VOZIDLA typVozidla) {
		if (typVozidla == TYP_VOZIDLA.MINIBUS) {
			return _uniformContinuousRNG;
		} else {
			return _triangularRNG;
		}
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}
}
