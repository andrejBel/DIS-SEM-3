package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="192"
public class Query1 extends Query {
	public Query1(int id, Simulation mySim, CommonAgent myAgent) {
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
