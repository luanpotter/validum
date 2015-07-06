var validum = validum || {};
validum.convert = (function() {

	var ConverterException = validum.ConverterException = function(message) {
		this.name = 'ConverterException';
		this.message = message;
	};

	ConverterException.prototype.getMessage = function() {
		return this.message;
	};

	var types = {};

	var setupNumbers = function() {
		var numbers = {};
		numbers.byte = numbers['java.lang.Byte'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-128'),
			'max_value' : new Big('127')
		};
		numbers.short = numbers['java.lang.Short'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-32768'),
			'max_value' : new Big('32767')
		};
		numbers.int = numbers['java.lang.Integer'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-2147483648'),
			'max_value' : new Big('2147483647')
		};
		numbers.long = numbers['java.lang.Long'] = {
			'precision' : new Big(1),
			'min_value' : new Big('-9223372036854775808'),
			'max_value' : new Big('9223372036854775807')
		};
		numbers.float = numbers['java.lang.Float'] = {
			'precision' : new Big('1.4E-45'),
			'min_value' : new Big('-3.4028235E38'),
			'max_value' : new Big('3.4028235E38')
		};
		numbers.double = numbers['java.lang.Double'] = {
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

		validum._.each(numbers, function(el, ntype) {
			types[el] = function(num) {
				var big;
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

	var createJavaObject = function(typeDef) {
		var MyClass = function() {
		};
		MyClass.prototype.getClass = function() {
			return typeDef;
		};
		return new MyClass();
	};

	var convertObject = function(typeDef) {
		return function(obj) {
			if (typeof obj !== 'object') {
				throw new ConverterException('InconvertableTypes{' + (typeof obj) + ', ' + typeDef + ' [class]}');
			}
			var result = createJavaObject(typeDef);
			validum._.each(obj, function(name, value) {
				if (!typeDef[name]) {
					throw new ConverterException('InvalidField{' + name + '}');
				}
				result[name] = value;
			});
			validum._.eachField(typeDef, function(name) {
				result[name] = result[name] || null;
			});
			return result;
		};
	};

	var convertEnum = function(typeDef) {
		return function(obj) {
			var result;
			if (typeof obj !== 'string') {
				throw new ConverterException('InconvertableTypes{' + (typeof obj) + ', ' + typeDef + ' [enum]}');
			}
			validum._.eachField(typeDef, function(enumConstant) {
				if (enumConstant === obj) {
					result = enumConstant;
					return true;
				}
			});
			if (!result) {
				throw new ConverterException('InvalidEnumConstant{' + obj + '}');
			}
			return result;
		};
	};

	var convert = function(obj, type) {
		var converter = types[type];
		if (!converter) {
			throw new ConverterException('InvalidType{' + type + '}');
		}
		if (obj === null || obj === undefined) {
			return null;
		}
		return converter(obj);
	};

	convert.setup = function(classDefs) {
		if (!(classDefs instanceof Array)) {
			classDefs = [ classDefs ];
		}
		validum._.each(classDefs, function(_i, classDef) {
			var className = classDef['[c]']['[t]'];
			var kind = classDef['[c]']['[k]'];
			var parent = classDef['[c]']['[p]'];
			if (kind === 'class') {
				if (parent) {
					var parentClassDef = (types[parent] || { classDef: classDefs[classDef] }).classDef;
					validum._.extend(classDef, parentClassDef);
				}
				types[className] = convertObject(classDef);
			} else if (kind === 'enum') {
				types[className] = convertEnum(classDef);
			} else {
				throw new Error('Invalid kind: ' + kind);
			}
			types[className].classDef = classDef;
		});
	};

	return convert;
})();