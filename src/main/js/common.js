var validum = validum || {};

validum._ = {};
validum._.each = function(object, callback) {
	var key;
	for (key in object) {
		if (object.hasOwnProperty(key)) {
			if (callback(key, object[key])) {
				break;
			}
		}
	}
};

validum._.eachField = function(object, callback) {
	validum._.each(object, function (key, value) {
		if (key === '[c]') {
			return;
		}
		return callback(key, value);
	});
};
