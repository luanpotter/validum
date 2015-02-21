package xyz.luan.validum.outliner;

import java.util.Arrays;
import java.util.stream.Stream;

import xyz.luan.validum.annotation.ToJson;
import xyz.luan.validum.util.StreamUtil;

public final class EnumOutliner {

	private EnumOutliner() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static String getJson(Class<? extends Enum<?>> c) {
		String definition = ToJson.toMapElement(ClassOutlinerNames.CLASS_DESCRIPTION, getClassDefinition(c));
		return ToJson.streamToMap(StreamUtil.add(definition, getElements(c)));
	}

	private static String getClassDefinition(Class<? extends Enum<?>> c) {
		return ToJson.streamToMap(StreamUtil.toStream(ToJson.kindToJson("enum"), ToJson.typeToJson(c)));
	}

	private static Stream<String> getElements(Class<? extends Enum<?>> c) {
		return Arrays.stream(c.getEnumConstants()).map(e -> ToJson.toMapElement(e.toString(), ToJson.EMPTY_MAP));
	}
}
