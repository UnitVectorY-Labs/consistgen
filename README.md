[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Active](https://img.shields.io/badge/Status-Active-green)](https://guide.unitvectorylabs.com/bestpractices/status/#active) [![javadoc](https://javadoc.io/badge2/com.unitvectory/consistgen/javadoc.svg)](https://javadoc.io/doc/com.unitvectory/consistgen) [![codecov](https://codecov.io/gh/UnitVectorY-Labs/consistgen/graph/badge.svg?token=FaZOUbYgks)](https://codecov.io/gh/UnitVectorY-Labs/consistgen)

# consistgen

This simple Java library provides static and dynamic implementations for generating timestamps and UUIDs, offering a structured way to inject predictable data into test cases.

## Purpose

Directly calling methods like `System.currentTimeMillis()` or `UUID.randomUUID()` can make it difficult to test code that relies on these methods as their output is unpredictable. By using the interfaces and classes provided by this library, which yes are just wrappers on these underlying methods, you can utilize dependency injection to provide static responses for these methods in your test cases while allowing them to behave normally in production.

While mocking frameworks like Mockito can be used to mock these methods, this library provides a simple way to inject predictable data into your test cases without the need for additional dependencies for these simple use cases.

## Getting Started

This library requires Java 17 and is available in the Maven Central Repository:

```xml
<dependency>
    <groupId>com.unitvectory</groupId>
    <artifactId>consistgen</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Dependency Injection

The `EpochTimeProvider` interface implementation wraps the `System.currentTimeMillis()` method, allowing you to obtain the current system time in milliseconds or seconds. Similarly, the `UuidGenerator` interface implementation wraps the `UUID.randomUUID().toString()` function to generate UUIDs.

The following example is a program demonstrating how to use dependency injection to insulate the implementation from the underlying details.  The following is a simple example of dependency injection in Java, the same principle can be applied to frameworks like Spring Boot.

```java
package example;

import com.unitvectory.consistgen.epoch.EpochTimeProvider;
import com.unitvectory.consistgen.uuid.UuidGenerator;

public class Demo {

    private final EpochTimeProvider epochTimeProvider;

    private final UuidGenerator uuidGenerator;

    public Demo(EpochTimeProvider epochTimeProvider, UuidGenerator uuidGenerator) {
        // Use dependency injection to insulate implementation from underlying details
        this.epochTimeProvider = epochTimeProvider;
        this.uuidGenerator = uuidGenerator;
    }

    public String build() {
        StringBuilder out = new StringBuilder();

        // Get the current epoch time in milliseconds
        long now = epochTimeProvider.epochTimeMilliseconds();

        // Output the current epoch time
        out.append("Current epoch time: " + now + "\n");

        // Generate a UUID
        String uuid = uuidGenerator.generateUuid();

        // Output the generated UUID
        out.append("Generated UUID: " + uuid + "\n");

        return out.toString();
    }
}
```

The production implementation will utilize the `SystemEpochTimeProvider` and `RandomUuidGenerator` classes to provide access to the actual implementation.

```java
package example;

import com.unitvectory.consistgen.epoch.SystemEpochTimeProvider;
import com.unitvectory.consistgen.uuid.RandomUuidGenerator;

public class Run {
    
    public static void main(String[] args) {
        // Create a new demo instance
        Demo demo = new Demo(SystemEpochTimeProvider.getInstance(), RandomUuidGenerator.getInstance());
        
        // Run the demo
        System.out.println(demo.build());
    }
}
```

Running the program will output something like this, which will vary each time the application is run. The time will be the current system time and the UUID will be randomly generated. While this is intentional for when the application is running, it can make writing test cases challenging.

```
Current epoch time: 1727227278720
Generated UUID: 309e0b9a-632d-4dd7-8751-c5c0d29f725f

```

We can use the `StaticEpochTimeProvider` and `StaticUuidGenerator` classes to provide static responses for these methods in our test cases without changing the implementation through the use of dependency injection. This allows us to write test cases that can assert the expect output of the program to verify that it is working as expected.

```java
package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.unitvectory.consistgen.epoch.EpochTimeProvider;
import com.unitvectory.consistgen.epoch.StaticEpochTimeProvider;
import com.unitvectory.consistgen.uuid.StaticUuidGenerator;
import com.unitvectory.consistgen.uuid.UuidGenerator;

class DemoTest {

    @Test
    void test() {
        // Use the implementation to generate a static output
        EpochTimeProvider epochTimeProvider = StaticEpochTimeProvider.builder().epochTimeMilliseconds(1234567890L)
                .build();
        UuidGenerator uuidGenerator = StaticUuidGenerator.builder().uuid("12345678-1234-5678-1234-567812345678")
                .build();

        // Test the implementation and verify the output
        Demo demo = new Demo(epochTimeProvider, uuidGenerator);
        assertEquals("Current epoch time: 1234567890\n" +
                "Generated UUID: 12345678-1234-5678-1234-567812345678\n", demo.build());
    }
}
```

This test case will consistently pass as the output is predictable and can be asserted. While this is library wraps very simple underlying methods, the pattern here of using dependency injection to insulate the implementation from the underlying details simplifies testing and allows for predictable test cases.
