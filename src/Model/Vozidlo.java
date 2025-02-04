package Model;

import Model.Enumeracie.STAV_VOZIDLA;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.CestujuciInfo;
import Model.Info.VozidloInfo;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import Statistiky.StatistikaInfo;
import Statistiky.WStatNamed;
import Statistiky.WeightStat;
import Utils.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simulation.KONSTANTY;
import simulation.SimulaciaDopravy;
import simulation.Sprava;

import java.text.DecimalFormat;
import java.util.TreeMap;
import java.util.function.Function;

public class Vozidlo extends SimulationEntity {


    private final long _idVozidla; // constructor
    private final int _maximalnaKapacita; // z typu vozidla

    private final double _casPrijazduNaPrvuZastavku;
    private final Linka _linkaNaKtorejJazdi;

    private final TYP_VOZIDLA _typVozidla;

    private SimQueue<Sprava> _cestujuciVoVozidle;
    private TreeMap<Long, Sprava> _nastupujuciCestujuci;
    private TreeMap<Long, Sprava> _vystupujuciCestujuci;
    private Boolean[] _obsadenostDveri;

    private STAV_VOZIDLA _stavVozidla = STAV_VOZIDLA.CAKA_NA_VYJAZD;

    private ZastavkaLinky _zastavkaNaKtorejJeNaposledyBol = null;
    private double _casOdchoduZPoslednejZastavky = -1.0;
    private double _casPrichoduNaDalsiuZastavu = -1.0;
    private boolean _pohybujeSa = false;
    private double _kolkoSekundJazdilo = 0.0;
    private int _indexZastavkyLinky = -1;
    private double _casVstupuDoFrontuVozidielNaZastavke = 0.0;
    private boolean _vozidloVoFronteVozidielCakajucichNaZastavke = false;

    private int pocetPrichodovKStadionu = 0;
    private int pocetPrivezenychCestujucichStadion = 0;
    private double _trzba = 0.0;

    public Vozidlo(Simulation mySim, long idVozidla, double casPrijazduNaPrvuZastavku, Linka linkaNaKtorejJazdi, TYP_VOZIDLA typVozidla) {
        super(mySim);
        this._idVozidla = idVozidla;
        this._maximalnaKapacita = typVozidla.getKapacita();
        this._casPrijazduNaPrvuZastavku = casPrijazduNaPrvuZastavku;
        this._linkaNaKtorejJazdi = linkaNaKtorejJazdi;
        this._typVozidla = typVozidla;
        this._cestujuciVoVozidle = new SimQueue<>();
        this._nastupujuciCestujuci = new TreeMap<>();
        this._vystupujuciCestujuci = new TreeMap<>();
        this._obsadenostDveri = new Boolean[typVozidla.getPocetDveri()];
        for (int indexDveri = 0; indexDveri < this._obsadenostDveri.length; indexDveri++) {
            this._obsadenostDveri[indexDveri] = false;
        }
    }

    @Override
    public void beforeReplication() {
        _cestujuciVoVozidle.clear();
        _nastupujuciCestujuci.clear();
        _vystupujuciCestujuci.clear();
        for (int indexDveri = 0; indexDveri < this._obsadenostDveri.length; indexDveri++) {
            this._obsadenostDveri[indexDveri] = false;
        }

        _stavVozidla = STAV_VOZIDLA.CAKA_NA_VYJAZD;

        _zastavkaNaKtorejJeNaposledyBol = null;
        _casOdchoduZPoslednejZastavky = -1.0;
        _casPrichoduNaDalsiuZastavu = -1.0;
        _pohybujeSa = false;
        _kolkoSekundJazdilo = 0.0;
        _indexZastavkyLinky = -1;
        _casVstupuDoFrontuVozidielNaZastavke = 0.0;
        _vozidloVoFronteVozidielCakajucichNaZastavke = false;

        pocetPrichodovKStadionu = 0;
        pocetPrivezenychCestujucichStadion = 0;
        _trzba = 0.0;
    }

    @Override
    public SimulaciaDopravy mySim() {
        return (SimulaciaDopravy) super.mySim();
    }

    public void vykonajPrijazdKPrvejZastavke() {
        _indexZastavkyLinky = 0;
        _zastavkaNaKtorejJeNaposledyBol = _linkaNaKtorejJazdi.getZastavky().get(_indexZastavkyLinky);
        _stavVozidla = STAV_VOZIDLA.CAKANIE_NA_ZASTAVKE;
        _pohybujeSa = false;
        _casPrichoduNaDalsiuZastavu = mySim().currentTime();
    }


    public void vykonajPresunKDalsejZastavke() {
        if (_pohybujeSa == true) {
            throw new RuntimeException("Vozidlo musi stat, aby sa mohlo pohnut");
        }
        _stavVozidla = STAV_VOZIDLA.POHYB;
        _casOdchoduZPoslednejZastavky = mySim().currentTime();
        _casPrichoduNaDalsiuZastavu = mySim().currentTime() + _zastavkaNaKtorejJeNaposledyBol.getCasPresunuNaDalsiuZastavku();
        _pohybujeSa = true;
    }

    public void ukonciPresun() {
        if (_pohybujeSa == false) {
            throw new RuntimeException("Vozidlo musi byt v pohybe, aby sa mohlo zastavit");
        }

        _kolkoSekundJazdilo += _zastavkaNaKtorejJeNaposledyBol.getCasPresunuNaDalsiuZastavku();

        ++_indexZastavkyLinky;
        if (_indexZastavkyLinky >= _linkaNaKtorejJazdi.getZastavky().size()) {
            _indexZastavkyLinky = 0;
        }
        _zastavkaNaKtorejJeNaposledyBol = _linkaNaKtorejJazdi.getZastavky().get(_indexZastavkyLinky);

        _stavVozidla = STAV_VOZIDLA.CAKANIE_NA_ZASTAVKE;
        _pohybujeSa = false;

    }

    public String infoOStave() {
        String info = "";
        switch (_stavVozidla) {
            case CAKA_NA_VYJAZD:
                info = "Čaká na výjazd na zastávku: " + _linkaNaKtorejJazdi.getZastavky().get(0).getZastavka().getNazovZastavky() + ", naplánovaný na: " + Helper.FormatujSimulacnyCas(this._casPrijazduNaPrvuZastavku);
                break;
            case POHYB:
                double kolkoPercentPrejdenych = ((mySim().currentTime() - _casOdchoduZPoslednejZastavky) / (_casPrichoduNaDalsiuZastavu - _casOdchoduZPoslednejZastavky)) * 100;

                info = "Presun : " + getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky() + "->" + getNasledujucaZastavka().getNazovZastavky() + "[" + Helper.FormatujDouble(kolkoPercentPrejdenych) + " %" + "]";
                info += ", začiatok presunu: " + Helper.FormatujSimulacnyCas(_casOdchoduZPoslednejZastavky) + ", koniec presunu: " + Helper.FormatujSimulacnyCas(_casPrichoduNaDalsiuZastavu);
                break;
            case CAKANIE_NA_ZASTAVKE:
                info = "Čakanie na zastávke: " + getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky() +
                        ", nasledujúca zastávka: " + getNasledujucaZastavka().getNazovZastavky() + ", čas príchodu: " + Helper.FormatujSimulacnyCas(_casPrichoduNaDalsiuZastavu);
                break;
            default:
                break;
        }
        return info;
    }

    public double kolkoSekundSaJazdilo() {
        if (_pohybujeSa) {
            double dlzkaPohybu = mySim().currentTime() - _casOdchoduZPoslednejZastavky;
            return _kolkoSekundJazdilo + dlzkaPohybu;
        } else {
            return _kolkoSekundJazdilo;
        }
    }


    public double getCasPotrebnyPrePresunKDalsejZastavke() {
        if (_zastavkaNaKtorejJeNaposledyBol == null) {
            throw new RuntimeException("");
        }
        return _zastavkaNaKtorejJeNaposledyBol.getCasPresunuNaDalsiuZastavku();
    }

    public ZastavkaKonfiguracia getAktualnaAleboPoslednaNavstivenaZastavka() {
        return _zastavkaNaKtorejJeNaposledyBol.getZastavka();
    }

    public ZastavkaKonfiguracia getNasledujucaZastavka() {
        return _zastavkaNaKtorejJeNaposledyBol.getDalsiaZastavka();
    }

    public STAV_VOZIDLA getStavVozidla() {
        return _stavVozidla;
    }

    public double getCasPrijazduNaPrvuZastavku() {
        return _casPrijazduNaPrvuZastavku;
    }

    public VozidloInfo getVozidloInfo(boolean ajSCestujucimi) {
        ObservableList<CestujuciInfo> nastupujuciCestujuci = FXCollections.observableArrayList();
        ObservableList<CestujuciInfo> cestujuciVoVozidle = FXCollections.observableArrayList();
        ObservableList<CestujuciInfo> vystupujuciCestujuci = FXCollections.observableArrayList();

        if (ajSCestujucimi) {
            _nastupujuciCestujuci.forEach((s, cestujuci) -> {
                nastupujuciCestujuci.add(cestujuci.getCestujuci().getCestujuciInfo());
            });

            _cestujuciVoVozidle.forEach(cestujuci -> {
                cestujuciVoVozidle.add(cestujuci.getCestujuci().getCestujuciInfo());
            });

            _vystupujuciCestujuci.forEach((s, cestujuci) -> {
                vystupujuciCestujuci.add(cestujuci.getCestujuci().getCestujuciInfo());
            });
        }

        return new VozidloInfo(
                _idVozidla,
                _typVozidla.getNazov(),
                kolkoSekundSaJazdilo(),
                _stavVozidla.getStav(),
                infoOStave(),
                _casVstupuDoFrontuVozidielNaZastavke,
                _trzba,
                _nastupujuciCestujuci.size(),
                _cestujuciVoVozidle.size(),
                _vystupujuciCestujuci.size(),
                nastupujuciCestujuci,
                cestujuciVoVozidle,
                vystupujuciCestujuci
        );
    }

    public long getIdVozidla() {
        return _idVozidla;
    }

    public TYP_VOZIDLA getTypVozidla() {
        return _typVozidla;
    }

    public Integer obsadPrvyIndexVolnychDveri() {
        for (int indexDveri = 0; indexDveri < this._obsadenostDveri.length; indexDveri++) {
            if (this._obsadenostDveri[indexDveri] == false) {
                this._obsadenostDveri[indexDveri] = true;
                return indexDveri;
            }
        }
        return null;
    }

    public boolean suVolneDvere() {
        for (int indexDveri = 0; indexDveri < this._obsadenostDveri.length; indexDveri++) {
            if (this._obsadenostDveri[indexDveri] == false) {
                return true;
            }
        }
        return false;
    }

    public void uvolniPouzivaneDvere(int indexDveri) {
        if (indexDveri < 0 || indexDveri > this._obsadenostDveri.length) {
            throw new RuntimeException("Index dveri out of range");
        }
        this._obsadenostDveri[indexDveri] = false;
    }

    public int getCelkovyPocetCestujucichVoVozidle() {
        return this._cestujuciVoVozidle.size() + this._nastupujuciCestujuci.size() + this._vystupujuciCestujuci.size();
    }

    public boolean jeVolneMiestoVoVozidle() {
        return getCelkovyPocetCestujucichVoVozidle() < this._maximalnaKapacita;
    }

    public void pridajCestujucehoNaNastup(Sprava spravaSCestujucim) {
        long idCestujuceho = spravaSCestujucim.getCestujuci().getIdCestujuceho();

        if (KONSTANTY.DEBUG) {
            if (this._nastupujuciCestujuci.containsKey(idCestujuceho)) {
                throw new RuntimeException("Cestujuci uz nastupuje");
            }
            this._cestujuciVoVozidle.forEach(c -> {
                if (c.getCestujuci().getIdCestujuceho() == idCestujuceho) {
                    throw new RuntimeException("Cestujuci uz nastupil");
                }
            });
            if (this._vystupujuciCestujuci.containsKey(idCestujuceho)) {
                throw new RuntimeException("Cestujuci vystupuje");
            }
        }
        this._nastupujuciCestujuci.put(idCestujuceho, spravaSCestujucim);
    }

    public Sprava odstranCestujucehoNaNastup(long idCestujuceho) {
        Sprava sprava = this._nastupujuciCestujuci.remove(idCestujuceho);
        if (sprava == null) {
            throw new RuntimeException("Cestujuci s danym id nenastupuje");
        }
        return sprava;
    }

    public void pridajCestujucehoNaVystup(Sprava spravaSCestujucim) {
        long idCestujuceho = spravaSCestujucim.getCestujuci().getIdCestujuceho();

        if (KONSTANTY.DEBUG) {
            if (this._nastupujuciCestujuci.containsKey(idCestujuceho)) {
                throw new RuntimeException("Cestujuci nastupuje");
            }
            if (this._vystupujuciCestujuci.containsKey(idCestujuceho)) {
                throw new RuntimeException("Cestujuci uz vystupuje");
            }
            boolean cestujuciNajdeny = false;
            for (Sprava sprava : _cestujuciVoVozidle) {
                Cestujuci cestujuci = sprava.getCestujuci();
                if (idCestujuceho == cestujuci.getIdCestujuceho()) {
                    cestujuciNajdeny = true;
                    break;
                }
            }
            if (cestujuciNajdeny == true) {
                throw new RuntimeException("Cestujuci nemoze vo vozidle aby mohol vystupit");
            }
        }
        this._vystupujuciCestujuci.put(idCestujuceho, spravaSCestujucim);
    }

    public Sprava odstranCestujucehoNaVystup(long idCestujuceho) {
        Sprava sprava = this._vystupujuciCestujuci.remove(idCestujuceho);
        if (sprava == null) {
            throw new RuntimeException("Cestujuci s danym id nevystupoval");
        }
        return sprava;
    }

    public void pridajCestujucehoDoVozidla(Sprava spravaSCestujucim) {
        long idCestujuceho = spravaSCestujucim.getCestujuci().getIdCestujuceho();
        if (KONSTANTY.DEBUG) {
            if (_nastupujuciCestujuci.containsKey(idCestujuceho)) {
                throw new RuntimeException("Cestujuci nastupuje");
            }

            if (_vystupujuciCestujuci.containsKey(idCestujuceho)) {
                throw new RuntimeException("Cestujuci nastupuje");
            }

            for (Sprava sprava : _cestujuciVoVozidle) {
                Cestujuci cestujuci = sprava.getCestujuci();
                if (idCestujuceho == cestujuci.getIdCestujuceho()) {
                    throw new RuntimeException("Cestujuci je uz vo vozidle");
                }
            }
        }
        _trzba += _typVozidla.getCenaListka();
        _cestujuciVoVozidle.add(spravaSCestujucim);
    }

    public Sprava odstranCestujucehoVoVozidle() {
        if (_cestujuciVoVozidle.size() == 0) {
            throw new RuntimeException("Pocet cestujucich je 0");
        }
        return _cestujuciVoVozidle.poll();
    }

    public boolean isVozidloPrazdne() {
        return getCelkovyPocetCestujucichVoVozidle() == 0;
    }

    public int getPocetNastupujucichCestujucich() {
        return this._nastupujuciCestujuci.size();
    }

    public int getPocetVystupujucichCestujucich() {
        return this._vystupujuciCestujuci.size();
    }

    public int getPocetCestujucichVAutobuseBezNastupujusichAVystupujucich() {
        return _cestujuciVoVozidle.size();
    }

    public double getCasVstupuDoFrontuVozidielNaZastavke() {
        return _casVstupuDoFrontuVozidielNaZastavke;
    }

    public void setCasVstupuDoFrontuVozidielNaZastavke(double casVstupuDoFrontuVozidielNaZastavke) {
        this._casVstupuDoFrontuVozidielNaZastavke = casVstupuDoFrontuVozidielNaZastavke;
    }

    public boolean isVozidloVoFronteVozidielCakajucichNaZastavke() {
        return _vozidloVoFronteVozidielCakajucichNaZastavke;
    }

    public void setVozidloVoFronteVozidielCakajucichNaZastavke(boolean vozidloVoFronteVozidielCakajucichNaZastavke) {
        this._vozidloVoFronteVozidielCakajucichNaZastavke = vozidloVoFronteVozidielCakajucichNaZastavke;
    }

    public SimQueue<Sprava> getCestujuciVoVozidle() {
        return _cestujuciVoVozidle;
    }

    public Linka getLinkaNaKtorejJazdi() {
        return _linkaNaKtorejJazdi;
    }

    public StatistikaInfo getStatistikaInfoVytazenieVozidlaRep() {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        Function<Double, Double> getVytazenie = (pocet -> (pocet / _maximalnaKapacita) * 100.0);

        return new StatistikaInfo("Vyťaženie V. " + _idVozidla, decimalFormat.format(getVytazenieVozidlaRep() * 100) + " %");
    }

    public int getMaximalnaKapacita() {
        return _maximalnaKapacita;
    }

    public double casZaciakuVystupu = 0.0;
    public double casKoncaVystupu = 0.0;

    public double getCasVystupovania() {
        return casKoncaVystupu - casZaciakuVystupu;
    }

    public void zvysPocetPrichodovKStadionu() {
        ++this.pocetPrichodovKStadionu;
    }

    public void zvysPocetPrivezenychStadionu() {
        ++this.pocetPrivezenychCestujucichStadion;
    }

    public double getVytazenieVozidlaRep() {
        if (pocetPrichodovKStadionu == 0) {
            return ((double) this.getCelkovyPocetCestujucichVoVozidle() ) / ((double) _maximalnaKapacita);
        }

        return ( ( (double) (pocetPrivezenychCestujucichStadion) ) / ((double) pocetPrichodovKStadionu) ) /  ((double) _maximalnaKapacita);
    }

    public double getKolkoKratStiholPrejstKStadionu() {
        double kolkePresielZAktualnejTrasy = 0.0;
        double celkovaVzdialenostKStadionu = _linkaNaKtorejJazdi.getZastavky().get(0).getVzdialenostKStadionu();
        switch (_stavVozidla) {
            case CAKA_NA_VYJAZD:
                kolkePresielZAktualnejTrasy = 0.0;
                break;
            case POHYB:
                if (_zastavkaNaKtorejJeNaposledyBol.getZastavka().getNazovZastavky().equals(KONSTANTY.STADION)) {
                    kolkePresielZAktualnejTrasy = 0.0;
                } else {
                    double kolkoPresielZTrasyMedziZastavkami = ((mySim().currentTime() - _casOdchoduZPoslednejZastavky) / (_casPrichoduNaDalsiuZastavu - _casOdchoduZPoslednejZastavky));
                    double kolkoUzPresiel =  kolkoPresielZTrasyMedziZastavkami * _zastavkaNaKtorejJeNaposledyBol.getCasPresunuNaDalsiuZastavku();

                    double vzdialenostKZastavkePrejdena = celkovaVzdialenostKStadionu - _zastavkaNaKtorejJeNaposledyBol.getVzdialenostKStadionu() + kolkoUzPresiel;
                    kolkePresielZAktualnejTrasy = vzdialenostKZastavkePrejdena / celkovaVzdialenostKStadionu;
                }


                break;
            case CAKANIE_NA_ZASTAVKE:
                if (_zastavkaNaKtorejJeNaposledyBol.getZastavka().getNazovZastavky().equals(KONSTANTY.STADION)) {
                    kolkePresielZAktualnejTrasy = 0.0;
                } else {
                    double vzdialenostPrejdena = celkovaVzdialenostKStadionu - _zastavkaNaKtorejJeNaposledyBol.getVzdialenostKStadionu();
                    kolkePresielZAktualnejTrasy = vzdialenostPrejdena / celkovaVzdialenostKStadionu;
                }

                break;
            default:
                break;
        }

        return pocetPrichodovKStadionu + kolkePresielZAktualnejTrasy;
    }

    public double getTrzba() {
        return _trzba;
    }

}
