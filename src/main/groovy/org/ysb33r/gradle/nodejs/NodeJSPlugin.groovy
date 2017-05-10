package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply plugin : NodeJSBasePlugin
    }
}
