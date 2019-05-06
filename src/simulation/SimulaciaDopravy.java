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
import Statistiky.WStatNamed;
import Utils.Helper;
import agents.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class SimulaciaDopravy extends Simulation {

    public static final Random GENERATOR_NASAD = new Random();

    private TreeMap<String, ZastavkaKonfiguracia> _zastavkyKonfiguracia = new TreeMap<>();
    private KonfiguraciaVozidiel _konfiguraciaVozidiel = new KonfiguraciaVozidiel(null, new ArrayList<>());
    private TreeMap<TYP_LINKY, Linka> _linky = new TreeMap<>();

    private boolean _krokovanie = false;
    private LinkedList<String> _zoznamUdalostiCoPozastaviliSimulaciu = new LinkedList<>();
    private double _casZaciatkuZapasu;


    // GLOBALNE STATISTIKY
    private StatNamed _pocetCestujucichSim = new StatNamed("Počet cestujúcich");
    private StatNamed _priemernyCasCakaniaNaZastavkeSim = new StatNamed("Priemerný čas čakania Z.");
    private StatNamed _priemernyCasCakaniaNaAutobusSim = new StatNamed("Priemerný čas čakania A.");
    private StatNamed _priemernyCasCakaniaNaMinibusSim = new StatNamed("Priemerný čas čakania M.");

    private StatNamed _percentoCestujucichPrichadzajucichPoZaciatkuZapasuSim = new StatNamed("Percento ľudí prichádzajúcich po začiatku zapasu");
    private StatNamed _priemernyZiskMinibusovSim = new StatNamed("Priemerný zisk minibusov");

    private TreeMap<String, StatNamed> _priemernyCasCakaniaCestujucichNaZastavkeSim = new TreeMap<>();
    private TreeMap<String, StatNamed> _priemernaDlzkaFrontuNaZastavkeSim = new TreeMap<>();
    private TreeMap<String, StatNamed> _priemernyPocetZakaznikovNaZastavkeSim = new TreeMap<>();
    private ArrayList<StatNamed> _priemernyPocetJazdSim = new ArrayList<>();
    private ArrayList<StatNamed> _priemerneVytazenieVozidielSim = new ArrayList<>();

    private List<StatNamed> _simStats = Arrays.asList(
            _pocetCestujucichSim,
            _priemernyCasCakaniaNaZastavkeSim,
            _priemernyCasCakaniaNaAutobusSim,
            _priemernyCasCakaniaNaMinibusSim,
            _percentoCestujucichPrichadzajucichPoZaciatkuZapasuSim,
            _priemernyZiskMinibusovSim
    );


    public SimulaciaDopravy() {
        init();
    }

    private boolean nacitajVstupneData() {
        try (BufferedReader br = new BufferedReader(new FileReader(KONSTANTY.VSTUPNY_SUBOR))) {
            ArrayList<String> parsedLine = new ArrayList<>();


            String line;
            Function<String, String> vymenCiaru = (String s) -> {
              return s.replace(',', '.');
            };


            for (; (line = br.readLine()) != null; ) {
                Helper.ParseLine(line, parsedLine);
                ArrayList<ArrayList<String>> parsovaneZastavky = new ArrayList<>();
                TYP_LINKY typLinky = TYP_LINKY.GetTypLinkyNaZakladeNazvu(parsedLine.get(0));
                int pocetZastavokNaLinke = Integer.parseInt(vymenCiaru.apply(parsedLine.get(1)));
                for (int indexZastavky = 0; indexZastavky < pocetZastavokNaLinke; indexZastavky++) {
                    ArrayList<String> parsedZastavka = new ArrayList<>();
                    line = vymenCiaru.apply(br.readLine());

                    Helper.ParseLine(line, parsedZastavka);
                    parsovaneZastavky.add(parsedZastavka);
                    String nazovZastavky = parsedZastavka.get(0);
                    int maximalnyPocetCestujucich = Integer.parseInt(parsedZastavka.get(2));
                    if (!_zastavkyKonfiguracia.containsKey(nazovZastavky)) {
                        _zastavkyKonfiguracia.put(nazovZastavky, new ZastavkaKonfiguracia(nazovZastavky, maximalnyPocetCestujucich));
                    }
                }
                ArrayList<ZastavkaLinky> zastavkyLinky = new ArrayList<>();
                for (int indexZastavky = 0; indexZastavky < parsovaneZastavky.size(); indexZastavky++) {
                    ArrayList<String> terajsiaZastavka = parsovaneZastavky.get(indexZastavky);
                    ArrayList<String> dalsiaZastavka = parsovaneZastavky.get((indexZastavky + 1) % parsovaneZastavky.size());
                    zastavkyLinky.add(new ZastavkaLinky(
                            _zastavkyKonfiguracia.get(terajsiaZastavka.get(0)),
                            Double.parseDouble(terajsiaZastavka.get(1)),
                            _zastavkyKonfiguracia.get(dalsiaZastavka.get(0))
                            )

                    );
                    System.out.println(zastavkyLinky.get(zastavkyLinky.size() -1));
                }
                Linka linka = new Linka(zastavkyLinky, typLinky );
                _linky.put(typLinky, linka);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {

        if (!nacitajVstupneData()) {
            throw new RuntimeException("Vstupne data sa nepodarilo nacitat!!!");
        }

        ZastavkaKonfiguracia stadion = _zastavkyKonfiguracia.get(KONSTANTY.STADION);
        stadion.setVystupnaZastavka();


            // najdi zastavku, co je najdalej od stadiona
        double najdlhsiCasPresunuZoZastavkyNaStadiona = 0;
        for (Map.Entry<TYP_LINKY, Linka> infoOLinke : _linky.entrySet()) {
            double vzdialenostKStadionu = 0.0;
            ArrayList<ZastavkaLinky> zastavkyLinky = infoOLinke.getValue().getZastavky();
            for (int indexLinky = zastavkyLinky.size() - 2; indexLinky >= 0; indexLinky--) { // ignorujem stadion
                vzdialenostKStadionu += zastavkyLinky.get(indexLinky).getCasPresunuNaDalsiuZastavku();
                zastavkyLinky.get(indexLinky).setVzdialenostKStadionu(vzdialenostKStadionu);
                System.out.println( zastavkyLinky.get(indexLinky));
            }
            najdlhsiCasPresunuZoZastavkyNaStadiona = Math.max(najdlhsiCasPresunuZoZastavkyNaStadiona, vzdialenostKStadionu);
        }
        this._casZaciatkuZapasu = najdlhsiCasPresunuZoZastavkyNaStadiona + KONSTANTY.NAJSKORSI_PRICHOD_NA_ZASTAVKU;
        System.out.println("Cas zaciatku zapasu: " + this._casZaciatkuZapasu);
        _zastavkyKonfiguracia.forEach((s, zastavkaKonfiguracia) -> {
            _priemernyCasCakaniaCestujucichNaZastavkeSim.put(zastavkaKonfiguracia.getNazovZastavky(), new StatNamed("Priemerný čas čakania C. na Z. " + zastavkaKonfiguracia.getNazovZastavky()));
            _priemernaDlzkaFrontuNaZastavkeSim.put(zastavkaKonfiguracia.getNazovZastavky(), new StatNamed("Priemerná dĺžka frontu na Z. " + zastavkaKonfiguracia.getNazovZastavky()));
            _priemernyPocetZakaznikovNaZastavkeSim.put(zastavkaKonfiguracia.getNazovZastavky(), new StatNamed("Priemerný počet cestujúcich na Z. " + zastavkaKonfiguracia.getNazovZastavky()));
        });


        setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
        setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu(), _zastavkyKonfiguracia, _linky));
        setAgentPrepravy(new AgentPrepravy(Id.agentPrepravy, this, agentModelu(), _zastavkyKonfiguracia));
        setAgentPohybu(new AgentPohybu(Id.agentPohybu, this, agentPrepravy(), _linky));
        setAgentZastavok(new AgentZastavok(Id.agentZastavok, this, agentPrepravy(), _zastavkyKonfiguracia));

    }

    private AgentModelu _agentModelu;

    public AgentModelu agentModelu() {
        return _agentModelu;
    }

    public void setAgentModelu(AgentModelu agentModelu) {
        _agentModelu = agentModelu;
    }

    private AgentOkolia _agentOkolia;

    public AgentOkolia agentOkolia() {
        return _agentOkolia;
    }

    public void setAgentOkolia(AgentOkolia agentOkolia) {
        _agentOkolia = agentOkolia;
    }

    private AgentPrepravy _agentPrepravy;

    public AgentPrepravy agentPrepravy() {
        return _agentPrepravy;
    }

    public void setAgentPrepravy(AgentPrepravy agentPrepravy) {
        _agentPrepravy = agentPrepravy;
    }

    private AgentPohybu _agentPohybu;

    public AgentPohybu agentPohybu() {
        return _agentPohybu;
    }

    public void setAgentPohybu(AgentPohybu agentPohybu) {
        _agentPohybu = agentPohybu;
    }

    private AgentZastavok _agentZastavok;

    public AgentZastavok agentZastavok() {
        return _agentZastavok;
    }

    public void setAgentZastavok(AgentZastavok agentZastavok) {
        _agentZastavok = agentZastavok;
    }

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
        if (_konfiguraciaVozidiel.getPrevadzkaLiniek() == null) {
            throw new RuntimeException("Nesprávna konfigurácia");
        }


        agentPohybu().inizializujVozidla(_konfiguraciaVozidiel.getKonfiguraciaVozidiel());


        _simStats.forEach(Stat::clear);
        _priemernyCasCakaniaCestujucichNaZastavkeSim.forEach((s, stat) -> {
            stat.clear();
        });
        _priemernaDlzkaFrontuNaZastavkeSim.forEach((s, stat) -> {
            stat.clear();
        });
        _priemernyPocetZakaznikovNaZastavkeSim.forEach((s, stat) -> {
            stat.clear();
        });
        _priemernyPocetJazdSim.clear();
        _priemerneVytazenieVozidielSim.clear();
        agentPohybu().getVozidla().forEach(vozidlo -> {
            _priemernyPocetJazdSim.add(new StatNamed("Priemerný počet jázd V. " + vozidlo.getIdVozidla() + ", linka: " + vozidlo.getLinkaNaKtorejJazdi().getTypLinky().getSkratka()));
            _priemerneVytazenieVozidielSim.add(new StatNamed("Vyťaženie V " + vozidlo.getIdVozidla() + ", linka: " + vozidlo.getLinkaNaKtorejJazdi().getTypLinky().getSkratka()));
        });


    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...
        _agentModelu.spustiSimulaciu();
        _zoznamUdalostiCoPozastaviliSimulaciu.clear();
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        super.replicationFinished();

        _pocetCestujucichSim.addSample(_agentZastavok.getPocetCestujucichRep());
        _priemernyCasCakaniaNaZastavkeSim.addSample(_agentZastavok.getPriemernyCasCakaniaCestujucehoNaZastavkeRep().mean());
        _priemernyCasCakaniaNaAutobusSim.addSample(_agentZastavok.getPriemernyCasCakaniaCestujucehoNaAutobusRep().mean());
        _priemernyCasCakaniaNaMinibusSim.addSample(_agentZastavok.getPriemernyCasCakaniaCestujucehoNaMinibusRep().mean());

        _percentoCestujucichPrichadzajucichPoZaciatkuZapasuSim.addSample(_agentZastavok.getPercentoLudiPrichadzajucichPoZapase());


        _agentZastavok.getZastavky().forEach((s, z) -> {
            _priemernyCasCakaniaCestujucichNaZastavkeSim.get(s).addSample(z.getPriemernyCasCakaniaCestujecehoNaZastavke().mean());
            _priemernaDlzkaFrontuNaZastavkeSim.get(s).addSample(z.getCestujuciNaZastavke().lengthStatistic().mean());
            _priemernyPocetZakaznikovNaZastavkeSim.get(s).addSample(z.getPocetCestujucich());
        });

        double ziskMinibusov = 0.0;
        for (int indexVozidla = 0; indexVozidla < agentPohybu().getVozidla().size(); indexVozidla++) {
            Vozidlo vozidlo = agentPohybu().getVozidla().get(indexVozidla);
            _priemernyPocetJazdSim.get(indexVozidla).addSample(vozidlo.getKolkoKratStiholPrejstKStadionu());
            _priemerneVytazenieVozidielSim.get(indexVozidla).addSample(vozidlo.getVytazenieVozidlaRep() * 100);
            if (!vozidlo.getTypVozidla().isAutobus()) {
                ziskMinibusov += vozidlo.getTrzba();
            }
        }
        _priemernyZiskMinibusovSim.addSample(ziskMinibusov);
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

    public TreeMap<String, ZastavkaKonfiguracia> getZastavkyKonfiguracia() {
        return _zastavkyKonfiguracia;
    }

    public void pridajUdalostCoPozastavilaSimulaciu(String coPozastaviloSimulaciu) {
        _zoznamUdalostiCoPozastaviliSimulaciu.add(coPozastaviloSimulaciu);
    }


    public BehReplikacieInfo getStatistikyVRamciReplikacie() {
        BehReplikacieInfo info = new BehReplikacieInfo();
        ObservableList<StatistikaInfo> statistiky = info.statistiky_;

        statistiky.add(new StatistikaInfo("Číslo replikácie", (this.currentReplication() + 1) + ""));
        statistiky.add(new StatistikaInfo("Čas replikácie", Helper.FormatujSimulacnyCas(this.currentTime(), false)));
        if (_krokovanie && !_zoznamUdalostiCoPozastaviliSimulaciu.isEmpty()) {
            statistiky.add(new StatistikaInfo("Čo pozastavilo simuláciu", ""));
            while (!_zoznamUdalostiCoPozastaviliSimulaciu.isEmpty()) {
                statistiky.add(new StatistikaInfo("", _zoznamUdalostiCoPozastaviliSimulaciu.removeFirst()));
            }

        }

        statistiky.add(new StatistikaInfo("Počet cestujúcich", String.valueOf(_agentZastavok.getPocetCestujucichRep())));
        statistiky.add(_agentZastavok.getPriemernyCasCakaniaCestujucehoNaZastavkeRep().getStatistikaInfo());
        statistiky.add(_agentZastavok.getPriemernyCasCakaniaCestujucehoNaAutobusRep().getStatistikaInfo());
        statistiky.add(_agentZastavok.getPriemernyCasCakaniaCestujucehoNaMinibusRep().getStatistikaInfo());
        _agentZastavok.getZastavky().forEach((s, z) -> {
            statistiky.add(z.getPriemernyCasCakaniaCestujecehoNaZastavke().getStatistikaInfo());
            statistiky.add(((WStatNamed) z.getCestujuciNaZastavke().lengthStatistic()).getStatistikaInfo());
        });

        int pocetLudiNaStadione = _agentZastavok.getPocetCestujucichNaStadione();
        int pocetLudiNaStadioneNaCas = _agentZastavok.getPocetCestujucichNaStadioneNaCas();
        int pocetLudiPoZaciatkuZapasu = _agentZastavok.getPocetCestujucichNaStadionePoZaciatkuZapasu();
        statistiky.add(new StatistikaInfo("Počet C. na štadióne", pocetLudiNaStadione + ""));
        statistiky.add(new StatistikaInfo("Počet C. na štadióne na čas", pocetLudiNaStadioneNaCas + ""));
        statistiky.add(new StatistikaInfo("Počet C. na štadióne po začiatku", pocetLudiPoZaciatkuZapasu + ""));
        statistiky.add(new StatistikaInfo("Percento C. na štadióne po začiatku", Helper.FormatujDouble(_agentZastavok.getPercentoLudiPrichadzajucichPoZapase()) + " %"));


        info._priemernyCasCakaniaCestujucehoNaZastavke = _agentZastavok.getPriemernyCasCakaniaCestujucehoNaZastavkeRep().mean();
        info._priemernyCasCakaniaCestujucehoNaAutobus = _agentZastavok.getPriemernyCasCakaniaCestujucehoNaAutobusRep().mean();
        info._priemernyCasCakaniaCestujucehoNaMinibus = _agentZastavok.getPriemernyCasCakaniaCestujucehoNaMinibusRep().mean();

        double ziskMinibusov = 0.0;
        for (Vozidlo vozidlo : agentPohybu().getVozidla()) {
            statistiky.add(vozidlo.getStatistikaInfoVytazenieVozidlaRep());
            statistiky.add(new StatistikaInfo("Počet jázd vozidlo " + vozidlo.getIdVozidla(), Helper.FormatujDouble(vozidlo.getKolkoKratStiholPrejstKStadionu()))); // TODO
            if (!vozidlo.getTypVozidla().isAutobus()) {
                ziskMinibusov += vozidlo.getTrzba();
            }
        }
        statistiky.add(new StatistikaInfo("Zisk minibusov", Helper.FormatujDouble(ziskMinibusov, 0)));

        ObservableList<VozidloInfo> vozidlaStatistiky = info.vozidlaInfo_;
        for (Vozidlo vozidlo : agentPohybu().getVozidla()) {
            vozidlaStatistiky.add(vozidlo.getVozidloInfo(true));
        }

        HashMap<String, ZastavkaInfo> zastavkyInfo = info.zastavkyInfo_;

        for (Map.Entry<String, Zastavka> zastavkaEntry : _agentZastavok.getZastavky().entrySet()) {
            String nazovZastavky = zastavkaEntry.getKey();
            Zastavka zastavka = zastavkaEntry.getValue();
            ObservableList<CestujuciInfo> infoOCestujucichNaZastavke = FXCollections.observableArrayList();
            for (Sprava sprava : zastavka.getCestujuciNaZastavke()) {
                Cestujuci cestujuci = sprava.getCestujuci();
                infoOCestujucichNaZastavke.add(cestujuci.getCestujuciInfo());
            }
            ObservableList<VozidloInfo> infoOVozidlachNaZastavke = FXCollections.observableArrayList();
            for (Sprava sprava: zastavka.getVozidlaNaZastavke()) {
                Vozidlo vozidlo = sprava.getVozidlo();
                infoOVozidlachNaZastavke.add(vozidlo.getVozidloInfo(false));
            }

            zastavkyInfo.put(nazovZastavky, new ZastavkaInfo(infoOCestujucichNaZastavke, infoOVozidlachNaZastavke));
        }

        return info;
    }

    public BehSimulacieInfo getStatistikySimulacie() {
        BehSimulacieInfo info = new BehSimulacieInfo();
        info._cisloReplikacie = this.currentReplication();
        info._priemernyCasCakaniaNaZastavke = _priemernyCasCakaniaNaZastavkeSim.mean();
        info._priemernyCasCakaniaNaAutobus = _priemernyCasCakaniaNaAutobusSim.mean();
        info._priemernyCasCakaniaNaMinibus = _priemernyCasCakaniaNaMinibusSim.mean();

        info._percentoCestujuciochPrichadzajucichPoZaciatku = _percentoCestujucichPrichadzajucichPoZaciatkuZapasuSim.mean();
        info._ziskMinibusov = _priemernyZiskMinibusovSim.mean();

        ObservableList<StatistikaInfo> statistiky = info.statistiky_;
        statistiky.add(new StatistikaInfo("Číslo replikácie", (this.currentReplication() + 1) + ""));
        statistiky.add(new StatistikaInfo("Koniec replikácie", Helper.FormatujSimulacnyCas(this.currentTime())));

        _simStats.forEach(stat -> {
            statistiky.add(stat.getStatistikaInfo());
        });

        statistiky.add(_pocetCestujucichSim.getStatistikaInfo());
        statistiky.add(new StatistikaInfo("Náklady konfigurácie", Helper.FormatujDouble(_konfiguraciaVozidiel.getNakladyKonfiguracie() ,0)));

        _agentZastavok.getZastavky().forEach((s, zastavka) -> {
            info.statistikyZastavky_.add(_priemernyCasCakaniaCestujucichNaZastavkeSim.get(s).getStatistikaInfo());
            info.statistikyZastavky_.add(_priemernaDlzkaFrontuNaZastavkeSim.get(s).getStatistikaInfo());
            info.statistikyZastavky_.add(_priemernyPocetZakaznikovNaZastavkeSim.get(s).getStatistikaInfo());
        });

        for (int indexVozidla = 0; indexVozidla < agentPohybu().getVozidla().size(); indexVozidla++) {
            info.statistikyVozidla_.add(_priemernyPocetJazdSim.get(indexVozidla).getStatistikaInfo());
            info.statistikyVozidla_.add(_priemerneVytazenieVozidielSim.get(indexVozidla).getStatistikaInfo());
        }

        return info;
    }

    public String getCsvFormatBehSimulacie() {
        StringBuilder header = new StringBuilder();
        StringBuilder headerData = new StringBuilder();
        int sirkaHlavicky = 0;

        header.append("Číslo replikácie" + Helper.DEFAULT_SEPARATOR);
        headerData.append(String.valueOf(this.currentReplication()) + Helper.DEFAULT_SEPARATOR);
        sirkaHlavicky++;

        header.append("Náklady konfigurácie" + Helper.DEFAULT_SEPARATOR);
        headerData.append(String.valueOf(this._konfiguraciaVozidiel.getNakladyKonfiguracie()).replace('.', ',') + Helper.DEFAULT_SEPARATOR);
        sirkaHlavicky++;

        _simStats.forEach(stat -> {
            header.append(stat.getName() + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR);
            headerData.append(stat.getCsvFormat());
        });
        sirkaHlavicky += _simStats.size() * 3;

        _agentZastavok.getZastavky().forEach((s, zastavka) -> {
            header.append("Priem. cas cakania Z. " + s + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR);
            header.append("Priem. dlzka frontu Z " + s + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR);
            headerData.append(_priemernyCasCakaniaCestujucichNaZastavkeSim.get(s).getCsvFormat());
            headerData.append(_priemernaDlzkaFrontuNaZastavkeSim.get(s).getCsvFormat());
        });

        sirkaHlavicky += _agentZastavok.getZastavky().size() * 6;

        String filler = String.format("%" + String.valueOf(sirkaHlavicky) + "s", "").replace(' ', Helper.DEFAULT_SEPARATOR);
        header.append(
                "Id vozidla" + Helper.DEFAULT_SEPARATOR +
                "Typ vozidla" + Helper.DEFAULT_SEPARATOR +
                "Linka" + Helper.DEFAULT_SEPARATOR +
                "Čas odchodu zo zastávky" + Helper.DEFAULT_SEPARATOR +
                "Priemerný počet jázd." + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR +
                "Vyťaženie" + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR + Helper.DEFAULT_SEPARATOR
                );

        String typVozidla = filler + _konfiguraciaVozidiel.getPrevadzkaLiniek().getNazov();
        headerData.append(typVozidla);
        for (Vozidlo vozidlo: _agentPohybu.getVozidla()) {
            long idVozidla = vozidlo.getIdVozidla();
            StringBuilder vozidloData = new StringBuilder();
            vozidloData.append(filler);

            vozidloData.append(String.valueOf(idVozidla) + Helper.DEFAULT_SEPARATOR);
            vozidloData.append(vozidlo.getTypVozidla().getNazov() + Helper.DEFAULT_SEPARATOR);
            vozidloData.append(vozidlo.getLinkaNaKtorejJazdi().getTypLinky().getNazovLinky() + Helper.DEFAULT_SEPARATOR);
            vozidloData.append(String.valueOf(vozidlo.getCasPrijazduNaPrvuZastavku()).replace('.', ',') + Helper.DEFAULT_SEPARATOR);
            vozidloData.append(String.valueOf(_priemernyPocetJazdSim.get((int) (idVozidla - 1) ).getCsvFormat() ));
            vozidloData.append(String.valueOf(_priemerneVytazenieVozidielSim.get((int) (idVozidla - 1) ).getCsvFormat() ));
            headerData.append("\n" + vozidloData.toString());
        }

        return header.toString() + "\n" + headerData.toString();
    }

    public boolean ulozKonfiguraciuVozidiel(String cesta, KonfiguraciaVozidiel konfiguraciaVozidiel) {
        try (PrintWriter writer = new PrintWriter(cesta)) {
            writer.println(konfiguraciaVozidiel.getCsvFormat());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean nacitajKonfiguraciuVozidiel(String cesta, KonfiguraciaVozidiel vyslednaKonfiguracia) {
        try (BufferedReader br = new BufferedReader(new FileReader(cesta))) {
            ArrayList<String> parsedLine = new ArrayList<>();

            String line = br.readLine();
            Helper.ParseLine(line, parsedLine);
            vyslednaKonfiguracia.setPrevadzkaLiniek(PREVADZKA_LINIEK.GetPrevadzkuLiniekPodlaOrdinal(Integer.parseInt(parsedLine.get(0))));
            ArrayList<VozidloKonfiguracia> konfiguraciaVozidiel = new ArrayList<>();
            for (; (line = br.readLine()) != null; ) {
                Helper.ParseLine(line, parsedLine);
                if (parsedLine.size() == 0) {
                    break;
                }
                String casPrijazdu = parsedLine.get(2);
                casPrijazdu = casPrijazdu.replace(',', '.');
                konfiguraciaVozidiel.add(new VozidloKonfiguracia(
                        TYP_VOZIDLA.GetTypVozidlaNaZakladeNazvu(parsedLine.get(0)),
                        TYP_LINKY.GetTypLinkyNaZakladeNazvu(parsedLine.get(1)),
                        Double.parseDouble(casPrijazdu)
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


    public boolean isKoniecReplikacie() {
        for (Vozidlo vozidlo : _agentPohybu.getVozidla()) {
            if (vozidlo.getCelkovyPocetCestujucichVoVozidle() > 0) {
                return false;
            }
        }
        for (Map.Entry<String, ZastavkaOkolie> zastavkaOkolieEntry: _agentOkolia.getZastavkyOkolia().entrySet()) {
            ZastavkaOkolie zastavkaOkolie = zastavkaOkolieEntry.getValue();
            if (zastavkaOkolie.getCasKoncaGenerovaniaPrichodovCestujucich() > this.currentTime()) {
                return false;
            }
        }
        TreeMap<String, Zastavka> zastavky = _agentZastavok.getZastavky();
        for (Map.Entry<String, Zastavka> zastavkaEntry : zastavky.entrySet()) {
            Zastavka zastavka = zastavkaEntry.getValue();
            if (zastavka.getCestujuciNaZastavke().size() > 0 && !zastavka.getZastavkaKonfiguracia().getNazovZastavky().equals(KONSTANTY.STADION)) {
                return false;
            }
        }
        return true;
    }

    public void finishReplication() {
        // TODO pozbieraj statistiky!!!

        this.stopReplication(this.currentTime());
    }

    public void zapisVysledokSimulacieDoSuboru(String cesta) {
        File f = new File(cesta);
        boolean appendUtf8Recognizer = false;
        if (!f.exists()) {
            appendUtf8Recognizer = true;
        }
        try (
                BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(cesta, true),
                        StandardCharsets.UTF_8
                )
        ); PrintWriter printWriter = new PrintWriter(out)
        ) {
            if (appendUtf8Recognizer) {
                printWriter.write(Helper.UTF8_RECOGNIZER);
            }
            printWriter.println(getCsvFormatBehSimulacie());

        } catch (IOException e) {
            e.printStackTrace();
        }
            }

    //meta! tag="end"
}