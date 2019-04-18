package simulation;

import Model.Cestujuci;
import Model.Vozidlo;
import OSPABA.*;

public class Sprava extends MessageForm {

	private Cestujuci _cestujuci = null;
	private Vozidlo _vozidlo = null;

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
		this._cestujuci = original._cestujuci;
		this._vozidlo = original._vozidlo;
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	public Cestujuci getCestujuci() {
		return _cestujuci;
	}

	public void setCestujuci(Cestujuci cestujuci) {
		this._cestujuci = cestujuci;
	}

	public Vozidlo getVozidlo() {
		return _vozidlo;
	}

	public void setVozidlo(Vozidlo vozidlo) {
		this._vozidlo = vozidlo;
	}
}