module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON("package.json"),
    bower: { install: { } },
    bower_concat: {
      all: {
        dest: "src/main/js/dependencies.js"
      }
    },
    jshint: {
      all: {
        src: [ "Gruntfile.js", "src/**/*.js" ],
        filter: function(path) { return path !== "src/main/js/dependencies.js"; }
      },
      options: {
        // options here to override JSHint defaults
        globals: {
          jQuery: true,
          console: true,
          module: true,
          document: true
        }
      }
    },
    clean: ["bower_components/", "dist/", "node/", "node_modules/", "src/main/js/dependencies.js" ]
  });

  grunt.loadNpmTasks("grunt-bower-task");
  grunt.loadNpmTasks("grunt-bower-concat");
  grunt.loadNpmTasks("grunt-contrib-jshint");
  grunt.loadNpmTasks("grunt-contrib-clean");

  grunt.registerTask("default", ["bower", "bower_concat", "jshint"]);

};

