package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="277"
public class AkciaVystupCestujuceho extends Action {
	public AkciaVystupCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
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
