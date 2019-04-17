package Stanok;

import OSPABA.Simulation;
import OSPStat.Stat;
import Model.Enumeracie.TYP_LINKY;
import Model.Linka;
import Model.Zastavka;
import Model.ZastavkaLinky;
import Statistiky.StatNamed;
import Stanok.Agenti.AgentModelu;
import Stanok.Agenti.AgentOkolia;
import Stanok.Agenti.AgentStanku;
import Stanok.Simulacia.Id;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimulaciaStanok extends Simulation {

    private AgentModelu agentModelu_;
    private AgentOkolia agentOkolia_;
    private AgentStanku agentStanku_;




    public StatNamed priemernyCasCakaniaZakaznikaRep_ = new StatNamed("Priemerny cas cakania zakaznika");
    public Stat priemernyCasCakaniaZakaznikaSim_ = new Stat();

    public SimulaciaStanok() {
        agentModelu_ = new AgentModelu(Id.agentModelu, this, null);
        agentOkolia_ = new AgentOkolia(Id.agentOkolia, this, agentModelu_);
        agentStanku_ = new AgentStanku(Id.agentStanku, this, agentModelu_);

        HashMap<String, Zastavka> zastavky = new HashMap<>();



        List<Zastavka> zastavkyL = Arrays.asList(
                new Zastavka("K1",3900.0/260.0, this),
                new Zastavka("K2",3900.0/210.0, this),
                new Zastavka("K3",3900.0/220.0, this),
                new Zastavka("ST", 0.0, this),
                new Zastavka("AA",3900.0/123.0, this),
                new Zastavka("AB",3900.0/92.0, this),
                new Zastavka("AC",3900.0/241.0, this),
                new Zastavka("AD",3900.0/123.0, this),
                new Zastavka("AE",3900.0/215.0, this),
                new Zastavka("AF",3900.0/245.0, this),
                new Zastavka("AG",3900.0/137.0, this),
                new Zastavka("AH",3900.0/132.0, this),
                new Zastavka("AI",3900.0/164.0, this),
                new Zastavka("AJ",3900.0/124.0, this),
                new Zastavka("AK",3900.0/213.0, this),
                new Zastavka("AL",3900.0/185.0, this),
                new Zastavka("BA",3900.0/79.0, this),
                new Zastavka("BB",3900.0/69.0, this),
                new Zastavka("BC",3900.0/43.0, this),
                new Zastavka("BD",3900.0/127.0, this),
                new Zastavka("BE",3900.0/30.0, this),
                new Zastavka("BF",3900.0/69.0, this),
                new Zastavka("BG",3900.0/162.0, this),
                new Zastavka("BH",3900.0/90.0, this),
                new Zastavka("BI",3900.0/148.0, this),
                new Zastavka("BJ",3900.0/171.0, this),
                new Zastavka("CA",3900.0/240.0, this),
                new Zastavka("CB",3900.0/310.0, this),
                new Zastavka("CC",3900.0/131.0, this),
                new Zastavka("CD",3900.0/190.0, this),
                new Zastavka("CE",3900.0/132.0, this),
                new Zastavka("CF",3900.0/128.0, this),
                new Zastavka("CG",3900.0/70.0, this)
        );
        for (Zastavka zastavka: zastavkyL) {
            zastavky.put(zastavka.getNazovZastavky(), zastavka);
        }

        Linka linkaA = new Linka(Arrays.asList(
                new ZastavkaLinky(zastavky.get("AA"), 3.2 * 60.0, zastavky.get("AB")),
                new ZastavkaLinky(zastavky.get("AB"), 2.3 * 60.0, zastavky.get("AC")),
                new ZastavkaLinky(zastavky.get("AC"), 2.1 * 60.0, zastavky.get("AD")),
                new ZastavkaLinky(zastavky.get("AD"), 1.2 * 60.0, zastavky.get("K1")),
                new ZastavkaLinky(zastavky.get("K1"), 5.4 * 60.0, zastavky.get("AE")),
                new ZastavkaLinky(zastavky.get("AE"), 2.9 * 60.0, zastavky.get("AF")),
                new ZastavkaLinky(zastavky.get("AF"), 3.4 * 60.0, zastavky.get("AG")),
                new ZastavkaLinky(zastavky.get("AG"), 1.8 * 60.0, zastavky.get("K3")),
                new ZastavkaLinky(zastavky.get("K3"), 4.0 * 60.0, zastavky.get("AH")),
                new ZastavkaLinky(zastavky.get("AH"), 1.6 * 60.0, zastavky.get("AI")),
                new ZastavkaLinky(zastavky.get("AI"), 4.6 * 60.0, zastavky.get("AJ")),
                new ZastavkaLinky(zastavky.get("AJ"), 3.4 * 60.0, zastavky.get("AK")),
                new ZastavkaLinky(zastavky.get("AK"), 1.2 * 60.0, zastavky.get("AL")),
                new ZastavkaLinky(zastavky.get("AL"), 0.9 * 60.0, zastavky.get("ST"))
        ), TYP_LINKY.LINKA_A);

        Linka linkaB = new Linka(Arrays.asList(
                new ZastavkaLinky(zastavky.get("BA"), 1.2 * 60.0, zastavky.get("BB")),
                new ZastavkaLinky(zastavky.get("BB"), 2.3 * 60.0, zastavky.get("BC")),
                new ZastavkaLinky(zastavky.get("BC"), 3.2 * 60.0, zastavky.get("BD")),
                new ZastavkaLinky(zastavky.get("BD"), 4.3 * 60.0, zastavky.get("K2")),
                new ZastavkaLinky(zastavky.get("K2"), 1.2 * 60.0, zastavky.get("BE")),
                new ZastavkaLinky(zastavky.get("BE"), 2.7 * 60.0, zastavky.get("BF")),
                new ZastavkaLinky(zastavky.get("BF"), 3.3 * 60.0, zastavky.get("K3")),
                new ZastavkaLinky(zastavky.get("K3"), 6.6 * 60.0, zastavky.get("BG")),
                new ZastavkaLinky(zastavky.get("BG"), 4.3 * 60.0, zastavky.get("BH")),
                new ZastavkaLinky(zastavky.get("BH"), 0.5 * 60.0, zastavky.get("BI")),
                new ZastavkaLinky(zastavky.get("BI"), 2.7 * 60.0, zastavky.get("BJ")),
                new ZastavkaLinky(zastavky.get("BJ"), 1.3 * 60.0, zastavky.get("ST"))
        ), TYP_LINKY.LINKA_B);

        Linka linkaC = new Linka(Arrays.asList(
                new ZastavkaLinky(zastavky.get("CA"), 0.6 * 60.0, zastavky.get("CB")),
                new ZastavkaLinky(zastavky.get("CB"), 2.3 * 60.0, zastavky.get("K1")),
                new ZastavkaLinky(zastavky.get("K1"), 4.1 * 60.0, zastavky.get("K2")),
                new ZastavkaLinky(zastavky.get("K2"), 6.6 * 60.0, zastavky.get("CC")),
                new ZastavkaLinky(zastavky.get("CC"), 2.3 * 60.0, zastavky.get("CD")),
                new ZastavkaLinky(zastavky.get("CD"), 7.1 * 60.0, zastavky.get("CE")),
                new ZastavkaLinky(zastavky.get("CE"), 4.8 * 60.0, zastavky.get("CF")),
                new ZastavkaLinky(zastavky.get("CF"), 3.7 * 60.0, zastavky.get("CG")),
                new ZastavkaLinky(zastavky.get("CG"), 7.2 * 60.0, zastavky.get("ST"))
        ), TYP_LINKY.LINKA_B);


    }


    @Override
    protected void prepareSimulation() {
        super.prepareSimulation();
    }

    @Override
    protected void prepareReplication() {
        super.prepareReplication();
        priemernyCasCakaniaZakaznikaRep_.clear();

        agentModelu_.spustiSimulaciu();
    }

    @Override
    protected void replicationFinished() {
        super.replicationFinished();
        System.out.println("Priemerny cas cakania zakaznika: " + priemernyCasCakaniaZakaznikaRep_.mean());
        priemernyCasCakaniaZakaznikaSim_.addSample(priemernyCasCakaniaZakaznikaRep_.mean());
        System.out.println("Priemerna dlzak radu: " + agentStanku_.getZakaznici().lengthStatistic().mean());
    }

    @Override
    protected void simulationFinished() {
        super.simulationFinished();
        System.out.println("Priemerny cas cakania zakaznika sim: " + priemernyCasCakaniaZakaznikaSim_.mean());
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

}
