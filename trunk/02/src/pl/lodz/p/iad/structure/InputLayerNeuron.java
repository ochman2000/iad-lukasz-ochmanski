package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;


public class InputLayerNeuron implements Neuron {
	private List<Edge> input;
	private List<Edge> output;
	private double localIn;
	private double localOut;
	private double momentum;
	private String ID;
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void setID(String iD) {
		ID = iD;
	}

	public InputLayerNeuron() {
		this(2,2);
	}

	public InputLayerNeuron(int in, int out) {
		input = new ArrayList<Edge>(in);
		output = new ArrayList<Edge>(out);
	}

	@Override
	public List<Edge> getInputs() {
		return input;
	}

	@Override
	public void setInputs(List<Edge> input) {
		this.input = input;
	}

	@Override
	public List<Edge> getOutputs() {
		return output;
	}

	@Override
	public void setOutputs(List<Edge> output) {
		this.output = output;
	}
	
	@Override
	public void addNeuronOut(Neuron neuron) {
		Edge edge = new Edge();
		
		neuron.getInputs().add(edge);
		edge.setNext(neuron);
			
		this.getOutputs().add(edge);
		edge.setPrev(this);
	}
	
	@Override
	public double getLocalIn() {
		return localIn;
	}

	@Override
	public double getLocalOut() {
		return localOut;
	}
	
	@Override
	public void setLocalOut(double localOut) {
		this.localOut = localOut;
	}

	@Override
	public double getMomentum() {
		return momentum;
	}

	@Override
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}
	
	@Override
	public double getBias() {
		throw new UnsupportedOperationException("Nie ma BIASu w warstwie wejściowej.");
	}
	
	@Override
	public void setBias(double bias) {
		throw new UnsupportedOperationException("Nie można ustawić BIASu dla warstwy wejściowej.");
	}
	
	public String toString() {
		String id = this.getID()==null ? "Uknown neuron" : this.getID(); id+="\n";
		String bias = "Bias: " +this.getBias() + "\n";
		String momentum = "Momentum: " + this.getMomentum() + "\n";
		String in = "Wejście: " + this.getLocalIn() + "\n";
		String out = "Wyjście: " + this.getLocalOut() + "\n";
		
		return 	"=======================================\n" +
				id+bias+momentum+in+out+		
				"=======================================\n";
	}

	@Override
	public double getWeightOut() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWeightOut(double weightOut) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge getInput(int index) {
		return this.getInputs().get(index);
	}

	@Override
	public Edge getOutput(int index) {
		return this.getOutputs().get(index);
	}
}
