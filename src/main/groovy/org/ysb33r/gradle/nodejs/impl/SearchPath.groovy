package org.ysb33r.gradle.nodejs.impl

import org.gradle.api.GradleException
import org.ysb33r.gradle.olifant.OperatingSystem
import org.ysb33r.gradle.olifant.StringUtils

/** Searches the operating system path for an executable
 *
 * @since 0.1
 */
class SearchPath {
    static private final OperatingSystem OS = OperatingSystem.current()

    /** Search path for executable
     *
     * @param path Any executable name supported by {@code org.ysb33r.gradle.olifant.StringUtils}.
     * @return Location of executable
     * @throw {@code GradleException} if not found
     */
    static File forExecutable(final Object lazyPath) {
        final String path = StringUtils.stringize(lazyPath)
        final File foundPath = OS.findInPath(path)

        if(foundPath == null) {
            throw new GradleException("Cannot locate '${path}' in system search path/")
        }

        foundPath
    }
}
