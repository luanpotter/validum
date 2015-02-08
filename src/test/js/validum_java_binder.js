validum.validateJava = function(json, type) {
	return validum.validate(JSON.parse(json), type);
};
validum.convertJava = function(json, type) {
	return validum.convert(JSON.parse(json), type);
};