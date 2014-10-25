SABV
===

[![Build Status](https://api.shippable.com/projects/53e6c4b5abb5ae8700a770d0/badge/master)](https://www.shippable.com/projects/53e6c4b5abb5ae8700a770d0/builds/history)

SABV, or Simple Annotation Based Validations, is a basic lib that adds validations to your classes that can be called via annotations.
It uses Java 8 brand new feature, the annotation target option ElementType.TYPE_USE, that allows you to validate the contents of your arrays and Lists with fine control.

Features
---
 * Full support for both inheritance and composition, how many levels desired;
 * Fine control of arrays' components or lists' elements with Java 8's TYPE_USE target option;
 * Support for custom validations, just create your own annotation and annotate it with @Validator annotation, and then add a custom Validator for it;
 * Class level validations; that is, the annotations can be added to the class declaration, to validate the class's object as a whole;
 * Each annotation must specify the types to which it applies, and exceptions are thrown if they are missused.

Planned Features
---
 * Javascript client side validation: you will be able to generate a JSON with validation information from your classes, so that a to-be client side library can validate your forms on the browser as well: client and server validations, unified at least.
 * Custom annotation preprocessor for validating each annotation appliable types

Examples
---

* Simple validations
```java
public class Person {

    @Required
    private String name;

    @Natural
    private int age;
}
```
* Validate arrays and lists, using custom annotation @Super
```java
public class SuperHero {

    @Required @Array(minLength = 2)
    private List<@Required @Super Power> powers;

    @Array.Fixed(3)
    private char @Array.Fixed(3) [] @CharOnly({'x', 'o'}) [] currentTickTackToeBoard;
}
```

Current Implemented Annotations
---

Custom annotations can be easily created, but some pre-defined default ones might come in hand. Currently, they are:

 * @Required
 * @Numeric(min, minCap, max, maxCap, type)
 * @Numeric.Max(value)
 * @Numeric.Min(value)
 * @Numeric.Natural
 * @EnumOnly(value)
 * @EnumExcept(value)
 * @Array(minLength, maxLength)
 * @Array.Length(value)
