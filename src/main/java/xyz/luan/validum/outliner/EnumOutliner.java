package xyz.luan.validum.outliner;

import java.util.Arrays;
import java.util.stream.Collectors;

import xyz.luan.validum.annotation.ToJson;

public final class EnumOutliner {

	private EnumOutliner() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static String getJson(Class<? extends Enum<?>> c) {
		String name = ToJson.toMapElement("name", ToJson.strToJson(c.getCanonicalName()));
		String elements = ToJson.toMapElement("elements", getElements(c));
		return "{ " + name + ", " + elements + " }";
	}

	private static String getElements(Class<? extends Enum<?>> c) {
		String elements = Arrays.stream(c.getEnumConstants()).map(e -> ToJson.strToJson(e.toString())).collect(Collectors.joining(", "));
		return "[ " + elements + " ]";
	}
}
