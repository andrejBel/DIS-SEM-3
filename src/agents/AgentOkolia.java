package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="3"
public class AgentOkolia extends Agent {
	public AgentOkolia(int id, Simulation mySim, Agent parent) {
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
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovacPrichodovZakaznikovNaZastavku(Id.planovacPrichodovZakaznikovNaZastavku, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	//meta! tag="end"
}