package agents;

import Model.*;
import Model.Enumeracie.TYP_LINKY;
import OSPABA.*;
import Utils.Helper;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//meta! id="3"
public class AgentOkolia extends Agent {

	private TreeMap<String, ZastavkaOkolie> _zastavkyOkolia = new TreeMap<>();
	private ArrayList<PlanovacPrichodovCestujucichNaZastavku> _planovacovePrichodov = new ArrayList<>();


	public AgentOkolia(int id, Simulation mySim, Agent parent, TreeMap<String, ZastavkaKonfiguracia> zastavky, TreeMap<TYP_LINKY, Linka> linky) {
		super(id, mySim, parent);

		double casZaciatkuZapasu = mySim().getCasZaciatkuZapasu();
		for (Map.Entry<String, ZastavkaKonfiguracia> zastavaEntry: zastavky.entrySet()) {
			ZastavkaKonfiguracia zastavka = zastavaEntry.getValue();
			if (zastavka.getNazovZastavky().equals(KONSTANTY.STADION)) {
				continue;
			}

			double casPrichoduPrvehoZakaznika = Double.MAX_VALUE;
			double casPrichoduPoslednehoZakaznika = 0;

			for (Map.Entry<TYP_LINKY, Linka> linkaEntry : linky.entrySet()) {
				Linka linka = linkaEntry.getValue();

				for (ZastavkaLinky zastavkaLinky: linka.getZastavky()) {
					if (zastavkaLinky.getZastavka() == zastavka) {
						 double T = zastavkaLinky.getVzdialenostKStadionu();
						casPrichoduPrvehoZakaznika = Math.min(casPrichoduPrvehoZakaznika, casZaciatkuZapasu - T - KONSTANTY.NAJSKORSI_PRICHOD_NA_ZASTAVKU);
						casPrichoduPoslednehoZakaznika = Math.max(casPrichoduPoslednehoZakaznika, casZaciatkuZapasu - T - KONSTANTY.NAJNESKORSI_PRICHOD_NA_ZASTAVKU);
					}
				}

			}
			_zastavkyOkolia.put(zastavka.getNazovZastavky(), new ZastavkaOkolie(zastavka, casPrichoduPrvehoZakaznika, casPrichoduPoslednehoZakaznika));

			System.out.print("Zastavka; "+ zastavka.getNazovZastavky() + Helper.DEFAULT_SEPARATOR + " ");
			System.out.print("casPrichoduPrvehoZakaznika; " + casPrichoduPrvehoZakaznika + Helper.DEFAULT_SEPARATOR + " ");
			System.out.print("casPrichoduPoslednehoZakaznika; " + casPrichoduPoslednehoZakaznika + Helper.DEFAULT_SEPARATOR + " ");
			System.out.print("Rozdiel; " + (casPrichoduPoslednehoZakaznika - casPrichoduPrvehoZakaznika) + Helper.DEFAULT_SEPARATOR + " ");
			System.out.print("Pocet zakaznikov; " + zastavka.getMaximalnyPocetCestujucich() + Helper.DEFAULT_SEPARATOR + " ");
			System.out.println("Parameter; " + _zastavkyOkolia.get(zastavka.getNazovZastavky()).getParameterExponencialnehoRozdelenia() + Helper.DEFAULT_SEPARATOR + " ");
		}

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

		int pociatocneId = Id.pociatocneIdPrePlanovacPrichodovCestujucich_;
		for (Map.Entry<String, ZastavkaOkolie> zastavkaOkolieEntry: _zastavkyOkolia.entrySet()) {
			ZastavkaOkolie zastavkaOkolie = zastavkaOkolieEntry.getValue();
			_planovacovePrichodov.add(new PlanovacPrichodovCestujucichNaZastavku(pociatocneId, mySim(), this, zastavkaOkolie));
			pociatocneId++;
		}
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.zacniGenerovat);
		addOwnMessage(Mc.prichodCestujucehoNaZastavku);
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	public ArrayList<PlanovacPrichodovCestujucichNaZastavku> getPlanovacovePrichodov() {
		return _planovacovePrichodov;
	}

	public TreeMap<String, ZastavkaOkolie> getZastavkyOkolia() {
		return _zastavkyOkolia;
	}

	//meta! tag="end"
}