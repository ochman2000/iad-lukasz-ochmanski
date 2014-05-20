package pl.lodz.p.iad.structure;

import java.util.LinkedList;
import java.util.List;

public class Network {

//    Our neuron values
	private List<Layer> layers;
//    Our weights
//    Our weight changes
//    Our error gradients


	public Network() {
		layers = new LinkedList<Layer>();
	}
	
	private void addLayer(int index, int neurons) {
		Layer newLayer = new Layer(neurons);
		layers.add(index, newLayer);
	}
	
	public void setInputLayer(int neurons) {
		Layer newLayer = new Layer(neurons);
		if (getNumberOfLayers()==0)
			layers.add(newLayer);
		else
			layers.set(0, newLayer);
	}
	
	public void setOutputLayer(int neurons) {
		Layer newLayer = new Layer(neurons);
		if (getNumberOfLayers()<2)
			layers.add(newLayer);
		else
			layers.set(getNumberOfLayers()-1, newLayer);
	}
	
	public void addHiddenLayer(int neurons) {
		int index = this.getNumberOfLayers() - 2;
		this.addLayer(index, neurons);
	}
	
	public Layer getLayer(int index) {
		return layers.get(index);
	}
	
	public int getNumberOfLayers() {
		return layers.size();
	}
	public Layer getInputLayer() {
		return layers.get(0);
	}
	public Layer getOutputLayer() {
		return layers.get(layers.size());
	}
}
