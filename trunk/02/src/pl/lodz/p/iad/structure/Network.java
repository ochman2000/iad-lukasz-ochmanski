package pl.lodz.p.iad.structure;

import java.util.LinkedList;
import java.util.List;

public class Network {

	private List<Layer> layers;

	public List<Layer> getLayers() {
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	public Network() {
		layers = new LinkedList<Layer>();
	}
	
	public void addLayer(int neurons) {
		Layer newLayer = new Layer(neurons);
		this.getLayers().add(newLayer);
	}
	
	public void addLayer(Layer layer) {
		this.getLayers().add(layer);
	}
	
	public void setLayer(int index, int neurons) {
		Layer newLayer = new Layer(neurons);
		layers.set(index, newLayer);
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
		Layer newLayer = new Layer(neurons);
		int index = this.getNumberOfLayers() - 2;
		this.getLayers().add(index, newLayer);
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
