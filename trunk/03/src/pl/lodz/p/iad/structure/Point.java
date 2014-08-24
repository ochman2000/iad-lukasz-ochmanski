package pl.lodz.p.iad.structure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Point implements Cloneable {
	
	/**
	 * Used in hashCode().
	 * Determines how precise should two points be.
	 * PRECISION = 1_000_000 means that 0.000_000_1 is equal to 0.000_000_2
	 */
	private final int PRECISION = 1_000_000; 
	
	private List<Double> coordinates;
	private Optional<Point> group;

	public Point(int size) {
		group = Optional.empty();
//		group = Optional.ofNullable(null);
		coordinates = new ArrayList<Double>(size);
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}
	
	public List<Double> getCoordinatesTrimmed() {
		List<Double> d = new ArrayList<Double>();
		for (int i=0; i<this.getCoordinates().size(); i++) {
			BigDecimal bd = new BigDecimal(this.getCoordinate(i));
		    bd = bd.setScale(2, RoundingMode.HALF_UP);
			d.add(i, bd.doubleValue());
		}
		return d;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

	public void addCoordinate(int dimension, double value) {
		this.getCoordinates().add(dimension, value);
	}

	 public void setCoordinate(int dimension, double value) {
		 this.getCoordinates().set(dimension, value);
	 }

	public double getCoordinate(int dimension) {
		return this.getCoordinates().get(dimension);
	}

	public Optional<Point> getGroup() {
		return group;
	}

	public void setGroup(Point group) {
		if (group==null)
			throw new IllegalArgumentException("Neuron nie może należeć do grupy null.");
		if (group!=null && this.getCoordinates().size()!=group.getCoordinates().size())
			throw new IllegalArgumentException("Liczba wymiarów jest niezgodna");
		this.group = Optional.ofNullable(group);
	}

	public boolean isCentroid() {
		if (!this.getGroup().isPresent())
			return false;
		else
			return this.getGroup().get() == this;
	}

	public String toString() {
		@SuppressWarnings("unused")
		String info = this.isCentroid() ? "Jest centroidem"
				: "należy do grupy centroida: " + this.getCoordinatesTrimmed();
		if (this.getGroup().isPresent()) info = "Nie jest przypisany do żadnej grupy"
				+ " centroidów.";
		return ""+this.getCoordinatesTrimmed(); // + " : " + info;
	}
	
	/**
	 * Klonowanie ma jedną wadę. W algorytmie k-uśrednień, po skopiowaniu całego
	 * zbioru danych, przynależność do klastrów będzie identyczna, a tego nie chcesz.
	 * Zapewne chciałbyś, aby metoda clone robiła również analogiczna operację
	 * przenoszenia grup to kopiowanego zbioru. Niestety tak dobrze nie ma.
	 * Przynależność do grupy jest jedynie wskaźnikiem, a wskaźnika rozmnożyć nie
	 * mogę. Zatem używaj tej metody z rozsądkiem.
	 */
	public Point clone() {
		List<Double> copy = new ArrayList<Double>();
		for (Double coordinate : this.getCoordinates()) {
			copy.add(coordinate);
		}
		Point p = new Point(this.getCoordinates().size());
		p.setCoordinates(copy);
//		if (this.getGroup()!= null) p.setGroup(this.getGroup().get());
//		this.getGroup().ifPresent(theGroup -> p.setGroup(theGroup));
		this.getGroup().ifPresent(p::setGroup);
		return p;
	}
	
	/**
	 * Zwraca odległość eukulidesową pomiędzy dwoma punktami. Obsługuje
	 * każdą liczbę wymiarów. Z powodów optymalizacyjnych, czy z lenistwa
	 * nie sprawdzam, czy sumowanie składników powoduje overflow...
	 * Zakładamy, że to się nie stanie.
	 * @param p
	 * @return
	 */
	public double getDistanceFrom(Point p) {
		if (this.getCoordinates().size()!=p.getCoordinates().size()) {
			throw new IllegalArgumentException("Punkty mają niezgodną ilość"
					+ " wymiarów.");
		}
	    double sum = 0.0;
	    for (int i=0; i<this.getCoordinates().size(); i++) {
			double odleglosc = p.getCoordinate(i) - this.getCoordinate(i);
			double pq = Math.pow(odleglosc, 2);
		    sum+=pq;
	    }
	    return Math.sqrt(sum);
	}
	
	/**
	 * Funkcja zwraca odległość wektora wejściowego od wyjściowego.
	 * Zwracana wartość jest równoważna odległości euklidesowej
	 * pomiędzy wagami dwu wektorów (We, Wy). W odróżnieniu od funkcji
	 * getDistanceFrom() ta funkcja jest znormalizowana do przedziału (-1, 1).
	 * @param p
	 * @return
	 */
	public double getEuclideanDistanceFrom(Point p) {
		double value = getDistanceFrom(p);
		if (value<=0.0 || value>=2.0)
			throw new RuntimeException("Output out of range: "+value);
		return value;
	}
	
	private double getNormalizationFactor() {
		double vectorLength = 0.0;
		for (Double coord : this.getCoordinates()) {
			vectorLength += coord*coord;
		}
		//TO JEST KONIECZNE, ŻEBY POTEM NIE DZIELIĆ PRZEZ LICZBĘ BLISKĄ ZERO
		if(vectorLength<1.E-30) vectorLength=1.E-30;
		return 1/(Math.sqrt(vectorLength));
	}
	
	@Override
    public int hashCode() {
		double sum = 0.0;
		for (double coordinate : getCoordinates()) {
			sum+=coordinate;
			sum++;
		}
		return (int) (sum*PRECISION);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj.hashCode()==this.hashCode()) {
			return true;
		}
		return false;
	}

	public Point getNormalized() {
		Point p = new Point(this.getCoordinates().size());
		for (int wymiar=0; wymiar<this.getCoordinates().size(); wymiar++) {
			double coord = this.getCoordinate(wymiar);
			double value = this.getNormalizationFactor()*coord;
			if (value<=-1.0 || value>=1.0)
				throw new RuntimeException("Output out of range: "+value);
			p.addCoordinate(wymiar, value);
			this.getGroup().ifPresent(p::setGroup);
		}
		return p;
	}
}
