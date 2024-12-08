/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("Komprehensions")


/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, R> doLet(
    zero: () -> A,
    one: (A) -> R,
): R =
    zero()
        .let { a ->
            one(a)
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                        }
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, D, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                                .let { d ->
                                    four(a, b, c, d)
                                }
                        }
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, D, E, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                                .let { d ->
                                    four(a, b, c, d)
                                        .let { e ->
                                            five(a, b, c, d, e)
                                        }
                                }
                        }
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, D, E, F, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                                .let { d ->
                                    four(a, b, c, d)
                                        .let { e ->
                                            five(a, b, c, d, e)
                                                .let { f ->
                                                    six(a, b, c, d, e, f)
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, D, E, F, G, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> G,
    seven: (A, B, C, D, E, F, G) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                                .let { d ->
                                    four(a, b, c, d)
                                        .let { e ->
                                            five(a, b, c, d, e)
                                                .let { f ->
                                                    six(a, b, c, d, e, f)
                                                        .let { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, D, E, F, G, H, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> G,
    seven: (A, B, C, D, E, F, G) -> H,
    eight: (A, B, C, D, E, F, G, H) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                                .let { d ->
                                    four(a, b, c, d)
                                        .let { e ->
                                            five(a, b, c, d, e)
                                                .let { f ->
                                                    six(a, b, c, d, e, f)
                                                        .let { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                                .let { h ->
                                                                    eight(a, b, c, d, e, f, g, h)
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes a sequence from multiple creation functions chained by let.
 *
 * @return chain
 */
fun <A, B, C, D, E, F, G, H, I, R> doLet(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> G,
    seven: (A, B, C, D, E, F, G) -> H,
    eight: (A, B, C, D, E, F, G, H) -> I,
    nine: (A, B, C, D, E, F, G, H, I) -> R,
): R =
    zero()
        .let { a ->
            one(a)
                .let { b ->
                    two(a, b)
                        .let { c ->
                            three(a, b, c)
                                .let { d ->
                                    four(a, b, c, d)
                                        .let { e ->
                                            five(a, b, c, d, e)
                                                .let { f ->
                                                    six(a, b, c, d, e, f)
                                                        .let { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                                .let { h ->
                                                                    eight(a, b, c, d, e, f, g, h)
                                                                        .let { i ->
                                                                            nine(a, b, c, d, e, f, g, h, i)
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                        .andThen { c ->
                            three(a, b, c)
                        }
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, D : Chainable, R : Chainable> doChainable(
    f0: () -> A,
    f1: (A) -> B,
    f2: (A, B) -> C,
    f3: (A, B, C) -> D,
    f4: (A, B, C, D) -> R,
): R =
    f0().andThen { a ->
        f1(a).andThen { b ->
            f2(a, b).andThen { c ->
                f3(a, b, c).andThen { d ->
                    f4(a, b, c, d)
                }
            }
        }
    }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, D : Chainable, E : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                        .andThen { c ->
                            three(a, b, c)
                                .andThen { d ->
                                    four(a, b, c, d)
                                        .andThen { e ->
                                            five(a, b, c, d, e)
                                        }
                                }
                        }
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, D : Chainable, E : Chainable, F : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                        .andThen { c ->
                            three(a, b, c)
                                .andThen { d ->
                                    four(a, b, c, d)
                                        .andThen { e ->
                                            five(a, b, c, d, e)
                                                .andThen { f ->
                                                    six(a, b, c, d, e, f)
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, D : Chainable, E : Chainable, F : Chainable, G : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> G,
    seven: (A, B, C, D, E, F, G) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                        .andThen { c ->
                            three(a, b, c)
                                .andThen { d ->
                                    four(a, b, c, d)
                                        .andThen { e ->
                                            five(a, b, c, d, e)
                                                .andThen { f ->
                                                    six(a, b, c, d, e, f)
                                                        .andThen { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, D : Chainable, E : Chainable, F : Chainable, G : Chainable, H : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> G,
    seven: (A, B, C, D, E, F, G) -> H,
    eight: (A, B, C, D, E, F, G, H) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                        .andThen { c ->
                            three(a, b, c)
                                .andThen { d ->
                                    four(a, b, c, d)
                                        .andThen { e ->
                                            five(a, b, c, d, e)
                                                .andThen { f ->
                                                    six(a, b, c, d, e, f)
                                                        .andThen { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                                .andThen { h ->
                                                                    eight(a, b, c, d, e, f, g, h)
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an sequence from multiple creation functions chained by andThen.
 *
 * @return chain
 */
fun <A : Chainable, B : Chainable, C : Chainable, D : Chainable, E : Chainable, F : Chainable, G : Chainable, H : Chainable, I : Chainable, R : Chainable> doChainable(
    zero: () -> A,
    one: (A) -> B,
    two: (A, B) -> C,
    three: (A, B, C) -> D,
    four: (A, B, C, D) -> E,
    five: (A, B, C, D, E) -> F,
    six: (A, B, C, D, E, F) -> G,
    seven: (A, B, C, D, E, F, G) -> H,
    eight: (A, B, C, D, E, F, G, H) -> I,
    nine: (A, B, C, D, E, F, G, H, I) -> R,
): R =
    zero()
        .andThen { a ->
            one(a)
                .andThen { b ->
                    two(a, b)
                        .andThen { c ->
                            three(a, b, c)
                                .andThen { d ->
                                    four(a, b, c, d)
                                        .andThen { e ->
                                            five(a, b, c, d, e)
                                                .andThen { f ->
                                                    six(a, b, c, d, e, f)
                                                        .andThen { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                                .andThen { h ->
                                                                    eight(a, b, c, d, e, f, g, h)
                                                                        .andThen { i ->
                                                                            nine(a, b, c, d, e, f, g, h, i)
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<R>,
): Iterable<R> =
    zero()
        .flatMap { a ->
            one(a)
        }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, B, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<R>,
): Iterable<R> =
    zero()
        .flatMap { a ->
            one(a)
                .flatMap { b ->
                    two(a, b)
                }
        }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
inline fun <A, B, C, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<R>,
): Iterable<R> =
    zero()
        .flatMap { a ->
            one(a)
                .flatMap { b ->
                    two(a, b)
                        .flatMap { c ->
                            three(a, b, c)
                        }
                }
        }

fun <A, B, C, R> doFlatMapSequence(
    zero: () -> Sequence<A>,
    one: (A) -> Sequence<B>,
    two: (A, B) -> Sequence<C>,
    three: (A, B, C) -> Sequence<R>,
): Sequence<R> =
    zero()
        .flatMap { a ->
            one(a)
                .flatMap { b ->
                    two(a, b)
                        .flatMap { c -> three(a, b, c) }
                }
        }


/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
inline fun <A, B, C, D, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<D>,
    four: (A, B, C, D) -> Iterable<R>,
): Iterable<R> =
    zero().flatMap { a ->
        one(a).flatMap { b ->
            two(a, b).flatMap { c ->
                three(a, b, c).flatMap { d ->
                    four(a, b, c, d)
                }
            }
        }
    }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, B, C, D, E, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<D>,
    four: (A, B, C, D) -> Iterable<E>,
    five: (A, B, C, D, E) -> Iterable<R>,
): Iterable<R> =
    zero()
        .flatMap { a ->
            one(a)
                .flatMap { b ->
                    two(a, b)
                        .flatMap { c ->
                            three(a, b, c)
                                .flatMap { d ->
                                    four(a, b, c, d)
                                        .flatMap { e ->
                                            five(a, b, c, d, e)
                                        }
                                }
                        }
                }
        }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, B, C, D, E, F, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<D>,
    four: (A, B, C, D) -> Iterable<E>,
    five: (A, B, C, D, E) -> Iterable<F>,
    six: (A, B, C, D, E, F) -> Iterable<R>,
): Iterable<R> =
    zero()
        .flatMap { a ->
            one(a)
                .flatMap { b ->
                    two(a, b)
                        .flatMap { c ->
                            three(a, b, c)
                                .flatMap { d ->
                                    four(a, b, c, d)
                                        .flatMap { e ->
                                            five(a, b, c, d, e)
                                                .flatMap { f ->
                                                    six(a, b, c, d, e, f)
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, B, C, D, E, F, G, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<D>,
    four: (A, B, C, D) -> Iterable<E>,
    five: (A, B, C, D, E) -> Iterable<F>,
    six: (A, B, C, D, E, F) -> Iterable<G>,
    seven: (A, B, C, D, E, F, G) -> Iterable<R>,
): Iterable<R> =
    zero().flatMap { a ->
        one(a).flatMap { b ->
            two(a, b).flatMap { c ->
                three(a, b, c).flatMap { d ->
                    four(a, b, c, d).flatMap { e ->
                        five(a, b, c, d, e).flatMap { f ->
                            six(a, b, c, d, e, f).flatMap { g ->
                                seven(a, b, c, d, e, f, g)
                            }
                        }
                    }
                }
            }
        }
    }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, B, C, D, E, F, G, H, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<D>,
    four: (A, B, C, D) -> Iterable<E>,
    five: (A, B, C, D, E) -> Iterable<F>,
    six: (A, B, C, D, E, F) -> Iterable<G>,
    seven: (A, B, C, D, E, F, G) -> Iterable<H>,
    eight: (A, B, C, D, E, F, G, H) -> Iterable<R>,
): Iterable<R> =
    zero().flatMap { a ->
        one(a).flatMap { b ->
            two(a, b).flatMap { c ->
                three(a, b, c).flatMap { d ->
                    four(a, b, c, d).flatMap { e ->
                        five(a, b, c, d, e).flatMap { f ->
                            six(a, b, c, d, e, f).flatMap { g ->
                                seven(a, b, c, d, e, f, g).flatMap { h ->
                                    eight(a, b, c, d, e, f, g, h)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

/**
 * Composes an [Iterable] from multiple creation functions chained by flatMap.
 *
 * @return iterable
 */
fun <A, B, C, D, E, F, G, H, I, R> doFlatMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> Iterable<B>,
    two: (A, B) -> Iterable<C>,
    three: (A, B, C) -> Iterable<D>,
    four: (A, B, C, D) -> Iterable<E>,
    five: (A, B, C, D, E) -> Iterable<F>,
    six: (A, B, C, D, E, F) -> Iterable<G>,
    seven: (A, B, C, D, E, F, G) -> Iterable<H>,
    eight: (A, B, C, D, E, F, G, H) -> Iterable<I>,
    nine: (A, B, C, D, E, F, G, H, I) -> Iterable<R>,
): Iterable<R> =
    zero()
        .flatMap { a ->
            one(a)
                .flatMap { b ->
                    two(a, b)
                        .flatMap { c ->
                            three(a, b, c)
                                .flatMap { d ->
                                    four(a, b, c, d)
                                        .flatMap { e ->
                                            five(a, b, c, d, e)
                                                .flatMap { f ->
                                                    six(a, b, c, d, e, f)
                                                        .flatMap { g ->
                                                            seven(a, b, c, d, e, f, g)
                                                                .flatMap { h ->
                                                                    eight(a, b, c, d, e, f, g, h)
                                                                        .flatMap { i ->
                                                                            nine(a, b, c, d, e, f, g, h, i)
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> R,
): Iterable<R> =
    zero()
        .map(one)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, D, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> D,
    four: (D) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)
        .map(four)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, D, E, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> D,
    four: (D) -> E,
    five: (E) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)
        .map(four)
        .map(five)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, D, E, F, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> D,
    four: (D) -> E,
    five: (E) -> F,
    six: (F) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)
        .map(four)
        .map(five)
        .map(six)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, D, E, F, G, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> D,
    four: (D) -> E,
    five: (E) -> F,
    six: (F) -> G,
    seven: (G) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)
        .map(four)
        .map(five)
        .map(six)
        .map(seven)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, D, E, F, G, H, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> D,
    four: (D) -> E,
    five: (E) -> F,
    six: (F) -> G,
    seven: (G) -> H,
    eight: (H) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)
        .map(four)
        .map(five)
        .map(six)
        .map(seven)
        .map(eight)

/**
 * Composes an [Iterable] from multiple functions chained by [Iterable.map]
 *
 * @return composed Observable
 */
fun <A, B, C, D, E, F, G, H, I, R> doMapIterable(
    zero: () -> Iterable<A>,
    one: (A) -> B,
    two: (B) -> C,
    three: (C) -> D,
    four: (D) -> E,
    five: (E) -> F,
    six: (F) -> G,
    seven: (G) -> H,
    eight: (H) -> I,
    nine: (I) -> R,
): Iterable<R> =
    zero()
        .map(one)
        .map(two)
        .map(three)
        .map(four)
        .map(five)
        .map(six)
        .map(seven)
        .map(eight)
        .map(nine)