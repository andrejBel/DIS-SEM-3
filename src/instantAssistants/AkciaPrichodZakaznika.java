package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="263"
public class AkciaPrichodZakaznika extends Action {
	public AkciaPrichodZakaznika(int id, Simulation mySim, CommonAgent myAgent) {
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
