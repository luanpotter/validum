var validum = validum || {};
validum.validators = (function() {
	var PACKAGE = '@xyz.luan.validum.validations.';
	var map = {};

	map[PACKAGE + 'Required'] = function(obj) {
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

		if (ann.maxCap === PACKAGE + 'Cap.INCLUSIVE') {
			if (number.gt(ann.max)) {
				errors.push('Numeric.greaterThan{' + ann.max + '}');
			}
		} else if (ann.maxCap === PACKAGE + 'Cap.EXCLUSIVE') {
			if (number.gte(ann.max)) {
				errors.push('Numeric.greaterOrEqualTo{' + ann.max + '}');
			}
		}

		if (ann.minCap === PACKAGE + 'Cap.INCLUSIVE') {
			if (number.lt(ann.min)) {
				errors.push('Numeric.smallerThan{' + ann.min + '}');
			}
		} else if (ann.minCap === PACKAGE + 'Cap.EXCLUSIVE') {
			if (number.lte(ann.min)) {
				errors.push('Numeric.smallerOrEqualTo{' + ann.min + '}');
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

	map[PACKAGE + 'Numeric.Min'] = function(number, min) {
		return map[PACKAGE + 'Numeric'](number, {
			minCap : min.cap,
			min : min.value,
			maxCap : PACKAGE + 'Cap.NONE'
		});
	};

	map[PACKAGE + 'Numeric.Max'] = function(number, max) {
		return map[PACKAGE + 'Numeric'](number, {
			maxCap : max.cap,
			max : max.value,
			minCap : PACKAGE + 'Cap.NONE'
		});
	};

	return map;
})();