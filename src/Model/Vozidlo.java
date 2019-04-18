package Model;

import Model.Enumeracie.STAV_VOZIDLA;
import Model.Enumeracie.TYP_VOZIDLA;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import Stanok.Simulacia.Sprava;
import Statistiky.WStatNamed;
import Utils.Helper;
import simulation.SimulaciaDopravy;

public class Vozidlo extends SimulationEntity {


    private final long _idVozidla; // constructor
    private final int _maximalnaKapacita; // z typu vozidla

    private final double _casPrijazduNaPrvuZastavku;
    private final Linka _linkaNaKtorejJazdi;

    private final TYP_VOZIDLA _typVozidla;

    private SimQueue<Sprava> _cestujuciVoVozidle;

    private STAV_VOZIDLA _stavVozidla = STAV_VOZIDLA.CAKA_NA_VYJAZD;

    private ZastavkaLinky _zastavkaNaKtorejJeNaposledyBol = null;
    private double _casOdchoduZPoslednejZastavky = -1.0;
    private double _casPrichoduNaDalsiuZastavu = -1.0;
    private boolean _pohybujeSa = false;
    private double _kolkoSekundJazdilo = 0.0;
    private int _indexZastavkyLinky = -1;

    public Vozidlo(Simulation mySim, long idVozidla, double casPrijazduNaPrvuZastavku, Linka linkaNaKtorejJazdi, TYP_VOZIDLA typVozidla) {
        super(mySim);
        this._idVozidla = idVozidla;
        this._maximalnaKapacita = typVozidla.getKapacita();
        this._casPrijazduNaPrvuZastavku = casPrijazduNaPrvuZastavku;
        this._linkaNaKtorejJazdi = linkaNaKtorejJazdi;
        this._typVozidla = typVozidla;
        this._cestujuciVoVozidle = new SimQueue<>(new WStatNamed(mySim(), "Priemerný počet cestujúcich"));
    }

    @Override
    public void beforeReplication() {
        _cestujuciVoVozidle.clear();

        _stavVozidla = STAV_VOZIDLA.CAKA_NA_VYJAZD;

        _zastavkaNaKtorejJeNaposledyBol = null;
        _casOdchoduZPoslednejZastavky = -1.0;
        _casPrichoduNaDalsiuZastavu = -1.0;
        _pohybujeSa = false;
        _kolkoSekundJazdilo = 0.0;
        _indexZastavkyLinky = -1;
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

    }

    /*
    ++_indexZastavkyLinky;
        if (_indexZastavkyLinky > _linkaNaKtorejJazdi.getZastavky().size()) {
            _indexZastavkyLinky = 0;
        }
        _zastavkaNaKtorejJeNaposledyBol = _linkaNaKtorejJazdi.getZastavky().get(_indexZastavkyLinky);
    */

    public void vykonajPresunKDalsejZastavke() {
        if (_pohybujeSa == true) {
            throw new RuntimeException("Vozidlo musi stat, aby sa mohlo pohnut");
        }
        //_zastavkaNaKtorejJeNaposledyBol =
        /*
        _stavVozidla = STAV_VOZIDLA.CAKA_NA_VYJAZD;

        _zastavkaNaKtorejJeNaposledyBol = null;
        _casOdchoduZPoslednejZastavky = -1.0;
        _casPrichoduNaDalsiuZastavu = -1.0;
        _pohybujeSa = false;
        _kolkoSekundJazdilo = 0.0;
        */

        _stavVozidla = STAV_VOZIDLA.POHYB;
        _casOdchoduZPoslednejZastavky = mySim().currentTime();
        _casPrichoduNaDalsiuZastavu = mySim().currentTime() + _zastavkaNaKtorejJeNaposledyBol.getCasPresunuNaDalsiuZastavku();
        _pohybujeSa = true;
    }

    public void ukonciPresun() {
        if (_pohybujeSa == false) {
            throw new RuntimeException("Vozidlo musi byt v pohybe, aby sa mohlo zastavit");
        }
        /*
        _stavVozidla = STAV_VOZIDLA.CAKA_NA_VYJAZD;

        _zastavkaNaKtorejJeNaposledyBol = null;
        _casOdchoduZPoslednejZastavky = -1.0;
        _casPrichoduNaDalsiuZastavu = -1.0;
        _pohybujeSa = false;
        _kolkoSekundJazdilo = 0.0;
        */
        _kolkoSekundJazdilo += _zastavkaNaKtorejJeNaposledyBol.getCasPresunuNaDalsiuZastavku();

        ++_indexZastavkyLinky;
        if (_indexZastavkyLinky > _linkaNaKtorejJazdi.getZastavky().size()) {
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
                double kolkoPercentPrejdenych = ((mySim().currentTime() - _casOdchoduZPoslednejZastavky) / (_casPrichoduNaDalsiuZastavu - _casOdchoduZPoslednejZastavky) ) * 100;

                info = "Presun medzi zastávkami: " + getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky() + "->" + getNasledujucaZastavka().getNazovZastavky();
                info += ", začiatok presunu: " + Helper.FormatujSimulacnyCas(_casOdchoduZPoslednejZastavky) + ", koniec presunu: " + Helper.FormatujSimulacnyCas(_casPrichoduNaDalsiuZastavu);
                info += ", prejdených " + Helper.FormatujDouble(kolkoPercentPrejdenych) + " %";
                break;
            case CAKANIE_NA_ZASTAVKE:
                info = "Čakanie na zastávke: " + getAktualnaAleboPoslednaNavstivenaZastavka().getNazovZastavky() + ", nasledujúca zastávka: " + getNasledujucaZastavka().getNazovZastavky();
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

    public Zastavka getAktualnaAleboPoslednaNavstivenaZastavka() {
        return _zastavkaNaKtorejJeNaposledyBol.getZastavka();
    }

    public Zastavka getNasledujucaZastavka() {
        return _zastavkaNaKtorejJeNaposledyBol.getDalsiaZastavka();
    }

    public STAV_VOZIDLA getStavVozidla() {
        return _stavVozidla;
    }



}
