(function () {
	var PACKAGE = 'xyz.luan.sabv.validations.';
	var map = {};

	map[PACKAGE + '@Required'] = function (obj, _ann) {
		return obj === null ? [ 'Required.empty' ] : [];
	};

	map[PACKAGE + '@Numeric'] = function (number, ann) {
		if (number === null) {
			return [];
		}

		if (typeof number !== 'Number') {
			return  [ 'Numeric.notANumber' ];
		}

		var errors = [];

        if (ann.type === PACKAGE + 'Type.INTEGER') {
            if (number % 1 !== 0)
                errors.push("Numeric.notAnInteger");
        }
        
        if (annotation.maxCap === PACKAGE + 'Cap.INCLUSIVE') {
            if (number > annotation.max) {
                errors.push('Numeric.greaterThan{' + annotation.max + '}');
            }
        } else if (annotation.maxCap === PACKAGE + 'Cap.EXCLUSIVE') {
            if (number >= annotation.max) {
                errors.push('Numeric.greaterOrEqualTo{' + annotation.max + '}');
            }
        }
        
        if (annotation.minCap === PACKAGE + 'Cap.INCLUSIVE') {
            if (number < annotation.min) {
                errors.push('Numeric.smallerThan{' + annotation.min + '}');
            }
        } else if (annotation.minCap === PACKAGE + 'Cap.EXCLUSIVE') {
            if (number <= annotation.min) {
                errors.push('Numeric.smallerOrEqualTo{' + annotation.min + '}');
            }
        }
        
        return errors;
	};

	return map;
})();