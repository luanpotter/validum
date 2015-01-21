package xyz.luan.validum.entities;

import java.util.List;
import java.util.Map;

import xyz.luan.validum.customs.PalindromeString;
import xyz.luan.validum.validations.Array;
import xyz.luan.validum.validations.Required;
import xyz.luan.validum.validations.Numeric.Max;
import xyz.luan.validum.validations.Numeric.Min;

public class God {

	private String name;

	private @Required Power @Array(minLength = 2) @Required [] powers;

	private @Required @Array(maxLength = 2) List<@Required Weakness> weaknesses;

	private @Min(3.5) @Max(4) float @Required @Array.Fixed(3) [] @Array.Fixed(4) [] calculationMatrix;

	private @Array(minLength = 1) Map<@PalindromeString @Required String, @Required @Array(minLength = 1) List<@Min(3.5) @Max(4) float @Required @Array.Fixed(3) [] @Array.Fixed(4) []>> secondaryMatrixesByName;

	public God(String name, Power[] powers, List<Weakness> weaknesses, float[][] calculationMatrix, Map<String, List<float[][]>> secondaryMatrixesByName) {
		this.name = name;
		this.powers = powers;
		this.weaknesses = weaknesses;
		this.calculationMatrix = calculationMatrix;
		this.secondaryMatrixesByName = secondaryMatrixesByName;
	}

	public String getName() {
		return name;
	}

	public Power[] getPowers() {
		return powers;
	}

	public List<Weakness> getWeaknesses() {
		return weaknesses;
	}

	public float[][] getCalculationMatrix() {
		return calculationMatrix;
	}

	public Map<String, List<float[][]>> getSecondaryMatrixesByName() {
		return secondaryMatrixesByName;
	}
}
