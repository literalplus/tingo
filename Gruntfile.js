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

            concat: {
                dist: {
                    src: 'src/main/js/**.js',
                    dest: 'build/resources/static/js/tingo.js'
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
            },
            
            copy: {
                bootstrap_fonts: {
                    expand: true,
                    src: 'node_modules/bootstrap-less/fonts/**',
                    dest: 'build/resources/static/fonts/'
                }
            }
        }
    );

// Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-copy');

// Default task(s).
    grunt.registerTask('build', ['jshint', 'concat', 'uglify', 'less', 'cssmin', 'copy']);
    grunt.registerTask('watch', ['watch']);

}
;
