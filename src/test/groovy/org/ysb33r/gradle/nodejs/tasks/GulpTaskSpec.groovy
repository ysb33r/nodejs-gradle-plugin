package org.ysb33r.gradle.nodejs.tasks

import org.ysb33r.gradle.nodejs.GulpExtension
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.helper.UnittestBaseSpecification

class GulpTaskSpec extends UnittestBaseSpecification {

    void 'Create Gulp task'() {

        setup:
        project.apply plugin : 'org.ysb33r.nodejs.gulp'

        when: 'A Gulp task is created'
        GulpTask task = project.tasks.create('myGulpTask',GulpTask)

        then: 'Three extensions are added to the task'
        task.extensions.getByName('nodejs') instanceof NodeJSExtension
        task.extensions.getByName('npm')    instanceof NpmExtension
        task.extensions.getByName('gulp')   instanceof GulpExtension

        when: 'Configuring the task'
        project.allprojects {
            // tag::configure-gulp-task[]
            myGulpTask {
                target 'clean' // <1>
            }
            // end::configure-gulp-task[]
        }

        then: 'The task can be set'
        task.target == 'clean'
        task.gulpPath == project.file("${project.projectDir}/gulpfile.js").absolutePath

        when: 'The gulp file location is configured via the task extension'
        project.allprojects {
            // tag::configure-gulp-extension[]
            myGulpTask {
                gulp {
                    gulpFile 'foo/gulpfile.js' // <1>
                }
                npm {
                    homeDirectory 'foo' // <2>
                }
            }
            // end::configure-gulp-extension[]
        }

        then: 'The value in the project extension is no longer used'
        task.gulpPath == project.file("${project.projectDir}/foo/gulpfile.js").absolutePath
        project.gulp.gulpFile == project.file("${project.projectDir}/gulpfile.js")
    }
}