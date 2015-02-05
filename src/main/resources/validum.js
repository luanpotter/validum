var validum = validum || {};
validum.validate = function(obj, clazz) {
	var each = function(object, callback) {
		var key;
		for (key in object) {
			if (object.hasOwnProperty(key)) {
				callback(key, object[key]);
			}
		}
	};
	each(clazz, function(fieldName, field) {
		// TODO
	});
	return clazz;
};
