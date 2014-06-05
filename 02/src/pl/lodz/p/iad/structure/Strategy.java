package pl.lodz.p.iad.structure;


public interface Strategy {
	Input[] readDataFromFile(Network network, String fileName);
	Network initializeStructure();
}
