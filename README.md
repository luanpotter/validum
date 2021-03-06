validum
===

[![Build Status](https://travis-ci.org/luanpotter/validum.svg)](https://travis-ci.org/luanpotter/validum)
[![Code Climate](https://codeclimate.com/github/luanpotter/validum/badges/gpa.svg)](https://codeclimate.com/github/luanpotter/validum)

validum is a basic lib that adds validations to your classes that can be called via annotations.
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

    private @Required String name;

    private @Natural int age;
}
```
* Validate arrays and lists, using custom annotation @Super
```java
public class SuperHero {

    private @Required @Array(minLength = 2) List<@Required @Super Power> powers;

    private @CharOnly({'x', 'o'}) char @Array.Fixed(3) [] @Array.Fixed(3) [] currentTickTackToeBoard;
}
```

Currently Implemented Annotations
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
