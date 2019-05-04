package managers;

import Model.Cestujuci;
import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Vozidlo;
import Model.Zastavka;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.*;

//meta! id="87"
public class ManagerZastavok extends Manager {

	private long _indexerCestujucich;

	public ManagerZastavok(int id, Simulation mySim, Agent myAgent) {
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null) {
			petriNet().clear();
		}
		_indexerCestujucich = 1;
	}

	//meta! sender="AgentPrepravy", id="110", type="Request"
	public void processPrichodVozidlaNaZastavku(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();
		ZastavkaKonfiguracia zastavkaNaKtoruVozidloPrislo = vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka();

		if (zastavkaNaKtoruVozidloPrislo.isVystup()) {
			vozidlo.zvysPocetPrichodovKStadionu();
			if (vozidlo.getCelkovyPocetCestujucichVoVozidle() == 0) {
				posliVozidloNaDalsiuZastavku(sprava);
				return;
			} else {
				sprava.setAddressee(myAgent().getProcesVystupuCestujucich());
				startContinualAssistant(sprava);
				return;
			}
		} else {
			if (vozidlo.jeVolneMiestoVoVozidle()) {
				sprava.setAddressee(myAgent().getProcesNastupuCestujucich());
				startContinualAssistant(sprava);
			} else {
				posliVozidloNaDalsiuZastavku(sprava);
				return;
			}
		}
	}

	//meta! sender="AgentPrepravy", id="98", type="Notice"
	public void processPrichodCestujucehoNaZastavku(MessageForm message) {
		Sprava sprava = (Sprava) message;

		//System.out.println("Cestujuci prisiel na zastavku: " + sprava.getZastavkaKonfiguracie().getNazovZastavky() + ", cas: " + Helper.FormatujSimulacnyCas(mySim().currentTime())); // TODO delete

		Cestujuci cestujuci = new Cestujuci(mySim(), _indexerCestujucich++, sprava.getZastavkaKonfiguracie(), mySim().currentTime(), STAV_CESTUJUCI.CAKA_NA_ZASTAVKE);
		sprava.setCestujuci(cestujuci);

		Zastavka zastavka = myAgent().getZastavka(sprava.getZastavkaKonfiguracie().getNazovZastavky());
		zastavka.pridajCestujucehoNaZastavku(sprava);
		myAgent().zvysPocetCestujucich();
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation(); //TODO uncomment
			mySim().pridajUdalostCoPozastavilaSimulaciu("Príchod cestujúceho " + cestujuci.getIdCestujuceho() + " na zastávku: " + sprava.getZastavkaKonfiguracie().getNazovZastavky());
		}


		Sprava spravaSVozidlomNaZastavke = zastavka.getPrveVolneVozidloZFrontuVozidielCakajucichNaZastavke();
		if (spravaSVozidlomNaZastavke != null) {
			if (!sprava.getZastavkaKonfiguracie().getNazovZastavky().equals(spravaSVozidlomNaZastavke.getZastavkaKonfiguracie().getNazovZastavky())) {
				throw new RuntimeException("Zastavka musi byt rovnaka");
			}
			spravaSVozidlomNaZastavke.setAddressee(myAgent().getProcesNastupCestujuceho());
			startContinualAssistant(spravaSVozidlomNaZastavke);
		}

	}

	//meta! sender="PlanovacPresunuVozidlaNaDalsiuZastavku", id="365", type="Notice"
	public void processFinishPlanovacPresunuVozidlaNaDalsiuZastavku(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();

		if (vozidlo.getPocetNastupujucichCestujucich() == 0) {
			Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());

			if (zastavka.odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(sprava)) {
				vozidloOdchadzaZoZastavkyPoCakani(sprava);
				posliVozidloNaDalsiuZastavku(sprava);
			}
		}

	}

	//meta! sender="ProcesNastupuCestujucich", id="329", type="Notice"
	public void processFinishProcesNastupuCestujucich(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();

		if (vozidlo.getPocetNastupujucichCestujucich() != 0) {
			throw new RuntimeException("Musia byt vsetci nastupeni!!!");
		}
		if (vozidlo.jeVolneMiestoVoVozidle()) {
			if (mySim().getKonfiguraciaVozidiel().getPrevadzkaLiniek() == PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA || !vozidlo.getTypVozidla().isAutobus()) { // hned po nastupeni odchadza
				posliVozidloNaDalsiuZastavku(sprava);
				return;
			} else { // 1,5 minuty caka, 90s // inak
				if (vozidlo.isVozidloVoFronteVozidielCakajucichNaZastavke()) {
					throw new RuntimeException("Vozidlo nemoze byt vo fronte!!!");
				} else {
					message.setAddressee(myAgent().getPlanovacPresunuVozidlaNaDalsiuZastavku());
					startContinualAssistant(message);
				}
			}
		} else {
			posliVozidloNaDalsiuZastavku(sprava);
			return;
		}
		//posliVozidloNaDalsiuZastavku((Sprava) message);
	}

	//meta! sender="ProcesVystupuCestujucich", id="340", type="Notice"
	public void processFinishProcesVystupuCestujucich(MessageForm message) {
		if (mySim().isKoniecReplikacie()) {
			if (mySim().isKrokovanie()) {
				mySim().pauseSimulation();
				mySim().pridajUdalostCoPozastavilaSimulaciu("Koniec simulácie");
			}
			mySim().finishReplication();
		}
		posliVozidloNaDalsiuZastavku((Sprava) message);
	}

	//meta! sender="ProcesNastupCestujuceho", id="372", type="Notice"
	public void processFinishProcesNastupCestujuceho(MessageForm message) {
		Sprava sprava = (Sprava) message;
		Vozidlo vozidlo = sprava.getVozidlo();
		if (vozidlo.getPocetNastupujucichCestujucich() == 0) {
			Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
			if (!vozidlo.jeVolneMiestoVoVozidle()) {
				boolean odstranene = zastavka.odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(sprava);
				if (odstranene == false) {
					throw new RuntimeException("Vozidlo musi byt odstranene z frontu!!!");
				}
				vozidloOdchadzaZoZastavkyPoCakani(sprava);
				posliVozidloNaDalsiuZastavku(sprava);
				return;
			} else {
				double timeDifference = mySim().currentTime() - vozidlo.getCasVstupuDoFrontuVozidielNaZastavke();
				if (timeDifference >= KONSTANTY.KOLKO_CAKA_PO_NASTUPE_VSETKYCH_CESTUJUCICH) { // todo > ? >=
					boolean odstranene = zastavka.odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(sprava);
					if (odstranene == false) {
						throw new RuntimeException("Vozidlo musi byt odstranene z frontu!!!");
					}
					vozidloOdchadzaZoZastavkyPoCakani(sprava);
					posliVozidloNaDalsiuZastavku(sprava);
				}
			}
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Default vetva by nemala nikdy nastat");
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
			case Mc.finish:
				switch (message.sender().id()) {
					case Id.procesNastupCestujuceho:
						processFinishProcesNastupCestujuceho(message);
						break;

					case Id.procesVystupuCestujucich:
						processFinishProcesVystupuCestujucich(message);
						break;

					case Id.procesNastupuCestujucich:
						processFinishProcesNastupuCestujucich(message);
						break;

					case Id.planovacPresunuVozidlaNaDalsiuZastavku:
						processFinishPlanovacPresunuVozidlaNaDalsiuZastavku(message);
						break;
				}
				break;

			case Mc.prichodVozidlaNaZastavku:
				processPrichodVozidlaNaZastavku(message);
				break;

			case Mc.prichodCestujucehoNaZastavku:
				processPrichodCestujucehoNaZastavku(message);
				break;

			default:
				processDefault(message);
				break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentZastavok myAgent() {
		return (AgentZastavok)super.myAgent();
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	public void posliVozidloNaDalsiuZastavku(Sprava spravaSRequestom) {
		spravaSRequestom.setCode(Mc.prichodVozidlaNaZastavku);
		response(spravaSRequestom);
	}

	private void vozidloOdchadzaZoZastavkyPoCakani(Sprava sprava) {
		if (mySim().isKrokovanie()) {
			mySim().pauseSimulation();
			Vozidlo vozidlo = sprava.getVozidlo();
			Zastavka zastavka = myAgent().getZastavka(vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky());
			mySim().pridajUdalostCoPozastavilaSimulaciu( "Vozidlo " + vozidlo.getIdVozidla() + " prestalo čakať na zastávke " + zastavka.getZastavkaKonfiguracia().getNazovZastavky());
		}
	}

}