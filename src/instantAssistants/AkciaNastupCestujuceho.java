package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="280"
public class AkciaNastupCestujuceho extends Action {
	public AkciaNastupCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
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
