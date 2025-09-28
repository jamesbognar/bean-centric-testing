package org.bct.api;

import org.junit.jupiter.api.*;

/**
 * Base test class that provides common test configuration for all test classes in the junit package.
 *
 * <p>This class configures test method ordering to use method names, ensuring consistent and predictable
 * test execution order across all test classes in the package.</p>
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public abstract class TestBase {
	// Base class for common test configuration
}
