package pl.lodz.p.iad.structure;

import java.util.LinkedList;
import java.util.List;

public class Network {

	private List<Layer> layers;
	private double momentum;
	private double learningRate;

	public List<Layer> getLayers() {
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	public Network() {
		this.setLayers(new LinkedList<Layer>());
	}
	
	public void addLayer(int neurons) {
		Layer newLayer = new Layer(neurons);
		this.addLayer(newLayer);
	}
	
	public void addLayer(Layer layer) {
		layer.setNetwork(this);
		this.getLayers().add(layer);
	}
	
	public void setLayer(int index, int neurons) {
		Layer newLayer = new Layer(neurons);
		this.getLayers().set(index, newLayer);
	}
	
//	public void setBias(double bias) {
//		for (Layer layer : this.getLayers()) {
//			for (Neuron neuron : layer.getNeurons()) {
//				if (neuron instanceof HiddenLayerNeuron) {
//					neuron.setBias(bias);
//				}
//			}
//		}
//	}
	
//	public void setInputLayer(int neurons) {
//		Layer newLayer = new Layer(neurons);
//		if (getNumberOfLayers()==0)
//			this.getLayers().add(newLayer);
//		else
//			this.getLayers().set(0, newLayer);
//	}
//	
//	public void setOutputLayer(int neurons) {
//		Layer newLayer = new Layer(neurons);
//		if (getNumberOfLayers()<2)
//			this.getLayers().add(newLayer);
//		else
//			this.getLayers().set(getNumberOfLayers()-1, newLayer);
//	}
//	
//	public void addHiddenLayer(int neurons) {
//		Layer newLayer = new Layer(neurons);
//		int index = this.getNumberOfLayers() - 2;
//		this.getLayers().add(index, newLayer);
//	}
	
	public Layer getLayer(int index) {
		return this.getLayers().get(index);
	}
	
	public int getNumberOfLayers() {
		return this.getLayers().size();
	}
	public Layer getInputLayer() {
		return this.getLayers().get(0);
	}
	public Layer getOutputLayer() {
		return this.getLayer(layers.size()-1);
	}
	
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}
	
	public double getMomentum() {
		return momentum;
	}
	
//	public List<Layer> getHiddenLayers() {
//		LinkedList<Layer> hlayers = new LinkedList<Layer>();
//		for (Layer layer : this.getLayers()) {
//			hlayers.add(layer);
//		}
//		hlayers.remove(0);
//		return hlayers;
//	}
	
	public String toString() {
		String s = "";
		for (Layer layer : this.getLayers()) {
			for (Neuron n : layer.getNeurons()) {
				s+=n.getID() + "\tIN: " + (String.format("%.2f", n.getLocalIn())) + "\t"+
						"OUT: " + (String.format("%.2f", n.getLocalOut())) + "\t";
				if (n instanceof HiddenLayerNeuron) {
					s+= "BIAS: " + (String.format("%.2f", n.getBias())) + "\t";
				}
				else {
					s+= "BIAS: null" + "\t";
				}
				s+= "MOMENTUM: " + (String.format("%.2f", this.getMomentum()))+"\n";
			}
		}
		return s;
		
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	
	public void train() {
		double[] in = {1.0, 2.0, 3.0};
		double[] out = {-0.85, 0.75};
		
		//layer 2
		double x2_0 = this.getOutputLayer().getNeuron(0).getLocalOut();
		double derivative2_0 = this.sigmoidDerivative(x2_0);
		double gradient2_0 = derivative2_0 * (out[0]-x2_0);
		this.getLayer(2).getNeuron(0).setGradient(gradient2_0);
		
		double x2_1 = this.getOutputLayer().getNeuron(1).getLocalOut();
		double derivative2_1 = this.sigmoidDerivative(x2_1);
		double gradient2_1 = derivative2_1 * (out[0]-x2_1);
		this.getLayer(2).getNeuron(1).setGradient(gradient2_1);
		
		//layer 1 (Zauważ, że liczenie gradientów dla warstw ukrytych odbywa się zupełnie inaczej)
		double x1_0 = this.getLayer(1).getNeuron(0).getLocalOut();
		double derivative1_0 = this.sigmoidDerivative(x1_0);
		double product1_0_0 = gradient2_0 * this.getLayer(1).getNeuron(0).getOutput(0).getWeight();
		double product1_0_1 = gradient2_1 * this.getLayer(1).getNeuron(0).getOutput(1).getWeight();
		double sumOfProducts1_0 = (product1_0_0+product1_0_1);
		double gradient1_0 = derivative1_0 * sumOfProducts1_0;
		this.getLayer(1).getNeuron(0).setGradient(gradient1_0);
		
		double x1_1 = this.getLayer(1).getNeuron(1).getLocalOut();
		double derivative1_1 = this.sigmoidDerivative(x1_1);
		double product1_1_0 = gradient2_0 * this.getLayer(1).getNeuron(1).getOutput(0).getWeight();
		double product1_1_1 = gradient2_1 * this.getLayer(1).getNeuron(1).getOutput(1).getWeight();
		double sumOfProducts1_1 = (product1_1_0+product1_1_1);
		double gradient1_1 = derivative1_1 * sumOfProducts1_1;
		this.getLayer(1).getNeuron(1).setGradient(gradient1_1);
		
		double x1_2 = this.getLayer(1).getNeuron(2).getLocalOut();
		double derivative1_2 = this.sigmoidDerivative(x1_2);
		double product1_2_0 = gradient2_0 * this.getLayer(1).getNeuron(2).getOutput(0).getWeight();
		double product1_2_1 = gradient2_1 * this.getLayer(1).getNeuron(2).getOutput(1).getWeight();
		double sumOfProducts1_2 = (product1_2_0+product1_2_1);
		double gradient1_2 = derivative1_2 * sumOfProducts1_2;
		this.getLayer(1).getNeuron(2).setGradient(gradient1_2);
		
		double x1_3 = this.getLayer(1).getNeuron(3).getLocalOut();
		double derivative1_3 = this.sigmoidDerivative(x1_3);
		double product1_3_0 = gradient2_0 * this.getLayer(1).getNeuron(3).getOutput(0).getWeight();
		double product1_3_1 = gradient2_1 * this.getLayer(1).getNeuron(3).getOutput(1).getWeight();
		double sumOfProducts1_3 = (product1_3_0+product1_3_1);
		double gradient1_3 = derivative1_3 * sumOfProducts1_3;
		this.getLayer(1).getNeuron(3).setGradient(gradient1_3);
	
//		dla warstwy pierwszej nie liczy się gradientów
		
		// COMPUTING THE WEIGHT AND BIAS DELTAS
		//LAYER 0-1  3X4
		for (int layer=0; layer<this.getNumberOfLayers()-1; layer++) {
			for (int neuron=0; neuron<this.getLayer(layer).getNeurons().size(); neuron++) {
				for (int edge=0; edge<this.getLayer(layer).getNeuron(neuron).getOutputs().size(); edge++) {
				
					double input0_0 = this.getLayer(layer).getNeuron(neuron).getLocalOut();
					double deltaWeight0_0_0 = this.getLearningRate() 
							* this.getLayer(layer+1).getNeuron(edge).getGradient() * input0_0;
					double newWeight0_0_0 = deltaWeight0_0_0 + this.getLayer(layer).getNeuron(neuron).getOutput(edge).getWeight();
					newWeight0_0_0 += this.getMomentum() * this.getLayer(layer).getNeuron(neuron).getOutput(edge).getPreviousDelta();
					this.getLayer(layer).getNeuron(neuron).getOutput(edge).setWeight(newWeight0_0_0);
					this.getLayer(layer).getNeuron(neuron).getOutput(edge).setPreviousDelta(deltaWeight0_0_0);
				}
			}
		}

		//BIAS
		//x4
		for (int layer=1; layer<this.getNumberOfLayers(); layer++) {
			for (int neuron=0; neuron<this.getLayer(layer).getNeurons().size(); neuron++) {
				double deltaBias1_0 = this.getLearningRate() * this.getLayer(layer).getNeuron(neuron).getGradient();
				double newBias1_0 = deltaBias1_0 + this.getLayer(layer).getNeuron(neuron).getBias();
				newBias1_0 += this.getMomentum() * this.getLayer(layer).getNeuron(neuron).getPreviousDelta();
				this.getLayer(layer).getNeuron(neuron).setBias(newBias1_0);
				this.getLayer(layer).getNeuron(neuron).setPreviousDelta(deltaBias1_0);
			}
		}
	}
	
	public double sigmoidDerivative(double x) {
		return x*(1-x);
	}
}
