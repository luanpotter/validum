(function() {
	var PACKAGE = '@xyz.luan.validum.customs.';
	validum.validators[PACKAGE + 'ValidAddress'] = function(address) {
		var invalid = address !== null && (address.number == 404 || address.street == "Bad Street");
		return invalid ? [ 'ValidAddress.invalidAddress' ] : [];
	};
}());