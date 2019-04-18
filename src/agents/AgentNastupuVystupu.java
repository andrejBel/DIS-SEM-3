package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="106"
public class AgentNastupuVystupu extends Agent {
	public AgentNastupuVystupu(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerNastupuVystupu(Id.managerNastupuVystupu, mySim(), this);
		addOwnMessage(Mc.nastupVystupZakaznika);
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	//meta! tag="end"
}