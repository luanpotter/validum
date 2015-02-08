var validum = validum || {};
validum._ = {};
validum._.each = function(object, callback) {
	var key;
	for (key in object) {
		if (object.hasOwnProperty(key)) {
			callback(key, object[key]);
		}
	}
};