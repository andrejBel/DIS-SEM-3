package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="271"
public class AkciePrichodVozidlaNaZastavku extends Action {
	public AkciePrichodVozidlaNaZastavku(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message) {
	}

	@Override
	public AgentPrepravy myAgent() {
		return (AgentPrepravy)super.myAgent();
	}

}
