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

validum._.contains = function(array, value) {
	return array.indexOf(value) !== -1;
};

validum._.extend = function(to, from) {
	validum._.eachField(from, function (key, value) {
		if (to[key]) {
			throw new Error('Impossible to extend ' + to + ' with ' + from + '; already has field ' + key);
		}
		to[key] = value;
	});
};
