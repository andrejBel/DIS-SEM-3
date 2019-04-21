package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="267"
public class AkciaPresunVozidloNaDalsiuZastavku extends Action {
	public AkciaPresunVozidloNaDalsiuZastavku(int id, Simulation mySim, CommonAgent myAgent) {
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
