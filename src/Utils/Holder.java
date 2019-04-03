package Utils;

public abstract class Holder<T> {

    public abstract T getValue();

    public abstract void setValue(Number value);

    public abstract void addValue(Number value);

    public abstract void substractValue(Number value);

    public static IntHolder makeIntHolder() {
        return new IntHolder();
    }

    public static DoubleHolder makeDoubleHolder() {
        return new DoubleHolder();
    }



    public static class IntHolder extends Holder<Integer> {

        int value_;

        public IntHolder() {
            this(0);
        }

        public IntHolder(int number) {
            value_ = number;
        }

        @Override
        public Integer getValue() {
            return value_;
        }

        @Override
        public void setValue(Number value) {
            this.value_ = value.intValue();
        }

        @Override
        public void addValue(Number value) {
            value_ += value.intValue();
        }

        @Override
        public void substractValue(Number value) {
            value_ -= value.intValue();
        }

        @Override
        public String toString() {
            return "IntHolder{" +
                    "value_=" + value_ +
                    '}';
        }

    }

    public static class DoubleHolder extends Holder<Double> {

        double value_;

        public DoubleHolder() {
            this(0.0);
        }

        public DoubleHolder(double value_) {
            this.value_ = value_;
        }

        @Override
        public Double getValue() {
            return value_;
        }

        @Override
        public void setValue(Number value) {
            this.value_ = value.doubleValue();
        }

        @Override
        public void addValue(Number value) {
            value_ += value.doubleValue();
        }

        @Override
        public void substractValue(Number value) {
            value_ -= value.doubleValue();
        }

        @Override
        public String toString() {
            return "IntHolder{" +
                    "value_=" + value_ +
                    '}';
        }

    }

}
