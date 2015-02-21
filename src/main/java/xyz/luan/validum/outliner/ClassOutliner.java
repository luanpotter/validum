package xyz.luan.validum.outliner;

import static xyz.luan.validum.annotation.ToJson.annotationToJson;
import static xyz.luan.validum.annotation.ToJson.streamToMap;
import static xyz.luan.validum.annotation.ToJson.toMapElement;
import static xyz.luan.validum.annotation.ToJson.typeToJson;
import static xyz.luan.validum.annotation.ToJson.kindToJson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.reflection.tclass.TypedClass;
import xyz.luan.validum.ValidationHelper;
import xyz.luan.validum.util.StreamUtil;

public final class ClassOutliner {

	private ClassOutliner() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static String getJson(Class<?> c) {
		return getJson(c, ReflectionUtils.getFieldsRecursively(c));
	}

	public static String getJson(Class<?> c, List<Field> fields) {
		String classElement = toMapElement(ClassOutlinerNames.CLASS_DESCRIPTION, getClassLevelAnnotations(c));
		Stream<String> fieldsJson = fields.stream().map(field -> toMapElement(field.getName(), parseField(field)));
		return streamToMap(StreamUtil.add(classElement, fieldsJson));
	}

	private static String parseField(Field field) {
		return parseClass(TypedClass.create(field));
	}

	private static String parseClass(TypedClass<?> clazz) {
		Stream<String> result = StreamUtil.add(typeToJson(clazz.asClass()), getAnnotations(clazz.getAnnotations()));
		if (clazz.isList()) {
			String componentString = toMapElement(ClassOutlinerNames.ARRAY_ELEMENT, parseClass(clazz.asList().getComponent()));
			result = StreamUtil.add(componentString, result);
		} else if (clazz.isMap()) {
			String keyString = toMapElement(ClassOutlinerNames.MAP_KEY, parseClass(clazz.asMap().getKey()));
			String valueString = toMapElement(ClassOutlinerNames.MAP_VALUE, parseClass(clazz.asMap().getValue()));
			result = StreamUtil.addToBeginning(result, keyString, valueString);
		}
		return streamToMap(result);
	}

	public static String getClassLevelAnnotations(Class<?> c) {
		Stream<String> annotations = getAnnotations(ValidationHelper.getValidationAnnotations(c));
		return streamToMap(StreamUtil.addToBeginning(annotations, typeToJson(c), kindToJson("class")));
	}

	private static Stream<String> getAnnotations(List<Annotation> annotations) {
		return annotations.stream().map(ann -> annotationToJson(ann));
	}
}
