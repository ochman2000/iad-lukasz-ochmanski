package pl.lodz.p.iad;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import pl.lodz.p.iad.structure.Edge;
import pl.lodz.p.iad.structure.HiddenLayerNeuron;
import pl.lodz.p.iad.structure.InputLayerNeuron;
import pl.lodz.p.iad.structure.Layer;
import pl.lodz.p.iad.structure.Network;
import pl.lodz.p.iad.structure.Neuron;

public class SampleTest {

	@Test
	public void test01() {
		Network network = this.getNetwork();
		double weight01 = network.getLayer(1).getNeuron(0).getInputs().get(0).getWeight();
		double weight02 = network.getLayer(1).getNeuron(0).getInputs().get(1).getWeight();
		double weight03 = network.getLayer(1).getNeuron(0).getInputs().get(2).getWeight();
		double bias = network.getLayer(1).getNeuron(0).getBias();
		
		double in = 1.0 * weight01 + 2.0 * weight02 + 3.0 * weight03 + bias;
		double calculated = network.getLayer(1).getNeuron(0).getLocalIn();
//		System.out.println(in+" "+calculated);
		Assert.assertEquals(in, calculated, 0.01);
	}
	
	@Test
	public void test02() {
		Network network = this.getNetwork();
		
		double in = network.getLayer(1).getNeuron(0).getLocalIn();
		double calculated = 1.0 / (1.0 + Math.exp(-(in)));
		double out = network.getLayer(1).getNeuron(0).getLocalOut();
		System.out.println(out+" "+calculated);
		Assert.assertEquals(out, calculated, 0.01);
	}
	
	/**
	 * Sprawdzam kolejną warstwę.
	 */
	@Test
	public void test03() {
		Network network = this.getNetwork();
		double weight01 = network.getLayer(2).getNeuron(0).getInputs().get(0).getWeight();
		double val01 = network.getLayer(1).getNeuron(0).getLocalOut();
		double weight02 = network.getLayer(2).getNeuron(0).getInputs().get(1).getWeight();
		double val02 = network.getLayer(1).getNeuron(1).getLocalOut();
		double weight03 = network.getLayer(2).getNeuron(0).getInputs().get(2).getWeight();
		double val03 = network.getLayer(1).getNeuron(2).getLocalOut();
		double weight04 = network.getLayer(2).getNeuron(0).getInputs().get(3).getWeight();
		double val04 = network.getLayer(1).getNeuron(3).getLocalOut();
		double bias = network.getLayer(2).getNeuron(0).getBias();
		
		double in = val01 * weight01 + val02 * weight02 + val03 * weight03 + val04 * weight04 + bias;
		double calculated = network.getLayer(2).getNeuron(0).getLocalIn();
		System.out.println(in+" "+calculated);
		Assert.assertEquals(in, calculated, 0.01);
	}
	
	@Test
	public void test04() {
		Network network = this.getNetwork();
		
		double in = network.getLayer(2).getNeuron(0).getLocalIn();
		double calculated = 1.0 / (1.0 + Math.exp(-(in)));
		double out = network.getLayer(2).getNeuron(0).getLocalOut();
		System.out.println(out+" "+calculated);
		Assert.assertEquals(out, calculated, 0.01);
	}
	
	@Test
	public void test05() {
		//sprawdzić czy prev = next
		Network network = this.getNetwork();
		Neuron valPrev = network.getLayer(2).getNeuron(0).getInput(0).getPrev();
		Neuron valNext = network.getLayer(1).getNeuron(0);
		assertTrue(valPrev==valNext);
	}
	
	//input = output
	@Test
	public void test06() {
		//sprawdzić czy prev = next
		Network network = this.getNetwork();
		Edge valPrev = network.getLayer(2).getNeuron(0).getInput(0);
		Edge valNext = network.getLayer(1).getNeuron(0).getOutput(0);
		assertTrue(valPrev==valNext);
	}
	
	public Network getNetwork() {
		Network network = new Network();
		
		//INPUT LAYER
		Layer layer0 = new Layer(3);
		Neuron neuron_0_0 = new InputLayerNeuron(); neuron_0_0.setID("[0-0]");
		Neuron neuron_0_1 = new InputLayerNeuron(); neuron_0_1.setID("[0-1]");
		Neuron neuron_0_2 = new InputLayerNeuron(); neuron_0_2.setID("[0-2]");
		neuron_0_0.setLocalOut(1.0);
		neuron_0_1.setLocalOut(2.0);
		neuron_0_2.setLocalOut(3.0);
		layer0.add(neuron_0_0);
		layer0.add(neuron_0_1);
		layer0.add(neuron_0_2);
		
		//HIDDEN LAYER
		Layer layer1 = new Layer(4);
		Neuron neuron_1_0 = new HiddenLayerNeuron(); neuron_1_0.setID("[1-0]");
		Neuron neuron_1_1 = new HiddenLayerNeuron(); neuron_1_1.setID("[1-1]");
		Neuron neuron_1_2 = new HiddenLayerNeuron(); neuron_1_2.setID("[1-2]");
		Neuron neuron_1_3 = new HiddenLayerNeuron(); neuron_1_3.setID("[1-3]");
		layer1.add(neuron_1_0);
		layer1.add(neuron_1_1);
		layer1.add(neuron_1_2);
		layer1.add(neuron_1_3);
		
		//OUTPUT LAYER
		Layer layer2 = new Layer(2);
		Neuron neuron_2_0 = new HiddenLayerNeuron(); neuron_2_0.setID("[2-0]");
		Neuron neuron_2_1 = new HiddenLayerNeuron(); neuron_2_1.setID("[2-1]");
		layer2.add(neuron_2_0);
		layer2.add(neuron_2_1);
		
		//CONNECTIONS
		neuron_0_0.addNeuronOut(neuron_1_0);
		neuron_0_0.addNeuronOut(neuron_1_1);
		neuron_0_0.addNeuronOut(neuron_1_2);
		neuron_0_0.addNeuronOut(neuron_1_3);
		
		neuron_0_1.addNeuronOut(neuron_1_0);
		neuron_0_1.addNeuronOut(neuron_1_1);
		neuron_0_1.addNeuronOut(neuron_1_2);
		neuron_0_1.addNeuronOut(neuron_1_3);
		
		neuron_0_2.addNeuronOut(neuron_1_0);
		neuron_0_2.addNeuronOut(neuron_1_1);
		neuron_0_2.addNeuronOut(neuron_1_2);
		neuron_0_2.addNeuronOut(neuron_1_3);
		
		neuron_1_0.addNeuronOut(neuron_2_0);
		neuron_1_0.addNeuronOut(neuron_2_1);
		
		neuron_1_1.addNeuronOut(neuron_2_0);
		neuron_1_1.addNeuronOut(neuron_2_1);
		
		neuron_1_2.addNeuronOut(neuron_2_0);
		neuron_1_2.addNeuronOut(neuron_2_1);
		
		neuron_1_3.addNeuronOut(neuron_2_0);
		neuron_1_3.addNeuronOut(neuron_2_1);
		
		network.addLayer(layer0);
		layer1.setBias(-7.0);
		network.addLayer(layer1);
		layer2.setBias(-7.0);
		network.addLayer(layer2);
		
		network.setMomentum(1.0);
		
		return network;
	}

}
