# RandomGenerator

## About

This project offers an API for random number generation and some implementations that use it.

## License

[This project is licensed under a GNU general public license.](./LICENSE "LICENSE")

## Usage

The `api` package contains the `RandomGenerator` interface. In order to use it, you just need to implement the non-default methods. The `api` package also contains partial implementations for generators with states of different sizes: `Abstract8RandomGenerator` for generators with 8 bit states, `Abstract16RandomGenerator` for generators with 16 bit states, `Abstract32RandomGenerator` for generators with 32 bit states and `Abstract64RandomGenerator` for generators with 64 bit states.

## Standard library compatibility

The `RandomGenerator` interface includes a default method that returns the generator wrapped in a `java.util.Random` object so that generators created using this API can be passed to methods that take an instance of the standard library's `Random` as an argument.

## Methods

This API includes methods to get and set the state of a generator, generate each type of value with different distributions and parameters, pick elements from collections and shuffle them.

Distributions include:
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