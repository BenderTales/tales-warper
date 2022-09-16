package fr.bendertales.mc.taleswarper.data;

import java.lang.reflect.Array;
import java.util.Optional;


public class SizedStack<T> {

	private final int maxSize;
	private final T[] data;
	private int currentSize = 0;

	public SizedStack(Class<T> klass) {
		this(klass, 5);
	}

	public SizedStack(Class<T> klass, int size) {
		maxSize = size;
		data = (T[]) Array.newInstance(klass, size);
	}

	public void add(T datum) {
		if (currentSize >= maxSize) {
			System.arraycopy(data, 1, data, 0, maxSize-1);
			currentSize -= 1;
		}
		data[currentSize] = datum;
		currentSize += 1;
	}

	public int size() {
		return currentSize;
	}

	public Optional<T> pop() {
		if (currentSize == 0) {
			return Optional.empty();
		}
		currentSize -=1;
		var datum = data[currentSize];
		data[currentSize] = null;
		return Optional.of(datum);
	}

}
