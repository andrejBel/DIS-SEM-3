package agents;

import Model.Enumeracie.TYP_LINKY;
import Model.Linka;
import Model.ZastavkaKonfiguracia;
import Model.ZastavkaLinky;
import Model.ZastavkaOkolie;
import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//meta! id="3"
public class AgentOkolia extends Agent {

	private ArrayList<ZastavkaOkolie> _zastavkyOkolia = new ArrayList<>();
	private ArrayList<PlanovacPrichodovZakaznikovNaZastavku> _planovacovePrichodov = new ArrayList<>();


	public AgentOkolia(int id, Simulation mySim, Agent parent, HashMap<String, ZastavkaKonfiguracia> zastavky, HashMap<TYP_LINKY, Linka> linky) {
		super(id, mySim, parent);

		double casZaciatkuZapasu = mySim().getCasZaciatkuZapasu();
		for (Map.Entry<String, ZastavkaKonfiguracia> zastavaEntry: zastavky.entrySet()) {
			ZastavkaKonfiguracia zastavka = zastavaEntry.getValue();
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
			_zastavkyOkolia.add(new ZastavkaOkolie(zastavka, casPrichoduPrvehoZakaznika, casPrichoduPoslednehoZakaznika));
			System.out.println("Zastavka: " + zastavka.getNazovZastavky());
			System.out.println("casPrichoduPrvehoZakaznika: " + casPrichoduPrvehoZakaznika);
			System.out.println("casPrichoduPoslednehoZakaznika: " + casPrichoduPoslednehoZakaznika);
			System.out.println("Rozdiel: " + (casPrichoduPoslednehoZakaznika - casPrichoduPrvehoZakaznika));
			System.out.println("Parameter: " + _zastavkyOkolia.get(_zastavkyOkolia.size() - 1).getParameterExponencialnehoRozdelenia());
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

		int pociatocneId = Id.pociatocneIdPrePlanovacPrichodovZakaznikov_;
		for (ZastavkaOkolie zastavkaOkolie: _zastavkyOkolia) {
			_planovacovePrichodov.add(new PlanovacPrichodovZakaznikovNaZastavku(pociatocneId, mySim(), this, zastavkaOkolie));
			pociatocneId++;
			//break; // TODO remove, iba na test
		}
		//new PlanovacPrichodovZakaznikovNaZastavku(Id.planovacPrichodovZakaznikovNaZastavku, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.zacniGenerovat);
		addOwnMessage(Mc.prichodZakaznikaNaZastavku);
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	public ArrayList<PlanovacPrichodovZakaznikovNaZastavku> getIdPlanovacovePrichodov() {
		return _planovacovePrichodov;
	}

	//meta! tag="end"
}