package managers;

import Model.Cestujuci;
import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Vozidlo;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;
import Utils.Helper;
import simulation.*;
import agents.*;

//meta! id="5"
public class ManagerPrepravy extends Manager {
    public ManagerPrepravy(int id, Simulation mySim, Agent myAgent) {
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
    }

    //meta! sender="AgentModelu", id="92", type="Notice"
    public void processInit(MessageForm message) {
        {
            Sprava copy = (Sprava) message.createCopy();
            copy.setAddressee(mySim().findAgent(Id.agentPohybu));
            notice(copy);
        }
    }

    //meta! sender="AgentPohybu", id="94", type="Notice"
    public void processPrichodVozidlaNaZastavku(MessageForm message) {
        Sprava sprava = (Sprava) message;
        Vozidlo vozidlo = sprava.getVozidlo();
        ZastavkaKonfiguracia zastavkaNaKtoruPrisloVozidlo = vozidlo.getAktualnaAleboPoslednaNavstivenaZastavka();


        sprava.getVozidlo().setVozidloVoFronteVozidielCakajucichNaZastavke(false);

        if (zastavkaNaKtoruPrisloVozidlo.isVystup()) { // cestujuci maju vystupovat na stadione
            if (vozidlo.getCelkovyPocetCestujucichVoVozidle() == 0) {
                myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(message);
                return;
            }
            while (vozidlo.suVolneDvere() && vozidlo.getPocetCestujucichVAutobuseBezNastupujusichAVystupujucich() > 0) {
                myAgent().getAkciaVystupCestujuceho().execute(sprava);
            }
            return; // presunieme sa do metody koniec vystupu cestujuceho

        } else { // cestujuci mozu nastupovat, ak tma nejaky su a je miesto
            if (vozidlo.jeVolneMiestoVoVozidle()) {
                sprava.setCode(Mc.cestujuciNaZastavke);
                sprava.setAddressee(mySim().findAgent(Id.agentZastavok));
                request(sprava);
                return;
            } else { // idem na dalsiu
                myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
                return;
            }
        }
    }

    //meta! sender="AgentModelu", id="97", type="Notice"
    public void processPrichodZakaznikaNaZastavkuAgentModelu(MessageForm message) {
        Sprava sprava = (Sprava) message;
        sprava.setAddressee(Id.agentZastavok);
        request(sprava);
    }

    //meta! sender="AgentZastavok", id="98", type="Response"
    public void processPrichodZakaznikaNaZastavkuAgentZastavok(MessageForm message) {
        Sprava sprava = (Sprava) message;
        // response, odpovedal mi, ze prisiel

        Sprava spravaSVozidlom = myAgent().getPrveVolneVozidloZFrontuVozidielCakajucichNaZastavke((Sprava) message);
        if (spravaSVozidlom != null) {
            if (!sprava.getZastavkaKonfiguracie().getNazovZastavky().equals(spravaSVozidlom.getZastavkaKonfiguracie().getNazovZastavky())) {
                throw new RuntimeException("Zastavka musi byt rovnaka");
            }
            sprava.setVozidlo(spravaSVozidlom.getVozidlo());
            sprava.setCode(Mc.cestujuciNaZastavke);
            sprava.setAddressee(mySim().findAgent(Id.agentZastavok));
            request(sprava);
        }
        // tu dorobit, ze ak su nejake vozidla cakajuce na zastavke, aby nastupil do nich
    }

    //meta! sender="AgentZastavok", id="258", type="Response"
    public void processCestujuciNaZastavke(MessageForm message) {
        // poziadal som o prichod cestujuceho
        Sprava sprava = (Sprava) message;
        Cestujuci cestuciNaZastavke = sprava.getCestujuci();
        Vozidlo vozidlo = sprava.getVozidlo();

        if (cestuciNaZastavke == null) { // na zastavke nikto nie je
            if (vozidlo.getPocetNastupujucichCestujucich() == 0) { // ak vsetci nastupili
                if (mySim().isKoniecReplikacie()) {
                    if (mySim().isKrokovanie()) {
                        mySim().pauseSimulation();
                        mySim().pridajUdalostCoPozastavilaSimulaciu("Koniec simulácie");
                    }
                    mySim().finishReplication();
                }

                if (mySim().getKonfiguraciaVozidiel().getPrevadzkaLiniek() == PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA || !vozidlo.getTypVozidla().isAutobus()) { // hned po nastupeni odchadza
                    myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
                } else { // 1,5 minuty caka, 90s // inak
                    if (!vozidlo.isVozidloVoFronteVozidielCakajucichNaZastavke()) {

                        message.setAddressee(myAgent().findAssistant(Id.naplanujPresunVozidlaNaZastavku));
                        startContinualAssistant(message);
                    } else {
                        double timeDifference = mySim().currentTime() - vozidlo.getCasVstupuDoFrontuVozidielNaZastavke();
                        if (timeDifference >= KONSTANTY.KOLKO_CAKA_PO_NASTUPE_VSETKYCH_CESTUJUCICH) { // todo > ? >=
                            boolean odstranene = myAgent().odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(sprava);
                            if (odstranene == false) {
                                throw new RuntimeException("Vozidlo musibyt odstranene z frontu!!!");
                            }
                            myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
                        }
                    }
                }
            }
        } else {
            // mam cestujuceho a mam isto volne miesto, musim ho usadit
            if (sprava.getVozidlo() == null) {
                System.out.println("stop");
            }
            myAgent().getAkciaNastupCestujuceho().execute(sprava);
            if (vozidlo.jeVolneMiestoVoVozidle() && vozidlo.suVolneDvere()) {
                // poziadam o dalsieho cestujuceho
                Sprava copy = (Sprava) sprava.createCopy();
                copy.setCode(Mc.cestujuciNaZastavke);
                copy.setAddressee(mySim().findAgent(Id.agentZastavok));
                request(copy);
            }
        }

    }

    //meta! sender="AgentNastupuVystupu", id="213", type="Response"
    public void processNastupCestujuceho(MessageForm message) {
        // cestujuci nastupil
        Sprava sprava = (Sprava) message;
        Vozidlo vozidlo = sprava.getVozidlo();
        int indexVstupnychDveri = sprava.getCestujuci().getIndexVstupnychDveri();
        vozidlo.uvolniPouzivaneDvere(indexVstupnychDveri);
        Cestujuci cestujuci = sprava.getCestujuci();
        vozidlo.odstranCestujucehoNaNastup(cestujuci.getIdCestujuceho());
        vozidlo.pridajCestujucehoDoVozidla((Sprava) sprava.createCopy());
        cestujuci.setStavCestujuci(STAV_CESTUJUCI.VEZIE_SA_VO_VOZIDLE);
        if (mySim().isKrokovanie()) {
            mySim().pauseSimulation();
            mySim().pridajUdalostCoPozastavilaSimulaciu("Cestujúci " + cestujuci.getIdCestujuceho() + " nastúpil do vozidla " + vozidlo.getIdVozidla());
        }

        // dotialto je to OK


        if (vozidlo.jeVolneMiestoVoVozidle()) {
            // poziadam o dalsieho cestujuceho
            sprava.setCode(Mc.cestujuciNaZastavke);
            sprava.setAddressee(mySim().findAgent(Id.agentZastavok));
            request(sprava);
        } else {
            if (vozidlo.getPocetNastupujucichCestujucich() == 0) {
                if (mySim().getKonfiguraciaVozidiel().getPrevadzkaLiniek() == PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA || !vozidlo.getTypVozidla().isAutobus()) {
                    myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
                } else if (mySim().getKonfiguraciaVozidiel().getPrevadzkaLiniek() == PREVADZKA_LINIEK.PO_NASTUPENI_CAKA) {
                    if (vozidlo.isVozidloVoFronteVozidielCakajucichNaZastavke()) {
                        boolean odstranene = myAgent().odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(sprava);
                        if (odstranene == false) {
                            throw new RuntimeException("Vozidlo musibyt odstranene z frontu!!!");
                        }
                        myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
                    } else {
                        myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
                    }
                }
            }
        }
    }

    //meta! sender="AgentNastupuVystupu", id="217", type="Response"
    public void processVystupCestujuceho(MessageForm message) {
        Sprava sprava = (Sprava) message;
        Vozidlo vozidlo = sprava.getVozidlo();
        Cestujuci cestujuci = sprava.getCestujuci();
        vozidlo.uvolniPouzivaneDvere(cestujuci.getIndexVystupnychDveri());
        vozidlo.odstranCestujucehoNaVystup(cestujuci.getIdCestujuceho());

        Sprava copy = (Sprava) sprava.createCopy();
        copy.setAddressee(mySim().findAgent(Id.agentZastavok));
        copy.setCode(Mc.prichodCestujucehoNaStadion);
        notice(copy);

        if (vozidlo.getPocetCestujucichVAutobuseBezNastupujusichAVystupujucich() > 0) {
            myAgent().getAkciaVystupCestujuceho().execute(sprava);
        } else if (vozidlo.getCelkovyPocetCestujucichVoVozidle() == 0) {
            // vsetci vystupili
            // musim skontrolovat koniec simulacie!!!
            myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
        }

    }

    public void processFinishNaplanujPresunVozidlaNaZastavku(MessageForm message) {
        Sprava sprava = (Sprava) message;
        Vozidlo vozidlo = sprava.getVozidlo();

        if (vozidlo.getPocetNastupujucichCestujucich() == 0) {
            if (myAgent().odstranVozidloZFrontuVozidielCakajucichNaZastavkeAkTamJe(sprava)) {
                myAgent().getAkciaPresunVozidloNaDalsiuZastavku().execute(sprava);
            }
        }
    }



    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        System.out.println(message.code());
        throw new RuntimeException("Default vetva by nemala nikdy nastat");
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.init:
                processInit(message);
                break;

            case Mc.cestujuciNaZastavke:
                processCestujuciNaZastavke(message);
                break;

            case Mc.vystupCestujuceho:
                processVystupCestujuceho(message);
                break;

            case Mc.prichodZakaznikaNaZastavku:
                switch (message.sender().id()) {
                    case Id.agentModelu:
                        processPrichodZakaznikaNaZastavkuAgentModelu(message);
                        break;

                    case Id.agentZastavok:
                        processPrichodZakaznikaNaZastavkuAgentZastavok(message);
                        break;
                }
                break;

            case Mc.nastupCestujuceho:
                processNastupCestujuceho(message);
                break;

            case Mc.prichodVozidlaNaZastavku:
                processPrichodVozidlaNaZastavku(message);
                break;
            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.naplanujPresunVozidlaNaZastavku:
                        processFinishNaplanujPresunVozidlaNaZastavku(message);
                        break;
                }
                break;
            default:
                processDefault(message);
                break;
        }
    }


    //meta! tag="end"

    @Override
    public AgentPrepravy myAgent() {
        return (AgentPrepravy) super.myAgent();
    }

    @Override
    public SimulaciaDopravy mySim() {
        return (SimulaciaDopravy) super.mySim();
    }
}