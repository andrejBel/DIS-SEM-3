package agents;

import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.HashMap;

//meta! id="87"
public class AgentZastavok extends Agent {

	private HashMap<String, ZastavkaKonfiguracia> _zastavkyKonfiguracia;

	public AgentZastavok(int id, Simulation mySim, Agent parent, HashMap<String, ZastavkaKonfiguracia> konfiguraciaZastavok) {
		super(id, mySim, parent);
		this._zastavkyKonfiguracia = konfiguraciaZastavok;
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerZastavok(Id.managerZastavok, mySim(), this);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	//meta! tag="end"
}