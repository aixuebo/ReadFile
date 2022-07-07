package com.maming.common.util;


import java.io.Serializable;

/**
 * A resettable implementation of the 32-bit MurmurHash algorithm.
 */
public class MurmurHash implements Serializable {

	private static final long serialVersionUID = 1L;

	// initial seed, which can be reset
	private final int seed;

	// number of 32-bit values processed
	private int count;

	// in-progress hash value
	private int hash;

	/**
	 * A resettable implementation of the 32-bit MurmurHash algorithm.
	 *
	 * @param seed MurmurHash seed
	 */
	public MurmurHash(int seed) {
		this.seed = seed;
		reset();
	}

	/**
	 * Re-initialize the MurmurHash state.
	 *
	 * @return this
	 */
	public MurmurHash reset() {
		count = 0;
		hash = seed;
		return this;
	}

	/**
	 * Process a {@code double} value.
	 *
	 * @param input 64-bit input value
	 * @return this
	 */
	public MurmurHash hash(double input) {
		hash(Double.doubleToLongBits(input));
		return this;
	}

	/**
	 * Process a {@code float} value.
	 *
	 * @param input 32-bit input value
	 * @return this
	 */
	public MurmurHash hash(float input) {
		hash(Float.floatToIntBits(input));
		return this;
	}

	/**
	 * Process an {@code integer} value.
	 *
	 * @param input 32-bit input value
	 * @return this
	 */
	public MurmurHash hash(int input) {
		count++;

		input *= 0xcc9e2d51;
		input = Integer.rotateLeft(input, 15);
		input *= 0x1b873593;

		hash ^= input;
		hash = Integer.rotateLeft(hash, 13);
		hash = hash * 5 + 0xe6546b64;

		return this;
	}

	/**
	 * Process a {@code long} value.
	 *
	 * @param input 64-bit input value
	 * @return this
	 */
	public MurmurHash hash(long input) {
		hash((int) (input >>> 32));
		hash((int) input);
		return this;
	}

	/**
	 * Finalize and return the MurmurHash output.
	 *
	 * @return 32-bit hash
	 */
	public int hash() {
		hash ^= 4 * count;
		hash ^= hash >>> 16;
		hash *= 0x85ebca6b;
		hash ^= hash >>> 13;
		hash *= 0xc2b2ae35;
		hash ^= hash >>> 16;

		return hash;
	}
}