package de.hetzge.sgame.common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Predicator<T> {

	private final List<Predicate<T>> predicates = new LinkedList<>();

	private Predicator(Predicate<T>... predicates) {
		this.predicates.addAll(Arrays.asList(predicates));
	}

	public static <T> Predicator<T> of(Predicate<T>... predicates) {
		return new Predicator<T>(predicates);
	}

	public boolean all(T object) {
		for (Predicate<T> predicate : this.predicates) {
			if (!predicate.test(object)) {
				return false;
			}
		}
		return true;
	}

	public boolean any(T object) {
		for (Predicate<T> predicate : this.predicates) {
			if (predicate.test(object)) {
				return true;
			}
		}
		return false;
	}

}
