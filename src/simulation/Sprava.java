package simulation;

import OSPABA.*;

public class Sprava extends MessageForm {
	public Sprava(Simulation sim) {
		super(sim);
	}

	public Sprava(Sprava original) {
		super(original);
		// copy() is called in superclass
	}

	@Override
	public MessageForm createCopy() {
		return new Sprava(this);
	}

	@Override
	protected void copy(MessageForm message) {
		super.copy(message);
		Sprava original = (Sprava)message;
		// Copy attributes
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}
}