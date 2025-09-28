package org.bct.api;

import static org.bct.api.Utils.*;

/**
 * Exception thrown when a requested property cannot be found on an object.
 *
 * <p>This exception is typically thrown by {@link PropertyExtractor} implementations
 * when attempting to access a property that does not exist on the target object.</p>
 *
 * <h5 class='section'>Examples:</h5>
 * <p class='bjava'>
 *   <jc>// Property extraction that may throw PropertyNotFoundException</jc>
 *   <jk>try</jk> {
 *      String <jv>value</jv> = <jv>converter</jv>.getNested(<jv>object</jv>, <jsm>tokenize</jsm>(<js>"nonExistentProperty"</js>).get(0));
 *   } <jk>catch</jk> (PropertyNotFoundException <jv>e</jv>) {
 *      <jc>// Handle missing property gracefully</jc>
 *      System.<jsm>err</jsm>.println(<js>"Property not found: "</js> + <jv>e</jv>.getMessage());
 *   }
 * </p>
 *
 * <p class='bjava'>
 *   <jc>// Testing for PropertyNotFoundException</jc>
 *   <jsm>assertThrows</jsm>(PropertyNotFoundException.<jk>class</jk>, () -> {
 *      <jv>converter</jv>.getNested(<jv>bean</jv>, <jsm>tokenize</jsm>(<js>"invalidField"</js>).get(0));
 *   });
 * </p>
 */
public class PropertyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new PropertyNotFoundException with the specified detail message.
	 *
	 * @param message The detail message describing the missing property
	 */
	public PropertyNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new PropertyNotFoundException with the specified detail message and cause.
	 *
	 * @param message The detail message describing the missing property
	 * @param cause The underlying cause of the exception
	 */
	public PropertyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new PropertyNotFoundException for a specific property name and object type.
	 *
	 * @param propertyName The name of the property that could not be found
	 * @param objectType The type of object on which the property was sought
	 */
	public PropertyNotFoundException(String propertyName, Class<?> objectType) {
		super(f("Property ''{0}'' not found on object of type {1}", propertyName, objectType.getSimpleName()));
	}

	/**
	 * Constructs a new PropertyNotFoundException for a specific property name and object type with a cause.
	 *
	 * @param propertyName The name of the property that could not be found
	 * @param objectType The type of object on which the property was sought
	 * @param cause The underlying cause of the exception
	 */
	public PropertyNotFoundException(String propertyName, Class<?> objectType, Throwable cause) {
		super(f("Property ''{0}'' not found on object of type {1}", propertyName, objectType.getSimpleName()) ,cause);
	}
}
