package Stanok;

import OSPABA.Simulation;
import OSPStat.Stat;
import Simulacia.Statistiky.StatNamed;
import Stanok.Agenti.AgentModelu;
import Stanok.Agenti.AgentOkolia;
import Stanok.Agenti.AgentStanku;
import Stanok.Simulacia.Id;

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
