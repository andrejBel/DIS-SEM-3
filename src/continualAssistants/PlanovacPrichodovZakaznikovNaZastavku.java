package continualAssistants;

import Model.ZastavkaOkolie;
import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;

//meta! id="136"
public class PlanovacPrichodovZakaznikovNaZastavku extends Scheduler {

	ExponentialRNG _exponentialRNG;
	ZastavkaOkolie _zastavkaOkolie;
	private int _pocetVygenerovanychZakaznikov;

	public PlanovacPrichodovZakaznikovNaZastavku(int id, Simulation mySim, CommonAgent myAgent, ZastavkaOkolie zastavkaOkolie) {
		super(id, mySim, myAgent);
		this._zastavkaOkolie = zastavkaOkolie;
		_exponentialRNG = new ExponentialRNG(zastavkaOkolie.getParameterExponencialnehoRozdelenia());
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
		_pocetVygenerovanychZakaznikov = 0;
	}

	//meta! sender="AgentOkolia", id="148", type="Notice"
	public void processStart(MessageForm message) {
		if (mySim().currentTime() != 0.0) {
			throw new RuntimeException("Start musi byt v case 0.0!!!");
		}
		message.setCode(Mc.zacniGenerovat);
		hold(_zastavkaOkolie.getCasZaciatkuGenerovaniaPrichodovCestujucich(), message);
	}

	//meta! sender="AgentOkolia", id="211", type="Notice"
	public void processZacniGenerovat(MessageForm message) {
		message.setCode(Mc.prichodZakaznikaNaZastavku);
		Sprava sprava = (Sprava) message;
		sprava.setZastavkaKonfiguracie(_zastavkaOkolie.getZastavkaKonfiguracia());
		hold(_exponentialRNG.sample(), sprava);
	}

	//meta! sender="AgentOkolia", id="139", type="Notice"
	public void processPrichodZakaznikaNaZastavku(MessageForm message) {
		_pocetVygenerovanychZakaznikov++;
		if (mySim().currentTime() < _zastavkaOkolie.getCasKoncaGenerovaniaPrichodovCestujucich() && _pocetVygenerovanychZakaznikov < _zastavkaOkolie.getZastavkaKonfiguracia().getMaximalnyPocetCestujucich()) {
			Sprava sprava = (Sprava) message.createCopy();
			hold(_exponentialRNG.sample(), sprava);
		}
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
			case Mc.prichodZakaznikaNaZastavku:
				processPrichodZakaznikaNaZastavku(message);
				break;

			case Mc.zacniGenerovat:
				processZacniGenerovat(message);
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
	public AgentOkolia myAgent() {
		return (AgentOkolia)super.myAgent();
	}

}
