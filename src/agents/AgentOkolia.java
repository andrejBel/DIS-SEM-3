package agents;

import Model.Enumeracie.TYP_LINKY;
import Model.Linka;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.HashMap;

//meta! id="3"
public class AgentOkolia extends Agent {

	private HashMap<String, ZastavkaKonfiguracia> _zastavky;
	private HashMap<TYP_LINKY, Linka> _linky;

	public AgentOkolia(int id, Simulation mySim, Agent parent, HashMap<String, ZastavkaKonfiguracia> zastavky, HashMap<TYP_LINKY, Linka> linky) {
		super(id, mySim, parent);
		this._zastavky = zastavky;
		this._linky = linky;
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