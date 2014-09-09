package p.lodz.p.iad.functions;

public class IdentityFunction implements Function {

    @Override
    public double calculate(double... x) {
        return x[0];
    }

    @Override
    public double derivative(double... x) {
        return 1.0;
    }

}
