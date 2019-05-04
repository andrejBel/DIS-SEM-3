package agents;

import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.Map;
import java.util.TreeMap;

//meta! id="5"
public class AgentPrepravy extends Agent {

	public AgentPrepravy(int id, Simulation mySim, Agent parent, TreeMap<String, ZastavkaKonfiguracia> zastavky) {
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
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.prichodCestujucehoNaZastavku);
	}
	//meta! tag="end"

}