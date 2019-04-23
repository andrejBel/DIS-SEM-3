package agents;

import Model.Vozidlo;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import javax.management.relation.RoleUnresolved;
import java.util.HashMap;
import java.util.List;

//meta! id="5"
public class AgentPrepravy extends Agent {

	private AkciaVystupCestujuceho _akciaVystupCestujuceho;
	private AkciaPresunVozidloNaDalsiuZastavku _akciaPresunVozidloNaDalsiuZastavku;
	private AkciaNastupCestujuceho _akciaNastupCestujuceho;
	private AkciaPrichodZakaznika _akciaPrichodZakaznika;
	private NaplanujPresunVozidlaNaZastavku _naplanujPresunVozidlaNaZastavku;

	private HashMap<String, SimQueue<Sprava>> _frontyVozidielCakajucichNaZastavkach = new HashMap<>();

	public AgentPrepravy(int id, Simulation mySim, Agent parent, List<ZastavkaKonfiguracia> zoznamZastavok) {
		super(id, mySim, parent);

		for (ZastavkaKonfiguracia zastavka: zoznamZastavok) {
			_frontyVozidielCakajucichNaZastavkach.put(zastavka.getNazovZastavky(), new SimQueue<>());
		}
		init();
	}


	@Override
	public void prepareReplication() {
		super.prepareReplication();

		_frontyVozidielCakajucichNaZastavkach.forEach((s, frontVozidiel) -> {
			frontVozidiel.clear();
		});
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerPrepravy(Id.managerPrepravy, mySim(), this);
		_akciaVystupCestujuceho = new AkciaVystupCestujuceho(Id.akciaVystupCestujuceho, mySim(), this);
		_akciaPresunVozidloNaDalsiuZastavku =new AkciaPresunVozidloNaDalsiuZastavku(Id.akciaPresunVozidloNaDalsiuZastavku, mySim(), this);
		_akciaNastupCestujuceho = new AkciaNastupCestujuceho(Id.akciaNastupCestujuceho, mySim(), this);
		_akciaPrichodZakaznika = new AkciaPrichodZakaznika(Id.akciaPrichodZakaznika, mySim(), this);
		_naplanujPresunVozidlaNaZastavku = new NaplanujPresunVozidlaNaZastavku(Id.naplanujPresunVozidlaNaZastavku, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.cestujuciNaZastavke);
		addOwnMessage(Mc.nastupCestujuceho);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.vystupCestujuceho);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
		addOwnMessage(Mc.presunVozidlo);
	}
	//meta! tag="end"


	public AkciaVystupCestujuceho getAkciaVystupCestujuceho() {
		return _akciaVystupCestujuceho;
	}

	public AkciaPresunVozidloNaDalsiuZastavku getAkciaPresunVozidloNaDalsiuZastavku() {
		return _akciaPresunVozidloNaDalsiuZastavku;
	}

	public AkciaNastupCestujuceho getAkciaNastupCestujuceho() {
		return _akciaNastupCestujuceho;
	}

	public AkciaPrichodZakaznika getAkciaPrichodZakaznika() {
		return _akciaPrichodZakaznika;
	}

	public NaplanujPresunVozidlaNaZastavku getNaplanujPresunVozidlaNaZastavku() {
		return _naplanujPresunVozidlaNaZastavku;
	}

	public SimQueue<Sprava> getFrontVozidielCakajucichNaZastavke(String nazov) {
		return _frontyVozidielCakajucichNaZastavkach.get(nazov);
	}

	public void vlozVozidloDoFrontuVozidielCakajucichNaZastavke(Sprava spravaSVozidlom) {

		ZastavkaKonfiguracia zastavkaKonfiguracia = spravaSVozidlom.getZastavkaKonfiguracie();
		SimQueue<Sprava> frontVozidiel = this._frontyVozidielCakajucichNaZastavkach.get(zastavkaKonfiguracia.getNazovZastavky());
		for (Sprava sprava: frontVozidiel) {
			if (sprava.getVozidlo().getIdVozidla() == spravaSVozidlom.getVozidlo().getIdVozidla()) {
				throw new RuntimeException("Nemozno vlozit vozidlo, ktore uz caka");
			}
		}
		frontVozidiel.add(spravaSVozidlom);
		spravaSVozidlom.getVozidlo().setVozidloVoFronteVozidielCakajucichNaZastavke(true);
		spravaSVozidlom.getVozidlo().setCasVstupuDoFrontuVozidielNaZastavke(mySim().currentTime());
	}

	public boolean odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(Sprava spravaSVozidlom) {
		ZastavkaKonfiguracia zastavkaKonfiguracia = spravaSVozidlom.getZastavkaKonfiguracie();

		SimQueue<Sprava> frontVozidiel = this._frontyVozidielCakajucichNaZastavkach.get(zastavkaKonfiguracia.getNazovZastavky());
		int indexVozidlaVoFronte = -1;
		int index = 0;
		for (Sprava sprava: frontVozidiel) {
			if (sprava.getVozidlo().getIdVozidla() == spravaSVozidlom.getVozidlo().getIdVozidla()) {
				indexVozidlaVoFronte = index;
				break;
			}
			index++;
		}
		if (indexVozidlaVoFronte != -1) {
			frontVozidiel.remove(indexVozidlaVoFronte);
			spravaSVozidlom.getVozidlo().setVozidloVoFronteVozidielCakajucichNaZastavke(false);
			return true;
		}
		return false;
	}

	public Sprava getPrveVolneVozidloZFrontuVozidielCakajucichNaZastavke(Sprava spravaSoZastavkou) {
		ZastavkaKonfiguracia zastavkaKonfiguracia = spravaSoZastavkou.getZastavkaKonfiguracie();
		SimQueue<Sprava> frontVozidiel = this._frontyVozidielCakajucichNaZastavkach.get(zastavkaKonfiguracia.getNazovZastavky());
		for (Sprava sprava: frontVozidiel) {
			if (sprava.getVozidlo().suVolneDvere() && sprava.getVozidlo().jeVolneMiestoVoVozidle()) {
				return (Sprava) sprava.createCopy();
			}
		}
		return null;
	}

}