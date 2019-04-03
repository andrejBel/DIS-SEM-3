package Simulacia.Jadro;

import GUI.CAS_UPDATU;
import GUI.IObserver;
import Simulacia.SystemoveUdalosti.PosunCasu;
import Simulacia.Udalost.FrontUdalosti;
import Simulacia.Udalost.Udalost;

import java.util.LinkedList;

public abstract class SimulacneJadro {

    private LinkedList<IObserver> observers_ = new LinkedList<>();

    private FrontUdalosti frontUdalosti_ = new FrontUdalosti();

    private boolean spustena_ = false;
    private boolean pozastavena_ = false;
    private boolean koniecSimulacie_ = false;

    private double casReplikacie_ = 0.0; // cas v ramci replikacie
    private double casSimulacie_ = 0.0; // kumulovany cas simulacie
    private long cisloReplikacie_ = 0; // aktualne cislo replikacie

    public SimulacneJadro setKoncovyCasReplikacie(double koncovyCasReplikacie) {
        this.koncovyCasReplikacie_ = koncovyCasReplikacie;
        return this;
    }

    public SimulacneJadro setJednotkovyPosunSimulacnehoCasu(long jednotkovyPosunSimulacnehoCasu) {
        this.jednotkovyPosunSimulacnehoCasu_ = jednotkovyPosunSimulacnehoCasu;
        return this;
    }

    public SimulacneJadro setSpomalenieCasu(long spomalenieCasu) {
        this.spomalenieCasu_ = spomalenieCasu;
        return this;
    }

    public SimulacneJadro setPocetReplikacii(long pocetReplikacii) {
        this.pocetReplikacii_ = pocetReplikacii;
        return this;
    }

    public SimulacneJadro setKrokovanieSimulacie(boolean krokovanieSimulacie) {
        this.krokovanieSimulacie_ = krokovanieSimulacie;
        return this;
    }

    public SimulacneJadro setZrychlenaSimulacia(boolean zrychlenaSimulacia) {
        this.zrychlenaSimulacia_ = zrychlenaSimulacia;
        return this;
    }

    public SimulacneJadro setChladenie(boolean chladenie) {
        this.chladenie_ = chladenie;
        return this;
    }

    public void setPlanujPosunCasu(boolean planujPosunCasu) {
        this.planujPosunCasu_ = planujPosunCasu;
    }

    // tieto je miozne nastavit zvonka
    private double koncovyCasReplikacie_; // kedy konci replikacia

    private long jednotkovyPosunSimulacnehoCasu_; // kolko ma udalost spat

    private long spomalenieCasu_;

    private long pocetReplikacii_;

    private long pocetSimulacii_;


    protected boolean zrychlenaSimulacia_ = false;
    protected boolean krokovanieSimulacie_ = false;
    protected boolean chladenie_ = false;
    protected boolean planujPosunCasu_ = false;

    private final Object zamokPozastavenia_ = new Object();

    private boolean asyncStart_ = false;
    Thread backroundThread_ = null;


    public SimulacneJadro(double koncovyCasReplikacie, long jednotkovyPosunSimulacnehoCasu, long spomalenieCasu, long pocetReplikacii) {
        this(koncovyCasReplikacie, jednotkovyPosunSimulacnehoCasu, spomalenieCasu, pocetReplikacii, 1, true, false, false, false);
    }

    public SimulacneJadro(double koncovyCasReplikacie, long jednotkovyPosunSimulacnehoCasu, long spomalenieCasu, long pocetReplikacii, long pocetSimulacii, boolean zrychlenaSimulacia, boolean krokovanieSimulacie, boolean chladenie, boolean planujPosunCasu) {
        this.koncovyCasReplikacie_ = koncovyCasReplikacie;
        this.jednotkovyPosunSimulacnehoCasu_ = jednotkovyPosunSimulacnehoCasu;
        this.spomalenieCasu_ = spomalenieCasu;
        this.pocetReplikacii_ = pocetReplikacii;
        this.pocetSimulacii_ = pocetSimulacii;
        this.zrychlenaSimulacia_ = zrychlenaSimulacia;
        this.krokovanieSimulacie_ = krokovanieSimulacie;
        this.chladenie_ = chladenie;
        this.planujPosunCasu_ = planujPosunCasu;
    }

    public void replikacia() {
        double casPredoslejUdalosti = 0.0;
        Udalost aktualnaUdalost = null;

        while ((chladenie_ || this.casReplikacie_ < this.koncovyCasReplikacie_) && !this.frontUdalosti_.isPrazdny()) {
            if (this.krokovanieSimulacie_) {
                pause();
            }
            if (this.pozastavena_) {
                synchronized (zamokPozastavenia_) {
                    try {
                        zamokPozastavenia_.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            /*
            if (this.krokovanieSimulacie_ && this.koniecSimulacie_) {
                break;
            }
            */

            if (this.koniecSimulacie_) {
                break;
            }

            this.casSimulacie_ -= casPredoslejUdalosti;

            aktualnaUdalost = frontUdalosti_.getNasledujucaUdalost();
            //System.out.println("Aktualna udalost: " + aktualnaUdalost);

            this.casReplikacie_ = aktualnaUdalost.casUdalosti_;

            this.casSimulacie_ += this.casReplikacie_;

            casPredoslejUdalosti = aktualnaUdalost.casUdalosti_;

            aktualnaUdalost.vykonaj();
            if (!zrychlenaSimulacia_) {
                refreshGui(CAS_UPDATU.POCAS_SIMULACIE_REPLIKACIE);
            }
        }

    }

    public void start() {
        System.out.println("Zaciatok simulacie");
        for (int indexSimulacie = 1; indexSimulacie <= pocetSimulacii_; indexSimulacie++) {
            this.predSimulaciou();
            for (int indexReplikacie = 1; indexReplikacie <= this.pocetReplikacii_; indexReplikacie++) {

                this.predReplikaciou(indexReplikacie);
                this.replikacia();
                if (this.koniecSimulacie_ && this.krokovanieSimulacie_) {
                    break;
                }
                this.poReplikacii(indexReplikacie);
                this.casReplikacie_ = 0.0;
                if (this.koniecSimulacie_) {
                    break;
                }
            }
            this.poSimulacii(indexSimulacie);
            if (this.koniecSimulacie_) {
                break;
            }
        }
        System.out.println("Koniec simulacii");
    }

    public void startAsync() {
        stop();
        backroundThread_ = new Thread(() -> this.start());
        backroundThread_.setPriority(Thread.MAX_PRIORITY);
        backroundThread_.setDaemon(true);
        backroundThread_.start();
    }

    public void pause() {
        if (!this.pozastavena_) {
            this.pozastavena_ = (true);
        }
    }

    public void resume() {
        if (this.pozastavena_) {
            synchronized (zamokPozastavenia_) {
                this.pozastavena_ = false;
                this.zamokPozastavenia_.notifyAll();
            }
        }
    }

    public void stop() {
        this.koniecSimulacie_ = true;
        resume();
        this.spustena_ = false;
        this.pozastavena_ = false;
        if (backroundThread_ != null) {
            try {
                backroundThread_.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                backroundThread_ = null;
            }
        }
    }

    public void planujUdalost(Udalost udalost) {
        if (udalost.casUdalosti_ >= this.casReplikacie_) {
            frontUdalosti_.planujUdalost(udalost);
        } else {
            throw new RuntimeException("Porusena kauzalita!!!");
        }
    }



    public void predSimulaciou() {
        this.spustena_ = true;
        this.pozastavena_ = false;
        this.koniecSimulacie_ = false;

        this.casReplikacie_ = 0.0; // cas v ramci replikacie
        this.casSimulacie_ = 0.0; // kumulovany cas simulacie
        this.cisloReplikacie_ = 0;

        this.frontUdalosti_.vynulujPocitadlo();
    }

    public void predReplikaciou(Integer cisloReplikacie) {
        this.cisloReplikacie_ = cisloReplikacie;
        this.frontUdalosti_.getFrontUdalosti().clear();
        this.casReplikacie_ = 0.0;

        if (!this.zrychlenaSimulacia_ && this.planujPosunCasu_) {
            this.planujUdalost(new PosunCasu(jednotkovyPosunSimulacnehoCasu_, this));
        }
    }

    public abstract void poReplikacii(Integer cisloReplikacie);

    public abstract void poSimulacii(int cisloSimulacie);

    public void registrujObservera(IObserver observer) {
        boolean najdeny = false;
        for (IObserver iobserver : observers_) {
            if (iobserver == observer) {
                najdeny = true;
                break;
            }
        }
        if (!najdeny) {
            observers_.add(observer);
        }
    }

    public void odregistujObservera(IObserver observer) {
        int indexObservera = -1;
        int index = 0;
        for (IObserver iobserver : observers_) {
            if (iobserver == observer) {
                indexObservera = index;
                break;
            }
            index++;
        }
        if (indexObservera != -1) {
            observers_.remove(indexObservera);
        }
    }

    protected void refreshGui(CAS_UPDATU casUpdatu) {
        for (IObserver iobserver : observers_) {
            iobserver.update(this, casUpdatu);
        }
    }

    public double getCasReplikacie() {
        return casReplikacie_;
    }

    public double getCasSimulacie() {
        return casSimulacie_;
    }

    public long getCisloReplikacie() {
        return cisloReplikacie_;
    }

    public double getKoncovyCasReplikacie() {
        return koncovyCasReplikacie_;
    }

    public long getJednotkovyPosunSimulacnehoCasu() {
        return jednotkovyPosunSimulacnehoCasu_;
    }

    public long getSpomalenieCasu() {
        return spomalenieCasu_;
    }

    public long getPocetReplikacii() {
        return pocetReplikacii_;
    }

    public boolean isKrokovanieSimulacie() {
        return krokovanieSimulacie_;
    }

    public boolean isZrychlenaSimulacia() {
        return zrychlenaSimulacia_;
    }

    public boolean isChladenie() {
        return chladenie_;
    }

    protected FrontUdalosti getFrontUdalosti() {
        return frontUdalosti_;
    }

    public boolean spustenaSimulacia() {
        return spustena_;
    }

    public long getPocetSimulacii() {
        return pocetSimulacii_;
    }

    public SimulacneJadro setPocetSimulacii(long pocetSimulacii) {
        this.pocetSimulacii_ = pocetSimulacii;
        return this;
    }

    public int getPocetUdalostiVoFronteUdalosti() {
        return this.frontUdalosti_.getFrontUdalosti().size();
    }

}
