package org.ysb33r.gradle.nodejs

import org.ysb33r.gradle.nodejs.helper.UnittestBaseSpecification
import spock.lang.Specification

class GulpExtensionSpec extends UnittestBaseSpecification {

    void 'Create a Gulp extension'() {

        when: 'The Gulp plugin is applied'
        project.allprojects {
            project.apply plugin : 'org.ysb33r.nodejs.gulp'
        }

        then: 'A project extension named gulp is created'
        project.extensions.getByName('gulp') instanceof GulpExtension
        project.extensions.getByName('gulp').extensionName == 'gulp'

        and: 'The default installation group is development'
        project.extensions.getByName('gulp').installGroup == NpmDependencyGroup.DEVELOPMENT

        when: 'When setting the gulpfile location and some requires'
        GulpExtension gulpExt = project.extensions.getByName('gulp')
        project.allprojects {
            // tag::configuring-gulp[]
            gulp {
                executable version : '1.2.3' // <1>
                gulpFile 'foo/gulpfile.js'   // <2>
                requires 'foo', 'bar'        // <3>
            }
            // end::configuring-gulp[]
        }

        then: 'The location should be updated'
        gulpExt.getGulpFile() == project.file('foo/gulpfile.js')

        and: 'The requires should listed'
        gulpExt.requires.containsAll(['bar','foo'])

        when: 'The gulpfile location is set via assignment'
        project.allprojects {
            gulp {
                gulpFile = 'bar/gulp.js'
            }
        }

        then: 'The location should replace any previous setting'
        gulpExt.getGulpFile() == project.file('bar/gulp.js')

        when: 'The installation group is updated'
        project.allprojects {
            // tag::change-installation-group[]
            gulp {
                installGroup NpmDependencyGroup.OPTIONAL // <1>
            }
            // end::change-installation-group[]
        }

        then: 'The installation group will be updated'
        gulpExt.installGroup == NpmDependencyGroup.OPTIONAL


    }
}