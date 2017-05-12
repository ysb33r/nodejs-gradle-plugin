package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.impl.NodeJSDistributionResolver
import org.ysb33r.gradle.olifant.OperatingSystem

/** Configure project defaults for Node.js.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExtension {

    static final String NAME = 'nodejs'

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
        nodeResolver = NodeJSDistributionResolver.createFromOptions(opts)
    }

    /** Resolves a path to a {@code node} executable.
     *
     * @return Returns the path to the located {@code node} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedExecutable getResolvedNodeExecutable() {
        if(nodeResolver == null) {
            throw new GradleException('''Node.js executable was not configured. Call 'executable' first to set a way of resolving the distribution''')
        }
        nodeResolver.resolve(project)
    }

    /** Resolves a path to a {@code npm} executable that is associated with the configured .
     *
     * @return Returns the path to the located {@code npm} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedExecutable getResolvedNpmExecutable() {
        new ResolvedExecutable() {
            @Override
            File getExecutable() {
                File root = getResolvedNodeExecutable().getExecutable().getParentFile()
                if(OperatingSystem.current().windows) {
                    new File(root,'npm.cmd')
                } else {
                    new File(root,'npm')
                }
            }
        }
    }

    /** Use this to configure a system path search for Node
     *
     * @return Returns a special option to be used in {@link #executable}
     */
    static Map<String,Object> searchPath() {
        NodeJSDistributionResolver.SEARCH_PATH
    }

    private Project project
    @PackageScope NodeJSDistributionResolver nodeResolver



}
