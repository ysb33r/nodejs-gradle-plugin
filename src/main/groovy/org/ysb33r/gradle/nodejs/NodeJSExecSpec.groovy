package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.olifant.exec.AbstractScriptExecSpec

/** Execution specification for running a Node.js script.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExecSpec extends AbstractScriptExecSpec {

    /** Construct class and attach it to specific project.
     *
     * @param project Project this exec spec is attached to.
     */
    NodeJSExecSpec(Project project) {
        super(project)
    }
}
