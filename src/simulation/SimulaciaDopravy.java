package simulation;

import Model.*;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.BehReplikacieInfo;
import Model.Info.BehSimulacieInfo;
import Model.Info.VozidloInfo;
import OSPABA.*;
import Statistiky.StatistikaInfo;
import Utils.Helper;
import agents.*;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimulaciaDopravy extends Simulation {

    private List<ZastavkaKonfiguracia> _zoznamZastavok = new ArrayList<>();
    private HashMap<String, ZastavkaKonfiguracia> _zastavkyKonfiguracia = new HashMap<>();
    private ArrayList<VozidloKonfiguracia> _vozidlaKonfiguracia = new ArrayList<>();
	private HashMap<TYP_LINKY, Linka> _linky = new HashMap<>();

    private boolean _krokovanie = false;
    private String _coPozastaviloSimulaciu;


	public SimulaciaDopravy() {
	    init();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
        _zoznamZastavok = Arrays.asList(
                new ZastavkaKonfiguracia("AA",3900.0/123.0),
                new ZastavkaKonfiguracia("AB",3900.0/92.0),
                new ZastavkaKonfiguracia("AC",3900.0/241.0),
                new ZastavkaKonfiguracia("AD",3900.0/123.0),
                new ZastavkaKonfiguracia("AE",3900.0/215.0),
                new ZastavkaKonfiguracia("AF",3900.0/245.0),
                new ZastavkaKonfiguracia("AG",3900.0/137.0),
                new ZastavkaKonfiguracia("AH",3900.0/132.0),
                new ZastavkaKonfiguracia("AI",3900.0/164.0),
                new ZastavkaKonfiguracia("AJ",3900.0/124.0),
                new ZastavkaKonfiguracia("AK",3900.0/213.0),
                new ZastavkaKonfiguracia("AL",3900.0/185.0),
                new ZastavkaKonfiguracia("BA",3900.0/79.0),
                new ZastavkaKonfiguracia("BB",3900.0/69.0),
                new ZastavkaKonfiguracia("BC",3900.0/43.0),
                new ZastavkaKonfiguracia("BD",3900.0/127.0),
                new ZastavkaKonfiguracia("BE",3900.0/30.0),
                new ZastavkaKonfiguracia("BF",3900.0/69.0),
                new ZastavkaKonfiguracia("BG",3900.0/162.0),
                new ZastavkaKonfiguracia("BH",3900.0/90.0),
                new ZastavkaKonfiguracia("BI",3900.0/148.0),
                new ZastavkaKonfiguracia("BJ",3900.0/171.0),
                new ZastavkaKonfiguracia("CA",3900.0/240.0),
                new ZastavkaKonfiguracia("CB",3900.0/310.0),
                new ZastavkaKonfiguracia("CC",3900.0/131.0),
                new ZastavkaKonfiguracia("CD",3900.0/190.0),
                new ZastavkaKonfiguracia("CE",3900.0/132.0),
                new ZastavkaKonfiguracia("CF",3900.0/128.0),
                new ZastavkaKonfiguracia("CG",3900.0/70.0),
                new ZastavkaKonfiguracia("K1",3900.0/260.0),
                new ZastavkaKonfiguracia("K2",3900.0/210.0),
                new ZastavkaKonfiguracia("K3",3900.0/220.0),
                new ZastavkaKonfiguracia("ST", 0.0)
        );

        for (ZastavkaKonfiguracia zastavkaKonfiguracia : _zoznamZastavok) {
            _zastavkyKonfiguracia.put(zastavkaKonfiguracia.getNazovZastavky(), zastavkaKonfiguracia);
        }
        _zastavkyKonfiguracia.get("ST").setVystupnaZastavka();

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
				new ZastavkaLinky(_zastavkyKonfiguracia.get("AL"), 0.9 * 60.0, _zastavkyKonfiguracia.get("ST")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("ST"), 25 * 60.0, _zastavkyKonfiguracia.get("AA"))
		), TYP_LINKY.LINKA_A);

		Linka linkaB = new Linka(Arrays.asList(
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BA"), 1.2 * 60.0, _zastavkyKonfiguracia.get("BB")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BB"), 2.3 * 60.0, _zastavkyKonfiguracia.get("BC")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BC"), 3.2 * 60.0, _zastavkyKonfiguracia.get("BD")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BD"), 4.3 * 60.0, _zastavkyKonfiguracia.get("K2")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K2"), 1.2 * 60.0, _zastavkyKonfiguracia.get("BE")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BE"), 2.7 * 60.0, _zastavkyKonfiguracia.get("BF")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BF"), 3.3 * 60.0, _zastavkyKonfiguracia.get("K3")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K3"), 6.6 * 60.0, _zastavkyKonfiguracia.get("BG")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BG"), 4.3 * 60.0, _zastavkyKonfiguracia.get("BH")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BH"), 0.5 * 60.0, _zastavkyKonfiguracia.get("BI")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BI"), 2.7 * 60.0, _zastavkyKonfiguracia.get("BJ")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("BJ"), 1.3 * 60.0, _zastavkyKonfiguracia.get("ST")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("ST"), 10 * 60.0, _zastavkyKonfiguracia.get("BA"))
		), TYP_LINKY.LINKA_B);

		Linka linkaC = new Linka(Arrays.asList(
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CA"), 0.6 * 60.0, _zastavkyKonfiguracia.get("CB")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CB"), 2.3 * 60.0, _zastavkyKonfiguracia.get("K1")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K1"), 4.1 * 60.0, _zastavkyKonfiguracia.get("K2")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("K2"), 6.6 * 60.0, _zastavkyKonfiguracia.get("CC")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CC"), 2.3 * 60.0, _zastavkyKonfiguracia.get("CD")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CD"), 7.1 * 60.0, _zastavkyKonfiguracia.get("CE")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CE"), 4.8 * 60.0, _zastavkyKonfiguracia.get("CF")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CF"), 3.7 * 60.0, _zastavkyKonfiguracia.get("CG")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("CG"), 7.2 * 60.0, _zastavkyKonfiguracia.get("ST")),
				new ZastavkaLinky(_zastavkyKonfiguracia.get("ST"), 30 * 60.0, _zastavkyKonfiguracia.get("CA"))
		), TYP_LINKY.LINKA_C);

		_linky.put(linkaA.getTypLinky(), linkaA);
		_linky.put(linkaB.getTypLinky(), linkaB);
		_linky.put(linkaC.getTypLinky(), linkaC);


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
        setKonfiguraciuVozdiel( Arrays.asList(
                //new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_A, 30.0),
				//new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_B, 60.0),
				new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_C, 90.0)
        ));

        agentPohybu().inizializujVozidla(_vozidlaKonfiguracia);
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
    }

    @Override
    public void simulationFinished() {
        // Dysplay simulation results
        super.simulationFinished();
    }

    public void setKonfiguraciuVozdiel(List<VozidloKonfiguracia> konfiguraciaVozidiel) {
        this._vozidlaKonfiguracia.clear();
        this._vozidlaKonfiguracia.addAll(konfiguraciaVozidiel);
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
		if (_krokovanie) {
			statistiky.add(new StatistikaInfo("Čo pozastavilo simuláciu", _coPozastaviloSimulaciu));
		}

		ObservableList<VozidloInfo> vozidlaStatistiky = info.vozidlaInfo_;
		for (Vozidlo vozidlo: agentPohybu().getVozidla()) {
			vozidlaStatistiky.add(vozidlo.getVozidloInfo());
		}

		return info;
	}

	public BehSimulacieInfo getStatistikySimulacie() {
		BehSimulacieInfo info = new BehSimulacieInfo();
		ObservableList<StatistikaInfo> statistiky = info.statistiky_;
		statistiky.add(new StatistikaInfo("Číslo replikácie", (this.currentReplication() + 1) + ""));
		statistiky.add(new StatistikaInfo("Koniec replikácie", Helper.FormatujSimulacnyCas(this.currentTime())));


		return info;
	}

	public boolean ulozKonfiguraciuVozidiel(String cesta, List<VozidloKonfiguracia> konfiguracie) {
		try (PrintWriter writer = new PrintWriter(cesta)) {
			for (VozidloKonfiguracia konfiguracia: konfiguracie) {
				StringBuilder stringBuilder = new StringBuilder();
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

	public boolean nacitajKonfiguraciuVozidiel(String cesta, List<VozidloKonfiguracia> vyslednaKonfiguracia) {
		try(BufferedReader br = new BufferedReader(new FileReader(cesta))) {
			ArrayList<String> parsedLine = new ArrayList<>();
			vyslednaKonfiguracia.clear();
			for(String line; (line = br.readLine()) != null; ) {
				Helper.ParseLine(line, parsedLine);

				vyslednaKonfiguracia.add(new VozidloKonfiguracia(
						TYP_VOZIDLA.GetTypVozidlaNaZakladeNazvu(parsedLine.get(0)),
						TYP_LINKY.GetTypLinkyNaZakladeNazvu(parsedLine.get(1)),
						Double.parseDouble(parsedLine.get(2))
				));

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

    //meta! tag="end"
}