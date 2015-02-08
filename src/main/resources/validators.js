validum.validators = (function() {
	var PACKAGE = '@xyz.luan.validum.validations.';
	var map = {};

	map[PACKAGE + 'Required'] = function(obj, _ann) {
		return obj === null ? [ 'Required.empty' ] : [];
	};

	map[PACKAGE + 'Numeric'] = function(number, ann) {
		if (number === null) {
			return [];
		}

		var errors = [];

		if (ann.type === PACKAGE + 'Type.INTEGER') {
			if (!number.mod(1).eq(0)) {
				errors.push('Numeric.notAnInteger');
			}
		}

		if (annotation.maxCap === PACKAGE + 'Cap.INCLUSIVE') {
			if (number.gt(annotation.max)) {
				errors.push('Numeric.greaterThan{' + annotation.max + '}');
			}
		} else if (annotation.maxCap === PACKAGE + 'Cap.EXCLUSIVE') {
			if (number.gte(annotation.max)) {
				errors.push('Numeric.greaterOrEqualTo{' + annotation.max + '}');
			}
		}

		if (annotation.minCap === PACKAGE + 'Cap.INCLUSIVE') {
			if (number.lt(annotation.min)) {
				errors.push('Numeric.smallerThan{' + annotation.min + '}');
			}
		} else if (annotation.minCap === PACKAGE + 'Cap.EXCLUSIVE') {
			if (number.lte(annotation.min)) {
				errors.push('Numeric.smallerOrEqualTo{' + annotation.min + '}');
			}
		}

		return errors;
	};

	map[PACKAGE + 'Numeric.Natural'] = function(number) {
		return map[PACKAGE + 'Numeric'](number, {
			type : PACKAGE + 'Type.INTEGER',
			minCap : PACKAGE + 'Cap.INCLUSIVE',
			min : 0,
			maxCap : PACKAGE + 'Cap.NONE'
		});
	};

	return map;
})();