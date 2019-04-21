package simulation;

import Model.*;
import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.*;
import OSPABA.*;
import OSPStat.Stat;
import Statistiky.StatNamed;
import Statistiky.StatistikaInfo;
import Utils.Helper;
import agents.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class SimulaciaDopravy extends Simulation {

    private List<ZastavkaKonfiguracia> _zoznamZastavok = new ArrayList<>();
    private HashMap<String, ZastavkaKonfiguracia> _zastavkyKonfiguracia = new HashMap<>();
    private KonfiguraciaVozidiel _konfiguraciaVozidiel = new KonfiguraciaVozidiel(null, null);
	private HashMap<TYP_LINKY, Linka> _linky = new HashMap<>();

    private boolean _krokovanie = false;
    private String _coPozastaviloSimulaciu;
	private double _casZaciatkuZapasu;


	// GLOBALNE STATISTIKY
	private StatNamed pocetCestujucichSim = new StatNamed("Počet cestujúcich");

	private List<Stat> _simStats = Arrays.asList(
			pocetCestujucichSim
	);


	public SimulaciaDopravy() {
	    init();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
        _zoznamZastavok = Arrays.asList(
				new ZastavkaKonfiguracia("AA",123),
				new ZastavkaKonfiguracia("AB",92),
				new ZastavkaKonfiguracia("AC",241),
				new ZastavkaKonfiguracia("AD",123),
				new ZastavkaKonfiguracia("AE",215),
				new ZastavkaKonfiguracia("AF",245),
				new ZastavkaKonfiguracia("AG",137),
				new ZastavkaKonfiguracia("AH",132),
				new ZastavkaKonfiguracia("AI",164),
				new ZastavkaKonfiguracia("AJ",124),
				new ZastavkaKonfiguracia("AK",213),
				new ZastavkaKonfiguracia("AL",185),
				new ZastavkaKonfiguracia("BA",79),
				new ZastavkaKonfiguracia("BB",69),
				new ZastavkaKonfiguracia("BC",43),
				new ZastavkaKonfiguracia("BD",127),
				new ZastavkaKonfiguracia("BE",30),
				new ZastavkaKonfiguracia("BF",69),
				new ZastavkaKonfiguracia("BG",162),
				new ZastavkaKonfiguracia("BH",90),
				new ZastavkaKonfiguracia("BI",148),
				new ZastavkaKonfiguracia("BJ",171),
				new ZastavkaKonfiguracia("CA",240),
				new ZastavkaKonfiguracia("CB",310),
				new ZastavkaKonfiguracia("CC",131),
				new ZastavkaKonfiguracia("CD",190),
				new ZastavkaKonfiguracia("CE",132),
				new ZastavkaKonfiguracia("CF",128),
				new ZastavkaKonfiguracia("CG",70),
				new ZastavkaKonfiguracia("K1",260),
                new ZastavkaKonfiguracia("K2",210),
                new ZastavkaKonfiguracia("K3",220),
                new ZastavkaKonfiguracia(KONSTANTY.STADION, 0)
        );

        for (ZastavkaKonfiguracia zastavkaKonfiguracia : _zoznamZastavok) {
            _zastavkyKonfiguracia.put(zastavkaKonfiguracia.getNazovZastavky(), zastavkaKonfiguracia);
        }
        ZastavkaKonfiguracia stadion = _zastavkyKonfiguracia.get(KONSTANTY.STADION);
        stadion.setVystupnaZastavka();

		Linka linkaA = new Linka(Arrays.asList(
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AA"), 3.2 * 60.0, _zastavkyKonfiguracia.get("AB")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AB"), 2.3 * 60.0, _zastavkyKonfiguracia.get("AC")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AC"), 2.1 * 60.0, _zastavkyKonfiguracia.get("AD")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AD"), 1.2 * 60.0, _zastavkyKonfiguracia.get("K1")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K1"), 5.4 * 60.0, _zastavkyKonfiguracia.get("AE")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AE"), 2.9 * 60.0, _zastavkyKonfiguracia.get("AF")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AF"), 3.4 * 60.0, _zastavkyKonfiguracia.get("AG")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AG"), 1.8 * 60.0, _zastavkyKonfiguracia.get("K3")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K3"), 4.0 * 60.0, _zastavkyKonfiguracia.get("AH")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AH"), 1.6 * 60.0, _zastavkyKonfiguracia.get("AI")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AI"), 4.6 * 60.0, _zastavkyKonfiguracia.get("AJ")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AJ"), 3.4 * 60.0, _zastavkyKonfiguracia.get("AK")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AK"), 1.2 * 60.0, _zastavkyKonfiguracia.get("AL")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AL"), 0.9 * 60.0, _zastavkyKonfiguracia.get(KONSTANTY.STADION)),
				new ZastavkaLinky(_zastavkyKonfiguracia.get(KONSTANTY.STADION), 25 * 60.0, _zastavkyKonfiguracia.get("AA"))
		), TYP_LINKY.LINKA_A);

		Linka linkaB = new Linka(Arrays.asList(
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BA"), 1.2 * 60.0, _zastavkyKonfiguracia.get("BB")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BB"), 2.3 * 60.0, _zastavkyKonfiguracia.get("BC")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BC"), 3.2 * 60.0, _zastavkyKonfiguracia.get("BD")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BD"), 4.3 * 60.0, _zastavkyKonfiguracia.get("K2")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K2"), 1.2 * 60.0, _zastavkyKonfiguracia.get("BE")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BE"), 2.7 * 60.0, _zastavkyKonfiguracia.get("BF")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BF"), 3.0 * 60.0, _zastavkyKonfiguracia.get("K3")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K3"), 6.0 * 60.0, _zastavkyKonfiguracia.get("BG")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BG"), 4.3 * 60.0, _zastavkyKonfiguracia.get("BH")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BH"), 0.5 * 60.0, _zastavkyKonfiguracia.get("BI")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BI"), 2.7 * 60.0, _zastavkyKonfiguracia.get("BJ")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BJ"), 1.3 * 60.0, _zastavkyKonfiguracia.get(KONSTANTY.STADION)),
				new ZastavkaLinky(_zastavkyKonfiguracia.get(KONSTANTY.STADION), 10 * 60.0, _zastavkyKonfiguracia.get("BA"))
		), TYP_LINKY.LINKA_B);

		Linka linkaC = new Linka(Arrays.asList(
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CA"), 0.6 * 60.0, _zastavkyKonfiguracia.get("CB")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CB"), 2.3 * 60.0, _zastavkyKonfiguracia.get("K1")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K1"), 4.1 * 60.0, _zastavkyKonfiguracia.get("K2")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K2"), 6 * 60.0, _zastavkyKonfiguracia.get("CC")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CC"), 2.3 * 60.0, _zastavkyKonfiguracia.get("CD")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CD"), 7.1 * 60.0, _zastavkyKonfiguracia.get("CE")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CE"), 4.8 * 60.0, _zastavkyKonfiguracia.get("CF")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CF"), 3.7 * 60.0, _zastavkyKonfiguracia.get("CG")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CG"), 7.2 * 60.0, _zastavkyKonfiguracia.get(KONSTANTY.STADION)),
				new ZastavkaLinky(_zastavkyKonfiguracia.get(KONSTANTY.STADION), 30 * 60.0, _zastavkyKonfiguracia.get("CA"))
		), TYP_LINKY.LINKA_C);

		_linky.put(linkaA.getTypLinky(), linkaA);
		_linky.put(linkaB.getTypLinky(), linkaB);
		_linky.put(linkaC.getTypLinky(), linkaC);

		// najdi zastavku, co je najdalej od stadiona
		double najdlhsiCasPresunuZoZastavkyNaStadiona = 0;
		for (Map.Entry<TYP_LINKY, Linka> infoOLinke: _linky.entrySet()) {
			double vzdialenostKStadionu = 0.0;
			ArrayList<ZastavkaLinky> zastavkyLinky = infoOLinke.getValue().getZastavky();
			System.out.println(infoOLinke.getKey().getNazovLinky());
			for (int indexLinky = zastavkyLinky.size() - 2; indexLinky >= 0; indexLinky--) { // ignorujem stadion
				vzdialenostKStadionu += zastavkyLinky.get(indexLinky).getCasPresunuNaDalsiuZastavku();
				zastavkyLinky.get(indexLinky).setVzdialenostKStadionu(vzdialenostKStadionu);
			}
			najdlhsiCasPresunuZoZastavkyNaStadiona = Math.max(najdlhsiCasPresunuZoZastavkyNaStadiona, vzdialenostKStadionu);
		}
		this._casZaciatkuZapasu = najdlhsiCasPresunuZoZastavkyNaStadiona + KONSTANTY.NAJSKORSI_PRICHOD_NA_ZASTAVKU;
		System.out.println("Cas zaciatku zapasu: " + this._casZaciatkuZapasu);


		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu(), _zastavkyKonfiguracia, _linky));
		setAgentPrepravy(new AgentPrepravy(Id.agentPrepravy, this, agentModelu()));
		setAgentPohybu(new AgentPohybu(Id.agentPohybu, this, agentPrepravy(), _linky));
		setAgentZastavok(new AgentZastavok(Id.agentZastavok, this, agentPrepravy(), _zastavkyKonfiguracia));
		setAgentNastupuVystupu(new AgentNastupuVystupu(Id.agentNastupuVystupu, this, agentPrepravy()));

	}

	private AgentModelu _agentModelu;

public AgentModelu agentModelu()
	{ return _agentModelu; }

	public void setAgentModelu(AgentModelu agentModelu)
	{_agentModelu = agentModelu; }

	private AgentOkolia _agentOkolia;

public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public void setAgentOkolia(AgentOkolia agentOkolia)
	{_agentOkolia = agentOkolia; }

	private AgentPrepravy _agentPrepravy;

public AgentPrepravy agentPrepravy()
	{ return _agentPrepravy; }

	public void setAgentPrepravy(AgentPrepravy agentPrepravy)
	{_agentPrepravy = agentPrepravy; }

	private AgentPohybu _agentPohybu;

public AgentPohybu agentPohybu()
	{ return _agentPohybu; }

	public void setAgentPohybu(AgentPohybu agentPohybu)
	{_agentPohybu = agentPohybu; }

	private AgentZastavok _agentZastavok;

public AgentZastavok agentZastavok()
	{ return _agentZastavok; }

	public void setAgentZastavok(AgentZastavok agentZastavok)
	{_agentZastavok = agentZastavok; }

	private AgentNastupuVystupu _agentNastupuVystupu;

public AgentNastupuVystupu agentNastupuVystupu()
	{ return _agentNastupuVystupu; }

	public void setAgentNastupuVystupu(AgentNastupuVystupu agentNastupuVystupu)
	{_agentNastupuVystupu = agentNastupuVystupu; }

	@Override
	public void stopSimulation() {
		super.stopSimulation();
		this.resumeSimulation();
	}

	@Override
	public void pauseSimulation() {
		if (this.isRunning()) {
			super.pauseSimulation();
		}
	}

	@Override
	public void resumeSimulation() {
		if (this.isPaused()) {
			super.resumeSimulation();
		}
	}

	// odtialto pises svoj vlastny kod


    public SimulaciaDopravy setKrokovanie(boolean krokovanie) {
        this._krokovanie = krokovanie;
        return this;
    }

    public boolean isKrokovanie() {
        return this._krokovanie;
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();

        if (_konfiguraciaVozidiel.getKonfiguraciaVozidiel() == null) {
			setKonfiguracia(
					new KonfiguraciaVozidiel(PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA, new ArrayList<>(Arrays.asList(

							new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_C, 90.0)
					)))
			);
		}

        agentPohybu().inizializujVozidla(_konfiguraciaVozidiel.getKonfiguraciaVozidiel());

		_simStats.forEach(Stat::clear);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...
        _agentModelu.spustiSimulaciu();

    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        super.replicationFinished();

        pocetCestujucichSim.addSample(_agentZastavok.getPocetCestujucichRep());
    }

    @Override
    public void simulationFinished() {
        // Dysplay simulation results
        super.simulationFinished();
    }

    public void setKonfiguracia(KonfiguraciaVozidiel konfiguraciaVozidiel) {
        this._konfiguraciaVozidiel = konfiguraciaVozidiel;
    }

    public KonfiguraciaVozidiel getKonfiguraciaVozidiel() {
		return _konfiguraciaVozidiel;
	}

    public List<ZastavkaKonfiguracia> getZoznamZastavok() {
        return _zoznamZastavok;
    }

    public void setCoPozastaviloSimulaciu(String coPozastaviloSimulaciu) {
        this._coPozastaviloSimulaciu = coPozastaviloSimulaciu;
    }

    public String getCoPozastaviloSimulaciu() {
        return _coPozastaviloSimulaciu;
    }

	public BehReplikacieInfo getStatistikyVRamciPreplikacie() {
		BehReplikacieInfo info = new BehReplikacieInfo();
		ObservableList<StatistikaInfo> statistiky = info.statistiky_;

		statistiky.add(new StatistikaInfo("Číslo replikácie", (this.currentReplication() + 1) + ""));
		statistiky.add(new StatistikaInfo("Čas replikácie", Helper.FormatujSimulacnyCas(this.currentTime(), false)));
		statistiky.add(new StatistikaInfo("Počet cestujúcich", String.valueOf(_agentZastavok.getPocetCestujucichRep()) ));
		if (_krokovanie) {
			statistiky.add(new StatistikaInfo("Čo pozastavilo simuláciu", _coPozastaviloSimulaciu));
		}

		ObservableList<VozidloInfo> vozidlaStatistiky = info.vozidlaInfo_;
		for (Vozidlo vozidlo: agentPohybu().getVozidla()) {
			vozidlaStatistiky.add(vozidlo.getVozidloInfo());
		}

		HashMap<String, ObservableList<CestujuciInfo>> cestujuciInfo = info.cestujuciInfo_;

		for (Map.Entry<String, Zastavka> zastavkaEntry: _agentZastavok.getZastavky().entrySet()) {
			String nazovZastavky = zastavkaEntry.getKey();
			Zastavka zastavka =  zastavkaEntry.getValue();
			ObservableList<CestujuciInfo> infoOCestujucichNaZastavke = FXCollections.observableArrayList();
			for (Sprava sprava: zastavka.getCestujuciNaZastavke()) {
				Cestujuci cestujuci = sprava.getCestujuci();
				infoOCestujucichNaZastavke.add(cestujuci.getCestujuciInfo());
			}
			cestujuciInfo.put(nazovZastavky, infoOCestujucichNaZastavke);
		}

		return info;
	}

	public BehSimulacieInfo getStatistikySimulacie() {
		BehSimulacieInfo info = new BehSimulacieInfo();
		ObservableList<StatistikaInfo> statistiky = info.statistiky_;
		statistiky.add(new StatistikaInfo("Číslo replikácie", (this.currentReplication() + 1) + ""));
		statistiky.add(new StatistikaInfo("Koniec replikácie", Helper.FormatujSimulacnyCas(this.currentTime())));
		statistiky.add(pocetCestujucichSim.getStatistikaInfo());

		return info;
	}

	public boolean ulozKonfiguraciuVozidiel(String cesta, KonfiguraciaVozidiel konfiguraciaVozidiel) {
		try (PrintWriter writer = new PrintWriter(cesta)) {
			writer.println(konfiguraciaVozidiel.getPrevadzkaLiniek().getNazov());
			for (VozidloKonfiguracia konfiguracia: konfiguraciaVozidiel.getKonfiguraciaVozidiel()) {

				writer.println(
								konfiguracia.getTypVozidla().getNazov() + Helper.DEFAULT_SEPARATOR +
								konfiguracia.getTypLinky().getNazovLinky() + Helper.DEFAULT_SEPARATOR +
								konfiguracia.getCasPrijazduNaPrvuZastavku() + Helper.DEFAULT_SEPARATOR
				);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean nacitajKonfiguraciuVozidiel(String cesta, KonfiguraciaVozidiel vyslednaKonfiguracia) {
		try(BufferedReader br = new BufferedReader(new FileReader(cesta))) {
			ArrayList<String> parsedLine = new ArrayList<>();

			String line = br.readLine();
			Helper.ParseLine(line, parsedLine);
			vyslednaKonfiguracia.setPrevadzkaLiniek(PREVADZKA_LINIEK.GetPrevadzkuLiniekPodlaNazvu(parsedLine.get(0)));
			ArrayList<VozidloKonfiguracia> konfiguraciaVozidiel = new ArrayList<>();
			for(; (line = br.readLine()) != null; ) {
				Helper.ParseLine(line, parsedLine);

				konfiguraciaVozidiel.add(new VozidloKonfiguracia(
						TYP_VOZIDLA.GetTypVozidlaNaZakladeNazvu(parsedLine.get(0)),
						TYP_LINKY.GetTypLinkyNaZakladeNazvu(parsedLine.get(1)),
						Double.parseDouble(parsedLine.get(2))
				));

			}
			vyslednaKonfiguracia.setKonfiguraciaVozidiel(konfiguraciaVozidiel);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public double getCasZaciatkuZapasu() {
		return _casZaciatkuZapasu;
	}

	//meta! tag="end"
}