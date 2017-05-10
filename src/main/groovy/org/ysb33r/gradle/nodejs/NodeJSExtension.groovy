package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.impl.NodeJSDistributionResolver

/** Configure project defaults for Node.js.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExtension {

    /** Constructs a new exception which is attahced to the provided project.
     *
     * @param project Project this extensionm is associated with.
     */
    NodeJSExtension(Project project) {
        this.project = project
    }

    /** Sets node executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By version (Gradle will download and cache the correct distribution).
     *   executable version : '7.10.0'
     *
     *   // By a physical path (
     *   executable path : '/path/to/node'
     *
     *   // By using searchPath (will attempt to locate in search path).
     *   executable searchPath()
     * </code>
     *
     * @param opts Map taken {@code version} or {@code path} as key.
     */
    void executable( final Map<String,Object> opts ) {
        NodeJSDistributionResolver.createFromOptions(opts)
    }

    /** Resolves a Node.js distribution
     *
     * @return Returns the path to the located {@code node} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedDistribution getResolvedDistribution() {
        nodeResolver.resolve(project)
    }

    /** Use this to configure a system path search for Node
     *
     * @return Returns a special option to be used in {@link #executable}
     */
    static Map<String,Object> searchPath() {
        NodeJSDistributionResolver.SEARCH_PATH
    }

    private Project project
    private NodeJSDistributionResolver nodeResolver



}
