var fnMock = function () {
};

var jQuery = {
	each : fnMock,
	ajax : fnMock
};

var setup = function (validations) {
	eval('sabv.validations = ' + validations);
}

var runFor = function (obj) {
	return sabv.validate(obj);
}