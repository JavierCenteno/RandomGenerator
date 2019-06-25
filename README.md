# RandomGenerator

## About

This project offers an API for random number generation and some implementations of random number generators using it (See "Generators" below for the full list of implementations provided).

## License

[This project is licensed under a MIT license.](./LICENSE "LICENSE")

## Features

* The capacity of generating random primitives with different restrictions using different probability distributions with different parameters (See "Distributions" below for a full list of distributions supported).
* A wrapper for Java's Random interface to offer compatibility with libraries that use it (See `getRandom()` method in the `RandomGenerator` class for more information).
* Operations to get and set the state of a generator (optional operations).
* Operation to split the generator (optional operation).
* Collection operations (pick random element, shuffle).

## Usage

The `api` package contains the `RandomGenerator` interface. In order to use it, you just need to implement the non-default methods.

The `api` package also contains the classes `RandomGenerator1` for generators that generate 1 random bit at a time, `RandomGenerator8` for generators that generate 8 random bits at a time, `RandomGenerator16` for generators that generate 16 random bits at a time, `RandomGenerator32` for generators that generate 32 random bits at a time and `RandomGenerator64` for generators that generate 64 random bits at a time. These classes provide default implementations for the methods to generate different primitives, so you can implement random number generators by simply implementing any of those interfaces and overriding the corresponding non-default method (`generateBit()` for `RandomGenerator1`, `generateUniformByte()` for `RandomGenerator8`, `generateUniformShort()` for `RandomGenerator16`, `generateUniformInteger()` for `RandomGenerator32` or `generateUniformLong()` for `RandomGenerator64`).

The `api` package contains partial implementations as well for generators with states of different sizes: `Abstract8RandomGenerator` for generators with 8 bit states, `Abstract16RandomGenerator` for generators with 16 bit states, `Abstract32RandomGenerator` for generators with 32 bit states and `Abstract64RandomGenerator` for generators with 64 bit states.

## Distributions

Distributions supported by this API include:
* Uniform distribution
* Bates distribution
* Normal distribution
* Lognormal distribution
* Triangular distribution
* Pareto distribution
* Weibull distribution
* Exponential distribution
* Gamma distribution
* Beta distribution
* Poisson distribution

## Generators

The `generators` package contains a few implementations of the API with some well known random number generators.

These implementations include:
* Blum Blum Shub
* Complementary-Multiply-With-Carry
* Java's Random
* Java's SplittableRandom
* Lehmer
* Lineal Congruential Generator 32
* Lineal Congruential Generator 64
* Mersenne Twister 19937
* Permuted Congruential Generator XSH-RR
* Permuted Congruential Generator XSH-RS
* WELL 512 a
* WELL 1024 a
* WELL 19937 a
* WELL 19937 c
* WELL 44497 a
* WELL 44497 b
* Xorshift32
* Xorshift64
* Xorshift128
* Xorshift128+
* Xorshift64*
* Xorshift1024*
* Xorwow
* Xoroshiro128+
* Xoroshiro128*
* Xoroshiro128**
* Xoshiro256+
* Xoshiro256**
* Xoshiro512+
* Xoshiro512**
