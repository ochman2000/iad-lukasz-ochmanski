package pl.lodz.p.iad.structure;

import java.util.LinkedList;
import java.util.List;

public class Network {

	private List<Layer> layers;
	private double momentum;

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
		return this.getLayers().get(layers.size()-1);
	}
	
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}
	
	public double getMomentum() {
		return momentum;
	}
	
	public List<Layer> getHiddenLayers() {
		LinkedList<Layer> hlayers = new LinkedList<Layer>();
		for (Layer layer : this.getLayers()) {
			hlayers.add(layer);
		}
		hlayers.remove(0);
		return hlayers;
	}
	
	public String toString() {
		String s = "";
		for (Layer layer : this.getLayers()) {
			for (Neuron n : layer.getNeurons()) {
				s+=n.getID() + "\tIN: " + (String.format("%.2f", n.getLocalIn())) + "\t"+
						"OUT: " + (String.format("%.2f", n.getLocalOut())) + "\t";
				if (n instanceof HiddenLayerNeuron) {
					s+= "BIAS: " + (String.format("%.2f", layer.getBias())) + "\t";
				}
				else {
					s+= "BIAS: null" + "\t";
				}
				s+= "MOMENTUM: " + (String.format("%.2f", this.getMomentum()))+"\n";
			}
		}
		return s;
		
	}
}
