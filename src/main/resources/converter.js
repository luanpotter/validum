validum.convert = (function() {

	var ConverterException = function(message) {
		this.name = 'ConverterException';
		this.message = message;
	};

	ConverterException.prototype.getMessage = function() {
		return this.message;
	}

	var types = {};

	var setupNumbers = function() {
		var numbers = {};
		numbers['byte'] = numbers['java.lang.Byte'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-128'),
			'max_value' : new Big('127')
		};
		numbers['short'] = numbers['java.lang.Short'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-32768'),
			'max_value' : new Big('32767')
		};
		numbers['int'] = numbers['java.lang.Integer'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-2147483648'),
			'max_value' : new Big('2147483647')
		};
		numbers['long'] = numbers['java.lang.Long'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-9223372036854775808'),
			'max_value' : new Big('9223372036854775807')
		};
		numbers['float'] = numbers['java.lang.Float'] = {
			'precision' : new Big('1.4E-45'),
			'min_value' : new Big('-3.4028235E38'),
			'max_value' : new Big('3.4028235E38')
		};
		numbers['double'] = numbers['java.lang.Double'] = {
			'precision' : new Big('-4.9E-324'),
			'min_value' : new Big('-1.7976931348623157E308'),
			'max_value' : new Big('1.7976931348623157E308')
		};
		numbers['java.math.BigInteger'] = {
			'precision' : 1,
			'min_value' : null,
			'max_value' : null
		};
		numbers['java.math.BigDecimal'] = {
			'precision' : null,
			'min_value' : null,
			'max_value' : null
		};

		validum._.each(numbers, function(el, num) {
			types[el] = function(num) {
				var ntype = numbers[el], big;
				try {
					big = new Big(num);
				} catch (nan) {
					throw new ConverterException('Numeric.notANumber');
				}

				if (ntype.precision && !big.mod(ntype.precision).eq(0)) {
					throw new ConverterException('Numeric.precisionGreaterThan{' + ntype.precision + '}');
				}

				if (ntype.max_value && big.gt(ntype.max_value)) {
					throw new ConverterException('Numeric.greaterThan{' + ntype.max_value.toString() + '}');
				} else if (ntype.min_value && big.lt(ntype.min_value)) {
					throw new ConverterException('Numeric.smallerThan{' + ntype.min_value.toString() + '}');
				}

				return big;
			};
		});
	};
	setupNumbers();

	types['java.lang.String'] = function(str) {
		return str;
	};

	return function(obj, type) {
		var converter = types[type];
		if (converter) {
			return converter(obj);
		}
		throw new ConverterException('InvalidType');
	};

})();