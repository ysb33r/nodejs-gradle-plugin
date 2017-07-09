package org.ysb33r.gradle.nodejs.impl

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.ysb33r.gradle.nodejs.NodeJSExecSpec
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Utilities to execute Node.js scripts.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExecutor {

    /** Configures an {@link NodeJSExecSpec} from a {@link NodeJSExtension}.
     *
     * @param execSpec Execution specification to configure.
     * @param nodeJS NodeJS extension to consult during configuration
     * @return The configured execution specification.
     */
    static NodeJSExecSpec configureSpecFromExtensions(NodeJSExecSpec execSpec, NodeJSExtension nodeJS) {
        ResolvedExecutable resolver = execSpec.getResolvedExecutable()
        if(resolver == null) {
            resolver = nodeJS.resolvedNodeExecutable
            execSpec.setExecutable(  resolver )
        }

       return execSpec
    }

    /** Runs Node.JS scripts given a fully-configured execution specification.
     *
     * @param project The project in which context this execution will be performed.
     * @param execSpec
     * @return Execution result
     * @throw May throw depending on whether execution specification was mal-configured or whether
     *   execution itself failed.
     */
    static ExecResult runNode(Project project, NodeJSExecSpec execSpec) {
        Closure runner = { NodeJSExecSpec fromSpec, ExecSpec toSpec ->
            fromSpec.copyToExecSpec(toSpec)
        }

        project.exec runner.curry(execSpec)
    }

}
