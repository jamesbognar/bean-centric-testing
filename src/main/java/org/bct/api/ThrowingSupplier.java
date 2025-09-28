package org.bct.api;

/**
 * A supplier that throws an exception.
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {
	T get() throws Exception;
}
