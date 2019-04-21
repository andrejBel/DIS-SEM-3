package agents;

import Model.Zastavka;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import Statistiky.StatNamed;
import simulation.*;
import managers.*;

import java.util.HashMap;
import java.util.Map;

//meta! id="87"
public class AgentZastavok extends Agent {

	private HashMap<String, Zastavka> _zastavky = new HashMap<>();

	private int _pocetCestujucichRep;

	public AgentZastavok(int id, Simulation mySim, Agent parent, HashMap<String, ZastavkaKonfiguracia> konfiguraciaZastavok) {
		super(id, mySim, parent);

		for (Map.Entry<String, ZastavkaKonfiguracia> konfiguracia: konfiguraciaZastavok.entrySet()) {
			_zastavky.put(konfiguracia.getKey(), new Zastavka(mySim, konfiguracia.getValue()));
		}

		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();

		_zastavky.forEach((s, zastavka) -> {zastavka.beforeReplication();});
		_pocetCestujucichRep = 0;
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerZastavok(Id.managerZastavok, mySim(), this);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
	}
	//meta! tag="end"

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	public Zastavka getZastavka(String nazovZastavky) {
		return _zastavky.get(nazovZastavky);
	}

	public HashMap<String, Zastavka> getZastavky() {
		return _zastavky;
	}

	public void zvysPocetCestujucich() {
		_pocetCestujucichRep++;;
	}

	public int getPocetCestujucichRep() {
		return _pocetCestujucichRep;
	}


}