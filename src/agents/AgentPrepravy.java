package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="5"
public class AgentPrepravy extends Agent {
	public AgentPrepravy(int id, Simulation mySim, Agent parent) {
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
		new ManagerPrepravy(Id.managerPrepravy, mySim(), this);
		new ProcesNastupuZakaznikov(Id.procesNastupuZakaznikov, mySim(), this);
		addOwnMessage(Mc.zakazniciNastupili);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.start);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.finish);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
		addOwnMessage(Mc.nastupVystupZakaznika);
	}
	//meta! tag="end"
}