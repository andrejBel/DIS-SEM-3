package Simulacia.Model.Restauracia;

import Simulacia.Jadro.ISimulacnyObjekt;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Enumeracie.TypCinnosti;
import Simulacia.Model.Udalost.UdalostRestauracia;
import Simulacia.Model.Udalost.ZaciatokOdnesenia;
import Simulacia.Model.Udalost.ZaciatokPlatenia;
import Simulacia.Model.Udalost.ZaciatokUsadenia;
import Simulacia.SimulaciaRestauracia;
import Simulacia.Statistiky.StatistikaPriemer;
import Simulacia.Statistiky.StatistikaVazenyPriemer;
import Utils.Helper;

import java.util.*;

public class Restauracia implements ISimulacnyObjekt {

    public static final int DENNA_OTVARACIA_DOBA = 9 * (int) Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund();


    private SimulaciaRestauracia simulacia_;
    // premenne modelu
    private int pocetCasnikov_;
    private ArrayList<Casnik> vsetciCasnici_ = new ArrayList<>();
    private ArrayList<Casnik> volniCasnici_ = new ArrayList<>();
    private HashMap<Long, Casnik> pracujuciCasnici_ = new HashMap<>();


    private int pocetKucharov_;
    private ArrayList<Kuchar> vsetciKuchari_ = new ArrayList<>();
    private ArrayList<Kuchar> volniKuchari_ = new ArrayList<>();
    private HashMap<Long, Kuchar> pracujuciKuchari_ = new HashMap<>();


    // STOLY
    private List<Stol.ParametreStola> parametreStolov_ = Arrays.asList
            (
                    new Stol.ParametreStola(2, 10),
                    new Stol.ParametreStola(4, 7),
                    new Stol.ParametreStola(6, 6)
            );

    private ArrayList<Stol> vsetkyStoly_ = new ArrayList<>();
    private TreeMap<Integer, ArrayList<Stol>> volneStoly_ = new TreeMap<>();



    private long pocetZakaznikovCoOdisli_ = 0;
    private long pocetZakaznikov_ = 0; // vsetci co prisli, bez ohladu ci ukoncili vystup zo systemu
    private long indexerZakaznikov_ = 1;

    private Queue<Stol> stolyCakajuceNaObjednavkuAUsadenie_ = new LinkedList<>();
    private Queue<Zakaznik> frontJedal_ = new LinkedList<>();
    private Queue<Stol> stolyCakajuceNaDonesenieJedla_ = new LinkedList<>();
    private Queue<Stol> stolyCakajuceNaZaplatenie_ = new LinkedList<>();

    List<Queue> frontyVRestauracii_ = Arrays.asList(
            stolyCakajuceNaObjednavkuAUsadenie_,
            frontJedal_,
            stolyCakajuceNaDonesenieJedla_,
            stolyCakajuceNaZaplatenie_
    );

    // statistiky
    public StatistikaVazenyPriemer priemernyPocetVolnychCasnikovR_;
    public StatistikaVazenyPriemer priemernyPocetVolnychKucharovR_;
    public HashMap<Integer, Stol.StatistikaStolov> priemernyPocetVolnychStolovJednotlivychKapacitR_;

    public StatistikaPriemer priemernyPocetVolnychCasnikovS_;
    public StatistikaPriemer priemernyPocetVolnychKucharovS_;
    public HashMap<Integer, StatistikaPriemer> priemernyPocetVolnychStolovJednotlivychKapacitS_;



    public Restauracia(SimulaciaRestauracia simulacia, int pocetCasnikov, int pocetKucharov) {
        this.simulacia_ = simulacia;
        this.pocetCasnikov_ = pocetCasnikov;
        this.pocetKucharov_ = pocetKucharov;

        priemernyPocetVolnychCasnikovR_ = new StatistikaVazenyPriemer(simulacia_, "Priemerný počet voľných čašníkov");
        priemernyPocetVolnychKucharovR_ = new StatistikaVazenyPriemer(simulacia_, "Priemerný počet voľných kuchárov");
        priemernyPocetVolnychStolovJednotlivychKapacitR_ = new HashMap<>();
        priemernyPocetVolnychCasnikovS_ = new StatistikaPriemer("Priemerný počet voľných čašníkov");
        priemernyPocetVolnychKucharovS_ = new StatistikaPriemer("Priemerný počet voľných kuchárov");
        priemernyPocetVolnychStolovJednotlivychKapacitS_ = new HashMap<>();


        long indexerStolov_ = 1;
        for (Stol.ParametreStola parametreStola: parametreStolov_) {
            for (int indexStolov = 0; indexStolov < parametreStola.kolkoStolov_; indexStolov++) {
                vsetkyStoly_.add(new Stol(parametreStola.maximalnyPocetZakaznikovPriStole_, indexerStolov_++));
            }
            int maximalnyPocetZakaznikovPriStole = parametreStola.maximalnyPocetZakaznikovPriStole_;
            int pocetStolov = parametreStola.kolkoStolov_;
            priemernyPocetVolnychStolovJednotlivychKapacitR_.put(maximalnyPocetZakaznikovPriStole ,new Stol.StatistikaStolov(simulacia_, "Priemerný počet voľných stolov kapacity " + maximalnyPocetZakaznikovPriStole, pocetStolov));
            priemernyPocetVolnychStolovJednotlivychKapacitS_.put(maximalnyPocetZakaznikovPriStole ,new StatistikaPriemer("Priemerný počet voľných stolov kapacity " + maximalnyPocetZakaznikovPriStole));
        }

    }

    @Override
    public void predSimulaciou() {
        this.vsetciCasnici_.clear();
        for (int indexCasnik = 1; indexCasnik <= pocetCasnikov_ ; indexCasnik++) {
            this.vsetciCasnici_.add(new Casnik(indexCasnik));
        }
        this.vsetciKuchari_.clear();
        for (int indexKuchara = 1; indexKuchara <= pocetKucharov_; indexKuchara++) {
            this.vsetciKuchari_.add(new Kuchar(indexKuchara));
        }

        priemernyPocetVolnychCasnikovS_.clear();
        priemernyPocetVolnychKucharovS_.clear();
        for (Map.Entry<Integer, StatistikaPriemer> statistika: priemernyPocetVolnychStolovJednotlivychKapacitS_.entrySet()) {
            statistika.getValue().clear();
        }
    }

    @Override
    public void predReplikaciou(Integer cisloReplikacie) {
        this.volneStoly_.clear();
        for (Stol.ParametreStola parametreStola: parametreStolov_) {
            this.volneStoly_.put(parametreStola.maximalnyPocetZakaznikovPriStole_, new ArrayList<>());
        }
        this.vsetkyStoly_.forEach(stol -> {
            stol.clear();
            this.volneStoly_.get(stol.getMaximalnyPocetMiest()).add(stol);
        } );


        this.pracujuciCasnici_.clear();
        this.volniCasnici_.clear();

        for (Casnik casnik: vsetciCasnici_) {
            casnik.clear();
        }
        volniCasnici_.addAll(vsetciCasnici_);

        volniKuchari_.clear();
        pracujuciKuchari_.clear();
        for (Kuchar kuchar: vsetciKuchari_) {
            kuchar.clear();
        }
        volniKuchari_.addAll(vsetciKuchari_);

        this.pocetZakaznikovCoOdisli_ = 0;
        this.pocetZakaznikov_ = 0;
        this.indexerZakaznikov_ = 1;

        frontyVRestauracii_.forEach(front -> front.clear());

        priemernyPocetVolnychCasnikovR_.clear(volniCasnici_.size());
        priemernyPocetVolnychKucharovR_.clear(volniKuchari_.size());
        for (Map.Entry<Integer, Stol.StatistikaStolov> statistikaStolov: priemernyPocetVolnychStolovJednotlivychKapacitR_.entrySet()) {
            statistikaStolov.getValue().clear();
        }

    }

    @Override
    public void poReplikacii(Integer cisloReplikacie) {
        priemernyPocetVolnychCasnikovS_.pridaj(priemernyPocetVolnychCasnikovR_.getVazenyPriemer());
        priemernyPocetVolnychKucharovS_.pridaj(priemernyPocetVolnychKucharovR_.getVazenyPriemer());
        for (Map.Entry<Integer, Stol.StatistikaStolov> statistikaStolov: priemernyPocetVolnychStolovJednotlivychKapacitR_.entrySet()) {
            int maximalnyPocetStolov = statistikaStolov.getKey();
            double priemer = statistikaStolov.getValue().getVazenyPriemer();
            priemernyPocetVolnychStolovJednotlivychKapacitS_.get(maximalnyPocetStolov).pridaj(priemer);
        }
    }

    @Override
    public void poSimulacii() {

    }

    public Stol getNajmensiVolnyStolPreSkupinuZakaznikov(int pocetZakaznikov) {
        ArrayList<Stol> odkialJe = null;
        Stol najmensiStol = null;
        for (Map.Entry<Integer, ArrayList<Stol>> stolyDanejKapacity: volneStoly_.entrySet()) {
            int maximalnaKapacita = stolyDanejKapacity.getKey();
            ArrayList<Stol> stoly = stolyDanejKapacity.getValue();
            if (maximalnaKapacita < pocetZakaznikov) {
                continue;
            }
            for (Stol stol: stoly) {
                if (najmensiStol == null) {
                    odkialJe = stoly;
                    najmensiStol = stol;
                }
                if (stol.getMaximalnyPocetMiest() < najmensiStol.getMaximalnyPocetMiest()) {
                    odkialJe = stoly;
                    najmensiStol = stol;
                }
            }
        }
        if (najmensiStol != null) {
            boolean odstraneny = odkialJe.remove(najmensiStol);
            if (!odstraneny) {
                throw new RuntimeException("Stol mal byt odstraneny");
            }
            Stol.StatistikaStolov statistikaStolovKapacityStola = priemernyPocetVolnychStolovJednotlivychKapacitR_.get(najmensiStol.getMaximalnyPocetMiest());
            if (statistikaStolovKapacityStola == null) {
                throw new RuntimeException("Stol danej kapacity sa musi najst");
            }
            statistikaStolovKapacityStola.pridaj(odkialJe.size());
            return najmensiStol;
        }

        return  null;
    }

    public void vratPouzivanyStol(Stol pouzivanyStol) {

        pouzivanyStol.clear();

        ArrayList<Stol> kdePatriStol = volneStoly_.get(pouzivanyStol.getMaximalnyPocetMiest());
        kdePatriStol.add(pouzivanyStol);

        Stol.StatistikaStolov statistikaStolovKapacityStola = priemernyPocetVolnychStolovJednotlivychKapacitR_.get(pouzivanyStol.getMaximalnyPocetMiest());
        if (statistikaStolovKapacityStola == null) {
            throw new RuntimeException("Stol danej kapacity sa musi najst");
        }
        statistikaStolovKapacityStola.pridaj(kdePatriStol.size());
    }

    public void pridajZakaznikovCoOdisli(int kolko) {
        this.pocetZakaznikovCoOdisli_ += kolko;
    }

    public void pridajPrichadzajucichZakaznikov(int kolko) {
        this.pocetZakaznikov_ += kolko;
    }

    public long getPocetZakaznikovCoOdisli() {
        return pocetZakaznikovCoOdisli_;
    }

    public long getPocetZakaznikov() {
        return pocetZakaznikov_;
    }

    public boolean jeNejakyCasnikVolny() {
        return this.volniCasnici_.size() > 0;
    }

    public boolean jeNejakyKucharVolny() {
        return this.volniKuchari_.size() > 0;
    }

    public boolean suCinnostiPreCasnika() {
        return stolyCakajuceNaObjednavkuAUsadenie_.size() > 0 || stolyCakajuceNaDonesenieJedla_.size() > 0 || stolyCakajuceNaZaplatenie_.size() > 0;
    }

    public boolean suCinnostiPreKuchara() {
        return frontJedal_.size() > 0;
    }

    // priradenie casnika, co bol v dany den najviac casu volny
    // odstrani ho zo zoznmau casnikov
    public Casnik obsadCasnika(Stol stolKtoryObsluhuje, TypCinnosti typCinnosti) {
        if (volniCasnici_.size() <= 0) {
            throw new RuntimeException("Toto sa nemoze stat");
        }
        Casnik casnikNaVyber = volniCasnici_.get(0);
        int indexNaVyhodenie = 0;
        for (int indexCasnika = 1; indexCasnika < volniCasnici_.size(); indexCasnika++) {
            Casnik porovnavany = volniCasnici_.get(indexCasnika);
            if (porovnavany.kolkoJeCasnikVolny(simulacia_.getCasReplikacie()) > casnikNaVyber.kolkoJeCasnikVolny(simulacia_.getCasReplikacie())) {
                indexNaVyhodenie = indexCasnika;
                casnikNaVyber = porovnavany;
            }
        }
        volniCasnici_.remove(indexNaVyhodenie);
        priemernyPocetVolnychCasnikovR_.pridaj(volniCasnici_.size());
        casnikNaVyber.setObsadeny(simulacia_.getCasReplikacie());
        pracujuciCasnici_.put(casnikNaVyber.getIdCasnika(), casnikNaVyber);
        casnikNaVyber.setObsluhovanyStol(stolKtoryObsluhuje).setVykonavanaCinnost(typCinnosti);
        return casnikNaVyber;
    }


    public void ustanovCasnikaZaVolneho(Casnik casnik, double cas) {
        Casnik pracujuci = pracujuciCasnici_.remove(casnik.getIdCasnika());
        if (pracujuci == null) {
            throw new RuntimeException("Casnik musel byt predtym obsadeny");
        }
        volniCasnici_.add(casnik);
        priemernyPocetVolnychCasnikovR_.pridaj(volniCasnici_.size());
        if (simulacia_.getCasReplikacie() != cas) {
            System.out.println("Cas replikacie: " + simulacia_.getCasReplikacie());
            System.out.println("Cas poslany: " + cas);
            throw new RuntimeException("Ani tu by si sa nemal dostat");
        }
        casnik.setVolny(simulacia_.getCasReplikacie());
        casnik.setObsluhovanyStol(null).setVykonavanaCinnost(null);
    }

    public Kuchar obsadKuchara(Zakaznik zakaznik) {
        if (volniKuchari_.size() <= 0) {
            throw new RuntimeException("Toto sa nemoze stat");
        }
        Kuchar kucharNaVyber = volniKuchari_.get(0);
        int indexNaVyhodenie = 0;
        for (int indexKuchara = 1; indexKuchara < volniKuchari_.size(); indexKuchara++) {
            Kuchar porovnavany = volniKuchari_.get(indexKuchara);
            if (porovnavany.kolkoJeKucharObsadeny(simulacia_.getCasReplikacie()) < kucharNaVyber.kolkoJeKucharObsadeny(simulacia_.getCasReplikacie())) {
                indexNaVyhodenie = indexKuchara;
                kucharNaVyber = porovnavany;
            }
        }
        volniKuchari_.remove(indexNaVyhodenie);
        priemernyPocetVolnychKucharovR_.pridaj(volniKuchari_.size());
        kucharNaVyber.setObsadeny(simulacia_.getCasReplikacie());
        pracujuciKuchari_.put(kucharNaVyber.getIndexKuchara(), kucharNaVyber);
        kucharNaVyber.setZakaznikKtoremuJedloSaRobi(zakaznik, simulacia_.getCasReplikacie());
        return kucharNaVyber;
    }

    public void ustanovKucharaZaVolneho(Kuchar kuchar) {
        Kuchar pracujuci = pracujuciKuchari_.remove(kuchar.getIndexKuchara());
        if (pracujuci == null) {
            throw new RuntimeException("Kuchar musel byt predtym obsadeny");
        }
        volniKuchari_.add(kuchar);
        priemernyPocetVolnychKucharovR_.pridaj(volniKuchari_.size());
        kuchar.setVolny(simulacia_.getCasReplikacie());
        kuchar.setZakaznikKtoremuJedloSaRobi(null, 0.0);
    }

    public UdalostRestauracia vytvorCasnikovyUdalost(double cas) {
        if (!this.suCinnostiPreCasnika() || !jeNejakyCasnikVolny()) {
            throw new RuntimeException("Nie je splnena podmienka pre praci casnika!!!");
        }
        if (stolyCakajuceNaObjednavkuAUsadenie_.size() > 0) {
            Stol stolNaObsluhu = stolyCakajuceNaObjednavkuAUsadenie_.poll();
            Casnik obsluhujuciCasnik = obsadCasnika(stolNaObsluhu, TypCinnosti.PRIJATIE_OBJEDNAVKY_A_USADENIE);
            stolNaObsluhu.setStavStola_(StolStav.OBJEDNAVANIE);
            return new ZaciatokUsadenia(cas, simulacia_, stolNaObsluhu, obsluhujuciCasnik);
        }
        if (stolyCakajuceNaDonesenieJedla_.size() > 0) {
            Stol stolNaObsluhu = stolyCakajuceNaDonesenieJedla_.poll();
            Casnik obsluhujuciCasnik = obsadCasnika(stolNaObsluhu, TypCinnosti.PRINESENIE_JEDAL);
            stolNaObsluhu.setStavStola_(StolStav.CASNIK_DONASA_JEDLO);
            return new ZaciatokOdnesenia(cas, simulacia_, stolNaObsluhu, obsluhujuciCasnik);
        }
        if (stolyCakajuceNaZaplatenie_.size() > 0) {
            Stol stolNaObsluhu = stolyCakajuceNaZaplatenie_.poll();
            Casnik obsluhujuciCasnik = obsadCasnika(stolNaObsluhu, TypCinnosti.PREVZATIE_PLATBY);
            stolNaObsluhu.setStavStola_(StolStav.PLATENIE);
            return new ZaciatokPlatenia(cas, simulacia_, stolNaObsluhu, obsluhujuciCasnik);
        }
        throw new RuntimeException("Tu sa nemozes dostat");
    }

    public void pridajStolCakajuciNaObjednavkuAUsadenie(Stol stol) {
        /*
        for (Stol s: stolyCakajuceNaObjednavkuAUsadenie_) {
            if (s == stol) {
                throw new RuntimeException("Nemozne!!!");
            }
        }
        */
        stol.setStavStola_(StolStav.CAKAJUCI_NA_OBJEDNAVKU);
        stolyCakajuceNaObjednavkuAUsadenie_.add(stol);
    }

    public void pridajZakaznikaDoFrontuJedal(Zakaznik zakaznik) {
        /*
        for (Zakaznik z: frontJedal_) {
            if (z == zakaznik) {
                throw new RuntimeException("Nemozne!!!");
            }
        }
        */
        frontJedal_.add(zakaznik);
    }

    public void pridajStolCakajuciNaDonesenieJedla(Stol stol) {
        /*
        for (Stol s: stolyCakajuceNaDonesenieJedla_) {
            if (s == stol) {
                throw new RuntimeException("Nemozne!!!");
            }
        }
        */

        stol.setStavStola_(StolStav.CAKANIE_NA_DONESENIE_JEDLA);
        stolyCakajuceNaDonesenieJedla_.add(stol);
    }

    public void  pridajStolCakajuciNaZaplatenie(Stol stol) {
        /*
        for (Stol s: stolyCakajuceNaZaplatenie_) {
            if (s == stol) {
                throw new RuntimeException("Nemozne!!!");
            }
        }
        */
        stol.setStavStola_(StolStav.CAKAJUCI_NA_ZAPLATENIE);
        stolyCakajuceNaZaplatenie_.add(stol);
    }

    public Zakaznik getZakaznikaZFrontuJedal() {
        if (frontJedal_.size() == 0) {
            throw new RuntimeException("Tu by si sa nemal dostat");
        }
        return frontJedal_.poll();
    }

    public Restauracia zvysPocetCasnikov() {
        this.pocetCasnikov_++;
        return this;
    }

    public Restauracia zvysPocetKucharov() {
        this.pocetKucharov_++;
        return this;
    }

    public Restauracia setPocetCasnikov(int pocetCasnikov) {
        this.pocetCasnikov_ = pocetCasnikov;
        return this;
    }

    public Restauracia setPocetKucharov(int pocetKucharov) {
        this.pocetKucharov_ = pocetKucharov;
        return this;
    }

    public int getPocetVsetkychCasnikov() {
        return pocetCasnikov_;
    }

    public int getPocetVsetkychKucharov() {
        return pocetKucharov_;
    }

    public long priradDalsieIdZakaznikovi() {
        return indexerZakaznikov_++;
    }

    public ArrayList<Casnik> getVsetciCasnici() {
        return vsetciCasnici_;
    }

    public int getPocetPracujucichCasnikov() {
        return this.pracujuciCasnici_.size();
    }

    public int getPocetVolnychCasnikov() {
        return this.volniCasnici_.size();
    }

    public ArrayList<Stol> getVsetkyStoly() {
        return vsetkyStoly_;
    }

    public ArrayList<Kuchar> getVsetciKuchari() {
        return vsetciKuchari_;
    }

    public int getPocetPracujucichKucharov() {
        return this.pracujuciKuchari_.size();
    }

    public int getPocetVolnychKucharov() {
        return this.volniKuchari_.size();
    }

    public Queue<Stol> getStolyCakajuceNaObjednavkuAUsadenie() {
        return stolyCakajuceNaObjednavkuAUsadenie_;
    }

    public Queue<Zakaznik> getFrontJedal() {
        return frontJedal_;
    }

    public Queue<Stol> getStolyCakajuceNaDonesenieJedla() {
        return stolyCakajuceNaDonesenieJedla_;
    }

    public Queue<Stol> getStolyCakajuceNaZaplatenie() {
        return stolyCakajuceNaZaplatenie_;
    }

    public TreeMap<Integer, ArrayList<Stol>> getVolneStoly() {
        return volneStoly_;
    }
}
