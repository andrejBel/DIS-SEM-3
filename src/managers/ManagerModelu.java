package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;
import sun.plugin2.message.Message;

//meta! id="1"
public class ManagerModelu extends Manager {
	public ManagerModelu(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentOkolia", id="18", type="Notice"
	public void processPrichodZakaznikaNaZastavku(MessageForm message) {
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
		throw new RuntimeException("Tu sa nedostanes");
	}


	public void init() {
	}

	@Override
	public void processMessage(MessageForm message) {
		switch (message.code()) {
		case Mc.prichodZakaznikaNaZastavku:
		{

			break;
		}
		case Mc.init:
		{
			// prvu mozem preposlat priamo, druhu musim skopirovat
			{
				Sprava sprava = (Sprava) message;
				sprava.setAddressee(mySim().findAgent(Id.agentPrepravy));
				notice(sprava);
			}
			{
				Sprava copy = (Sprava) message.createCopy();
				copy.setAddressee(mySim().findAgent(Id.agentOkolia));
				notice(copy);
			}

			break;
		}

		default:
			processDefault(message);
		break;
		}
	}

	@Override
	public AgentModelu myAgent() {
		return (AgentModelu)super.myAgent();
	}

	//meta! tag="end"

}