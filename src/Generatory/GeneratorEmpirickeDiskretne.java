package Generatory;

public class GeneratorEmpirickeDiskretne extends Generator<Integer> {

    EmpirickeDiskretneParameter[] parametreRozdelenia_;
    GeneratorRovnomerneDiskretne[] generatory_;

    public GeneratorEmpirickeDiskretne(EmpirickeDiskretneParameter... parametreRozdelenia) {
        this.parametreRozdelenia_ = parametreRozdelenia;
        double sucetPravdepodobnosti = 0.0;
        for(EmpirickeDiskretneParameter parameter: this.parametreRozdelenia_) {
            sucetPravdepodobnosti += parameter.getPravdepodobnostNastatia();
        }
        if (Math.abs(sucetPravdepodobnosti - 1.0) > 0.005 ) {
            throw new RuntimeException("Sucet pravdepodobnosti nie je rovny 1.0");
        }
        this.generatory_ = new GeneratorRovnomerneDiskretne[parametreRozdelenia_.length];
        int indexGenerator = 0;
        for(EmpirickeDiskretneParameter parameter: this.parametreRozdelenia_) {
            this.generatory_[indexGenerator++] = new GeneratorRovnomerneDiskretne(parameter.getMin(), parameter.getMax());
        }
    }

    @Override
    public Integer generuj() {
        double pravdepodobnost = generator_.nextDouble();
        double kumulovanaPravdepodobnost = 0.0;
        int indexGenerator = 0;

        for(EmpirickeDiskretneParameter parameter: this.parametreRozdelenia_) {
            kumulovanaPravdepodobnost += parameter.getPravdepodobnostNastatia();
            if (kumulovanaPravdepodobnost >= pravdepodobnost) {
                return this.generatory_[indexGenerator].generuj();
            }
            indexGenerator++;
        }
        throw new RuntimeException("Ziadne cislo nebolo vygenerovane");
    }
}
