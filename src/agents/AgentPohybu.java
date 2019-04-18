package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="86"
public class AgentPohybu extends Agent {
	public AgentPohybu(int id, Simulation mySim, Agent parent) {
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
		new ManagerPohybu(Id.managerPohybu, mySim(), this);
		new AsistentPresunu(Id.asistentPresunu, mySim(), this);
		new AsistentVyjazdu(Id.asistentVyjazdu, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.presunVozidlaNaDalsiuZastavku);
		addOwnMessage(Mc.start);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.finish);
	}

	public void inizializujVozidla() {
		
	}

	//meta! tag="end"
}