package agents;

import Model.Zastavka;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import Statistiky.StatNamed;
import simulation.*;
import managers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//meta! id="87"
public class AgentZastavok extends Agent {

	private TreeMap<String, Zastavka> _zastavky = new TreeMap<>();


	private int _pocetCestujucichRep;
	private int _pocetCestujucichNaStadione;
	private int _pocetCestujucichNaStadioneNaCas;
	private StatNamed _priemernyCasCakaniaCestujucehoNaZastavkeRep = new StatNamed("Priemerný čas čakania cestujúceho na zastávke");


	public AgentZastavok(int id, Simulation mySim, Agent parent, TreeMap<String, ZastavkaKonfiguracia> konfiguraciaZastavok) {
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
		_pocetCestujucichNaStadione = 0;
		_pocetCestujucichNaStadioneNaCas = 0;
		_priemernyCasCakaniaCestujucehoNaZastavkeRep.clear();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerZastavok(Id.managerZastavok, mySim(), this);
		addOwnMessage(Mc.prichodCestujucehoNaStadion);
		addOwnMessage(Mc.cestujuciNaZastavke);
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

	public TreeMap<String, Zastavka> getZastavky() {
		return _zastavky;
	}

	public void zvysPocetCestujucich() {
		_pocetCestujucichRep++;
	}

	public int getPocetCestujucichRep() {
		return _pocetCestujucichRep;
	}

	public void zvysPocetCestujucichNaStadione(boolean nacas) {
		_pocetCestujucichNaStadione++;
		if (nacas) {
			_pocetCestujucichNaStadioneNaCas++;
		}
	}

	public int getPocetCestujucichNaStadione() {
		return _pocetCestujucichNaStadione;
	}

	public int getPocetCestujucichNaStadioneNaCas() {
		return _pocetCestujucichNaStadioneNaCas;
	}

	public int getPocetCestujucichNaStadionePoZaciatkuZapasu() {
		return this._pocetCestujucichNaStadione - _pocetCestujucichNaStadioneNaCas;
	}

	public double getPercentoLudiPrichadzajucichPoZapase() {
		int pocetCestujucichPoZaciatkuZapasu = getPocetCestujucichNaStadionePoZaciatkuZapasu();
		return _pocetCestujucichNaStadione > 0 ? (((double) pocetCestujucichPoZaciatkuZapasu) / ((double) _pocetCestujucichNaStadione )) * 100.0 : 0.0;
	}

	public void pridajCasCakaniaCestujucehoNaZastavke(double casCakania) {
		this._priemernyCasCakaniaCestujucehoNaZastavkeRep.addSample(casCakania);
	}

	public StatNamed getPriemernyCasCakaniaCestujucehoNaZastavkeRep() {
		return _priemernyCasCakaniaCestujucehoNaZastavkeRep;
	}



}