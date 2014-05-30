package pl.lodz.p.iad.structure;

import java.util.LinkedList;
import java.util.List;

public class Network {

	private List<Layer> layers;
	private double momentum;
	private double learningRate;
	private boolean useBias;

	public List<Layer> getLayers() {
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	public Network() {
		this.setLayers(new LinkedList<Layer>());
		this.enableBias(true);
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
	
	public void train(double[] in, double[] expected) {
		
		if (this.getOutputLayer().getNeurons().size()!=expected.length ||
				this.getInputLayer().getNeurons().size()!=in.length) {
			throw new IllegalArgumentException();
		}
		
		for (int neuron=0; neuron<in.length; neuron++) {
			this.getInputLayer().getNeuron(neuron).setLocalOut(in[neuron]);
		}
		//layer 2
		for (int neuron=0; neuron<this.getOutputLayer().getNeurons().size(); neuron++) {
			double x2_0 = this.getOutputLayer().getNeuron(neuron).getLocalOut();
			double derivative2_0 = this.sigmoidDerivative(x2_0);
			double gradient2_0 = derivative2_0 * (expected[neuron]-x2_0);
			this.getOutputLayer().getNeuron(neuron).setGradient(gradient2_0);
		}
		
		//layer 1 (Zauważ, że liczenie gradientów dla warstw ukrytych odbywa się zupełnie inaczej)
		for (int layer=this.getNumberOfLayers()-2; layer>0; layer--) {
			for (int neuron=0; neuron<this.getLayer(layer).getNeurons().size(); neuron++) {
				
				double x1_0 = this.getLayer(layer).getNeuron(neuron).getLocalOut();
				double derivative1_0 = this.sigmoidDerivative(x1_0);
				double sumOfProducts1_0 = 0.0;
				for (int output=0; output<this.getLayer(layer).getNeuron(neuron).getOutputs().size(); output++) {
					double product1_0_0 = this.getLayer(layer).getNeuron(neuron).getOutput(output).getNext().getGradient() * this.getLayer(layer).getNeuron(neuron).getOutput(output).getWeight();
					sumOfProducts1_0 += product1_0_0;
				}
				double gradient1_0 = derivative1_0 * sumOfProducts1_0;
				this.getLayer(1).getNeuron(0).setGradient(gradient1_0);
			}
		}
	
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
	/**
	 * Metoda zwraca tablicę z wartościami wszystkich wyjść.
	 * Np. Jeśli sieć ma cztery neurony wyjściowe to:
	 * [0.00109, 0.40571, 0.17159, 0.16581]
	 * @param in
	 * @return
	 */
	public double[] test(double[] in) {
		if (this.getInputLayer().getNeurons().size()!=in.length) {
			throw new IllegalArgumentException();
		}
		for (int neuron=0; neuron<in.length; neuron++) {
			this.getInputLayer().getNeuron(neuron).setLocalOut(in[neuron]);
		}
		double[] out = new double[getOutputLayer().getNeurons().size()];
		for (int neuron=0; neuron<out.length; neuron++) {
			out[neuron] = this.getOutputLayer().getNeuron(neuron).getLocalOut();
		}
		return out;
	}
	
	/**
	 * Metoda zwraca średni błąd kwadratowy wszystkich wyjść sieci.
	 * Np. dla  [0.00109, 0.40571, 0.17159, 0.16581]
	 * MSE: 0.10253
	 * @param in
	 * @return
	 */
	public double getMSE(double[] in) {
		if (this.getInputLayer().getNeurons().size()!=in.length) {
			throw new IllegalArgumentException();
		}
		for (int neuron=0; neuron<in.length; neuron++) {
			this.getInputLayer().getNeuron(neuron).setLocalOut(in[neuron]);
		}
		double[] out = new double[getOutputLayer().getNeurons().size()];
		for (int neuron=0; neuron<out.length; neuron++) {
			out[neuron] = this.getOutputLayer().getNeuron(neuron).getLocalOut();
		}
		return Network.MSE(in, out);
	}
	
	public String test(double[] in, int decimalPlaces) {
		double[] result =  this.test(in);
		String s = "";
		for (double d : result) {
			s += String.format("%."+decimalPlaces+"f", d) + ", ";
		}
		return "["+s+"] MSE: " + String.format("%."+decimalPlaces+"f", Network.MSE(in, result));
	}
	
	public double sigmoidDerivative(double x) {
		return x*(1-x);
	}

	public boolean isBiasEnabled() {
		return useBias;
	}
	
	public void enableBias(boolean useBias) {
		this.useBias = useBias;
	}
	
	public static double MSE(double[] in, double[] out) {
		double sum_sq = 0;

		for (int i = 0; i<in.length; ++i)
		{
			double err = in[i] - out[i];
       		sum_sq += (err * err);
		}
		return sum_sq/in.length;
	}
}
