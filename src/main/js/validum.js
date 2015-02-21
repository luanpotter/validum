var validum = validum || {};
validum.validate = function(rawObj, className) {
	var parseError = function(errors, fieldPrefix, e) {
		if (e instanceof validum.ConverterException) {
			errors.push(fieldPrefix + e.getMessage());
		} else {
			throw e;
		}
	};
	var validation = function(rawObj, className, prefix, errors) {
		var obj;
		try {
			obj = validum.convert(rawObj, className);
		} catch (e) {
			parseError(errors, prefix, e);
			return errors;
		}
		var empty = obj === null;
		if (empty) {
			return errors;
		}

		var primitive = !obj.getClass;
		if (primitive) {
			return errors;
		}

		var applyValidation = function(name, object, validation, prefix) {
			var validator = validum.validators[name];
			if (validator) {
				var newErrors = validator(object, validation);
				validum._.each(newErrors, function(_i, error) {
					errors.push(prefix + error);
				});
			} else {
				errors.push(prefix + 'InvalidValidation{' + name + '}');
			}
		};

		var classDef = obj.getClass();
		validum._.each(classDef, function(fieldName, field) {
			if (fieldName === '[c]') {
				return;
			}
			var fieldPrefix = prefix + fieldName + ':';

			var converted;
			try {
				converted = validum.convert(obj[fieldName], field['[t]']);
			} catch (e) {
				parseError(errors, fieldPrefix, e);
				return;
			}

			if (converted) {
				validation(converted, field['[t]'], fieldPrefix, errors);
			}

			validum._.each(field, function(name, validation) {
				if (name === '[t]') {
					return;
				}
				if (name === '[c]') {
					// TODO validate array elements
					throw new Error('NotImplementedYet');
				} else if (name === '[k]') {
					// TODO validate map keys
					throw new Error('NotImplementedYet');
				} else if (name === '[v]') {
					// TODO validate map values
					throw new Error('NotImplementedYet');
				} else {
					applyValidation(name, converted, validation, fieldPrefix);
				}
			});
		});

		var classLevelValidations = function() {
			validum._.each(classDef['[c]'], function(name, validation) {
				if (name === '[t]') {
					return;
				}
				applyValidation(name, obj, validation, prefix);
			});
		};

		classLevelValidations();
		return errors;
	};
	return validation(rawObj, className, ':', []);
};
