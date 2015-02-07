validum.validate = function(obj, clazz) {
	var _ = validum._;
	_.each(clazz, function(fieldName, field) {
		var type = field['c'];
		// TODO
	});
	return clazz;
};
