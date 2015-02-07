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
		if (obj === null) {
			return errors;
		}

		var classDef = obj.getClass();
		var str = '';
		validum._.each(classDef, function(fieldName, field) {
			if (fieldName == '[c]') {
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

			validum._.each(field, function(name, validation) {
				if (name == '[t]') {
					return;
				}
				if (name == '[c]') {
					// TODO
				} else {
					var validator = validum.validators[name];
					if (validator) {
						var newErrors = validator(converted, validation);
						validum._.each(newErrors, function(_i, error) {
							errors.push(fieldPrefix + error);
						});
					} else {
						errors.push(fieldPrefix + 'InvalidValidation{' + name + '}');
					}
				}
			});
		});
		// TODO validate whole class
		return errors;
	};
	return validation(rawObj, className, ':', []);
};
