package org.ysb33r.gradle.nodejs.impl

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.ResolvedDistribution
import org.ysb33r.gradle.olifant.OperatingSystem
import org.ysb33r.gradle.olifant.StringUtils

/** Provides a way of resolving a Node.js distribution
 *
 * @since 0.1
 */
@CompileStatic
abstract class NodeJSDistributionResolver {

    static private final OperatingSystem OS = OperatingSystem.current()

    static final Map<String,Object> SEARCH_PATH = [ search : 'node' ]

    /** Returns the path to the resolved {@code node} executable.
     *
     * @return A resolved file object
     */
    abstract ResolvedDistribution resolve(Project project)

    static NodeJSDistributionResolver createFromOptions(final Map<String,?> opts) {
        Number keyCount = opts.keySet().count { String it ->
           it == 'path' || it == 'search' || it == 'version'
        }

        if(keyCount>1) {
            throw new GradleException('''Combining use of 'path', 'search' and 'version' is not valid.''')
        }
        if(opts['version']) {
            return new Version(opts['version'])
        }
        if(opts['path']) {
            return new Path(opts['path'])
        }
        if(opts['search']) {
            return new SearchPath(opts['search'])
        }

        throw new GradleException('''Need one of 'path', 'search' and 'version'.''')
    }


    protected static class Version extends NodeJSDistributionResolver {

        Version(def lazyVersion) {
            this.lazyVersion = lazyVersion
        }

        ResolvedDistribution resolve(Project project) {
            Downloader dnl = new Downloader(StringUtils.stringize(lazyVersion),project)

            return new ResolvedDistribution() {

                @Override
                File getNodeExecutable() {
                    dnl.getNodeExecutablePath()
                }
            }
        }

        private Object lazyVersion
    }

    protected static class Path extends NodeJSDistributionResolver {

        Path(def lazyPath) {
            this.lazyPath = lazyPath
        }

        ResolvedDistribution resolve(Project project) {
            File resolvedPath = project.file(lazyPath)

            return new ResolvedDistribution() {
                @Override
                File getNodeExecutable() {
                    if(resolvedPath.exists()) {
                        return resolvedPath
                    } else {
                        throw new GradleException("${resolvedPath} does not exist.")
                    }
                }
            }
        }

        private Object lazyPath

    }

    protected static class SearchPath extends NodeJSDistributionResolver {

        SearchPath(def lazyPath) {
            this.lazyPath = lazyPath
        }

        ResolvedDistribution resolve(Project project) {

            final String path = StringUtils.stringize(lazyPath)
            final File foundPath = OS.findInPath(path)

            if(foundPath == null) {
                throw new GradleException("Cannot locate '${path}' in system search path/")
            }

            return new ResolvedDistribution() {
                @Override
                File getNodeExecutable() {
                    foundPath
                }
            }
        }

        private Object lazyPath

    }
}
