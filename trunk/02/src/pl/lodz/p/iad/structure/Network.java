package pl.lodz.p.iad.structure;

import java.util.ArrayList;
import java.util.List;

public class Network {

//    Our neuron values
	private List<Layer> layers;
//    Our weights
//    Our weight changes
//    Our error gradients


	public Network() {
		layers = new ArrayList<Layer>(3);
	}
	
	public void addLayer(int neurons) {
		Layer newLayer = new Layer(neurons);
		layers.add(newLayer);
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
