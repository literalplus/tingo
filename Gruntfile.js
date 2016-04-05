module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        jshint: {
            options: {
                reporter: require('jshint-stylish') // use jshint-stylish to make our errors look and read good
            },

            // when this task is run, lint the Gruntfile and all js files in src
            build: ['Gruntfile.js', 'src/main/js/**.js']
        },

        uglify: {
            options: {
                banner: '/*\n <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> \n*/\n'
            },
            build: {
                files: {
                    'build/resources/static/js/tingo.min.js': 'src/main/js/**.js'
                }
            }
        },

        less: {
            build: {
                files: {
                    'build/resources/static/css/tingo.css': 'src/main/less/tingo.less'
                }
            }
        },

        cssmin: {
            options: {
                banner: '/*\n <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> \n*/\n'
            },
            build: {
                files: {
                    'build/resources/static/css/tingo.min.css': 'build/resources/static/css/tingo.css'
                }
            }
        },

        sylesheets: {
        watch: {
            files: 'src/main/less/*.less',
            tasks: ['less', 'cssmin']
        },

        scripts: {
            files: 'src/main/js/**.js',
            tasks: ['jshint', 'uglify']
        }
    }
}
)
;

// Load the plugin that provides the "uglify" task.
grunt.loadNpmTasks('grunt-contrib-jshint');
grunt.loadNpmTasks('grunt-contrib-uglify');
grunt.loadNpmTasks('grunt-contrib-less');
grunt.loadNpmTasks('grunt-contrib-cssmin');
grunt.loadNpmTasks('grunt-contrib-watch');

// Default task(s).
grunt.registerTask('build', ['jshint', 'uglify', 'less', 'cssmin']);
grunt.registerTask('watch', ['watch']);

}
;