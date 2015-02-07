validum.validateJava = function(json, type) {
	return validum.validate(JSON.parse(json), type);
};