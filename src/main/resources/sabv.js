var sabv = (function ($) {
	var map = {};

	map.helpers = {};

	map.validators = {};

	map.setup = function (urls) {
		$.each(urls, function (_i, url) {
			$.ajax({
				type : 'GET',
				url : url,
				async : false,
				success : function (data) {
					var validations;
					eval('validations = ' + data);
					if (typeof validations !== 'Object') {
						throw 'Every validation file must evaluate to a map. Error in ' + url + ', got ' + (typeof validations); 
					}
					$.extend(map.validators, validations);
				},
			}).fail(function (error) {
				throw 'Request failed for url ' + url + '. Error: ' + error;
			});
		});
	};
	
	map.validate = function (obj) {
		//TODO
		throw 'not implemented yet!';
	};

	return map;

})(jQuery);