package org.ysb33r.gradle.nodejs.impl

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.ResolvedExecutable
import org.ysb33r.gradle.olifant.StringUtils

/**
 *
 * @since 0.1
 */
@CompileStatic
abstract class NpmResolver {

    static final Map<String,Object> SEARCH_PATH = [ search : 'npm' ] as Map<String,Object>

    /** Returns the path to the resolved {@code npm} executable.
     *
     * @return A resolved file object
     */
    abstract ResolvedExecutable resolve(Project project)

    static NpmResolver createFromOptions(final Map<String, ?> opts) {
        Number keyCount = opts.keySet().count { String it ->
            it == 'path' || it == 'search' || it == 'version' || it == 'default'
        }

        if (keyCount > 1) {
            throw new GradleException('''Combining use of 'default', 'path', 'search' and 'version' is not valid.''')
        }
        if (opts['version']) {
            return new Version(opts['version'])
        }
        if (opts['path']) {
            return new Path(opts['path'])
        }
        if (opts['search']) {
            return new SearchPath(opts['search'])
        }
        if (opts['default']) {
            return new NodeDefault((NodeJSExtension)(opts['default']))
        }

    }

    private static class Version extends NpmResolver {
        Version(def lazyVersion) {
            this.lazyVersion = lazyVersion
        }

        ResolvedExecutable resolve(Project project) {
//            Downloader dnl = new Downloader(StringUtils.stringize(lazyVersion),project)
//
//            return new ResolvedExecutable() {
//
//                @Override
//                File getExecutable() {
//                    dnl.getNodeExecutablePath()
//                }
//            }
            null
        }

        private Object lazyVersion
    }

    private static class Path extends NpmResolver {
        Path(def lazyPath) {
            this.lazyPath = lazyPath
        }

        ResolvedExecutable resolve(Project project) {
            File resolvedPath = project.file(lazyPath)

            return new ResolvedExecutable() {
                @Override
                File getExecutable() {
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

    private static class SearchPath extends NpmResolver {
        SearchPath(def lazyPath) {
            this.lazyPath = lazyPath
        }

        ResolvedExecutable resolve(Project project) {

            return new ResolvedExecutable() {
                @Override
                File getExecutable() {
                    org.ysb33r.gradle.nodejs.impl.SearchPath.forExecutable(lazyPath)
                }
            }
        }

        private Object lazyPath
    }

    private static class NodeDefault extends NpmResolver {

        NodeDefault(NodeJSExtension ext) {
            this.ext = ext
        }
        @Override
        ResolvedExecutable resolve(Project project) {
            ext.resolvedNpmExecutable
        }

        private NodeJSExtension ext
    }
}
