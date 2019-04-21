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
		new AkciaVystupCestujuceho(Id.akciaVystupCestujuceho, mySim(), this);
		new AkciaPresunVozidloNaDalsiuZastavku(Id.akciaPresunVozidloNaDalsiuZastavku, mySim(), this);
		new VlozVozidloDoFrontuVozidielCakajucichNaZastavke(Id.vlozVozidloDoFrontuVozidielCakajucichNaZastavke, mySim(), this);
		new AkciaNastupCestujuceho(Id.akciaNastupCestujuceho, mySim(), this);
		new AkciePrichodVozidlaNaZastavku(Id.akciePrichodVozidlaNaZastavku, mySim(), this);
		new VyberVozidloZFrontuVozidielCakajucichNaZastavke(Id.vyberVozidloZFrontuVozidielCakajucichNaZastavke, mySim(), this);
		new AkciaPrichodZakaznika(Id.akciaPrichodZakaznika, mySim(), this);
		new ProcesNastupuZakaznikov(Id.procesNastupuZakaznikov, mySim(), this); // TODO REMOVE
		new NaplanujPresunVozidlaNaZastavku(Id.naplanujPresunVozidlaNaZastavku, mySim(), this);
		addOwnMessage(Mc.zakazniciNastupili); // TODO REMOVE
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.cestujuciNaZastavke);
		addOwnMessage(Mc.nastupCestujuceho);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.vystupCestujuceho);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
		addOwnMessage(Mc.presunVozidlo);
	}
	//meta! tag="end"



}