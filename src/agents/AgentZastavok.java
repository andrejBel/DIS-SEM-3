package agents;

import Model.Zastavka;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import Statistiky.StatNamed;
import continualAssistants.PlanovacPresunuVozidlaNaDalsiuZastavku;
import continualAssistants.ProcesNastupCestujuceho;
import continualAssistants.ProcesNastupuCestujucich;
import continualAssistants.ProcesVystupuCestujucich;
import simulation.*;
import managers.*;

import java.util.Map;
import java.util.TreeMap;

//meta! id="87"
public class AgentZastavok extends Agent {

	private PlanovacPresunuVozidlaNaDalsiuZastavku _planovacPresunuVozidlaNaDalsiuZastavku;
	private ProcesNastupCestujuceho _procesNastupCestujuceho;
	private ProcesNastupuCestujucich _procesNastupuCestujucich;
	private ProcesVystupuCestujucich _procesVystupuCestujucich;

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
		_planovacPresunuVozidlaNaDalsiuZastavku = new PlanovacPresunuVozidlaNaDalsiuZastavku(Id.planovacPresunuVozidlaNaDalsiuZastavku, mySim(), this);
		_procesNastupCestujuceho = new ProcesNastupCestujuceho(Id.procesNastupCestujuceho, mySim(), this);
		_procesNastupuCestujucich = new ProcesNastupuCestujucich(Id.procesNastupuCestujucich, mySim(), this);
		_procesVystupuCestujucich = new ProcesVystupuCestujucich(Id.procesVystupuCestujucich, mySim(), this);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.cestujuciVystupil);
		addOwnMessage(Mc.prichodCestujucehoNaZastavku);
		addOwnMessage(Mc.presunVozidlo);
		addOwnMessage(Mc.cestujuciNastupil);
		addOwnMessage(Mc.prichodCestujucehoNaZastavku);
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

	public PlanovacPresunuVozidlaNaDalsiuZastavku getPlanovacPresunuVozidlaNaDalsiuZastavku() {
		return _planovacPresunuVozidlaNaDalsiuZastavku;
	}

	public ProcesNastupCestujuceho getProcesNastupCestujuceho() {
		return _procesNastupCestujuceho;
	}

	public ProcesNastupuCestujucich getProcesNastupuCestujucich() {
		return _procesNastupuCestujucich;
	}

	public ProcesVystupuCestujucich getProcesVystupuCestujucich() {
		return _procesVystupuCestujucich;
	}
}