package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

/** Provide the basic capabilites for dealing with Node.js & NPM tasks. Allow for downlaoding & caching of Node.js distributions
 * on a variery of the most common development platforms.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSBasePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create(NodeJSExtension.NAME,NodeJSExtension,project)
        project.extensions.create(NpmExtension.NAME,NpmExtension,project)
    }
}
