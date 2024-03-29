package pl.lodz.p.iad.structure;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Point implements Cloneable, Functional<Point> {

	/**
	 * Used in hashCode(). Determines how precise should two points be.
	 * PRECISION = 1_000_000 means that 0.000_000_1 is equal to 0.000_000_2
	 */
	private final int PRECISION = 1_000_000;
	private final int CZAS_ZYCIA = 2;
	
	private List<Double> coordinates;
	private Optional<Point> group;
	private Optional<Color> color;
	private int won = 0;

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
		for (int i = 0; i < this.getCoordinates().size(); i++) {
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
		if (group == null)
			throw new IllegalArgumentException(
					"Neuron nie może być przypisany do grupy" + " null.");
		if (group != null
				&& this.getCoordinates().size() != group.getCoordinates()
						.size())
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
		if (this.getGroup().isPresent())
			info = "Nie jest przypisany do żadnej grupy" + " centroidów.";
		return "" + this.getCoordinatesTrimmed(); // + " : " + info;
	}

	/**
	 * Klonowanie ma jedną wadę. W algorytmie k-uśrednień, po skopiowaniu całego
	 * zbioru danych, przynależność do klastrów będzie identyczna, a tego nie
	 * chcesz. Zapewne chciałbyś, aby metoda clone robiła również analogiczna
	 * operację przenoszenia grup to kopiowanego zbioru. Niestety tak dobrze nie
	 * ma. Przynależność do grupy jest jedynie wskaźnikiem, a wskaźnika
	 * rozmnożyć nie mogę. Zatem używaj tej metody z rozsądkiem.
	 */
	public Point clone() {
		List<Double> copy = new ArrayList<Double>();
		for (Double coordinate : this.getCoordinates()) {
			copy.add(coordinate);
		}
		Point p = new Point(this.getCoordinates().size());
		p.setCoordinates(copy);
		p.setWon(this.getWon());
		p.setColor(this.getColor());
		this.getGroup().ifPresent(p::setGroup);
		return p;
	}

	/**
	 * Zwraca odległość eukulidesową pomiędzy dwoma punktami. Obsługuje każdą
	 * liczbę wymiarów. Z powodów optymalizacyjnych, czy z lenistwa nie
	 * sprawdzam, czy sumowanie składników powoduje overflow... Zakładam, że to
	 * się nie stanie.
	 * 
	 * @param p
	 * @return
	 */
	public double getDistanceFrom(Point p) {
		if (p==null) throw new NoSuchElementException("Punkt jest null.");
		if (this.getCoordinates().size() != p.getCoordinates().size()) {
			throw new IllegalArgumentException("Punkty mają niezgodną ilość"
					+ " wymiarów.");
		}
		double sum = 0.0;
		for (int i = 0; i < this.getCoordinates().size(); i++) {
			double odleglosc = p.getCoordinate(i) - this.getCoordinate(i);
			double pq = Math.pow(odleglosc, 2);
			if (!Double.isFinite(pq))
				throw new ArithmeticException("Potęgowanie się sypnęło.");
			sum  += pq;
		}
		double pierw = Math.sqrt(sum);
		if (!Double.isFinite(pierw))
			throw new ArithmeticException("Pierwiastkowanie się sypnęło.");
		return pierw;
	}

	/**
	 * Funkcja zwraca odległość wektora wejściowego od wyjściowego. Zwracana
	 * wartość jest równoważna odległości euklidesowej pomiędzy wagami dwu
	 * wektorów (We, Wy). W odróżnieniu od funkcji getDistanceFrom() ta funkcja
	 * jest znormalizowana do przedziału (0, 2), ponieważ różnica pomiędzy
	 * maksymalnymi wartościami wektorów (-1, 1) to właśnie 2.
	 * 
	 * @param p
	 * @return
	 */
	public double getEuclideanDistanceFrom(Point p) {
		double value = getDistanceFrom(p);
//		if (value < 0.0 || value >= 2.0)
//			throw new RuntimeException("Output out of range: " + value);
		return value;
	}

	private double getNormalizationFactor() {
		double vectorLength = 0.0;
		for (Double coord : this.getCoordinates()) {
			vectorLength += coord * coord;
		}
		// TO JEST KONIECZNE, ŻEBY POTEM NIE DZIELIĆ PRZEZ LICZBĘ BLISKĄ ZERO
		if (vectorLength < 1.E-30)
			vectorLength = 1.E-30;
		return 1 / (Math.sqrt(vectorLength));
	}

	@Override
	public int hashCode() {
		double sum = 0.0;
		for (double coordinate : getCoordinates()) {
			sum += coordinate;
			sum++;
		}
		return (int) (sum * PRECISION);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj.hashCode() == this.hashCode()) {
			return true;
		}
		return false;
	}

	public Point getNormalized() {
		return getScaled(this.getNormalizationFactor());
	}
	
	public Point getScaled(double factor) {
		Point p = this.clone();
		for (int wymiar = 0; wymiar < this.getCoordinates().size(); wymiar++) {
			double coord = this.getCoordinate(wymiar);
			double value = factor * coord;
			if (value <= -1.0 || value >= 1.0)
				throw new RuntimeException("Output out of range: " + value);
			p.setCoordinate(wymiar, value);
		}
		return p;
	}
	
	

	public int getWon() {
		if (won>=CZAS_ZYCIA) throw new IllegalStateException("Niedozwolona liczba"
				+ "zwycięstw neuronu: "+won);
		return won;
	}

	private void setWon(int won) {
		this.won = won;
	}
	
	/**
	 * Jeśli neuron wygrał już maksymalną dozwoloną liczbę razy
	 * zostanie wyłączony i będzie pauzował przez jeden cykl.
	 */
	public void odnotujZwyciestwo() {
		setWon((getWon()+1)<CZAS_ZYCIA ? getWon()+1 : -1);
	}

	public Optional<Color> getColor() {
		return color;
	}

	public void setColor(Optional<Color> color) {
		this.color = color;
	}

	@Override
	public int compareByEuclideanDistance(Point o1, Point o2) {
		return Double.compare(this.getEuclideanDistanceFrom(o1),
				this.getEuclideanDistanceFrom(o2));
	}
}
