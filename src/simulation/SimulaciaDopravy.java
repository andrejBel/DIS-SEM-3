package simulation;

import OSPABA.*;
import agents.*;

public class SimulaciaDopravy extends Simulation {


	public SimulaciaDopravy() {
	    init();
	}

	@Override
	public void prepareSimulation() {
		super.prepareSimulation();
		// Create global statistcis
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

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
		setAgentPrepravy(new AgentPrepravy(Id.agentPrepravy, this, agentModelu()));
		setAgentPohybu(new AgentPohybu(Id.agentPohybu, this, agentPrepravy()));
		setAgentZastavok(new AgentZastavok(Id.agentZastavok, this, agentPrepravy()));
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

	//meta! tag="end"
}