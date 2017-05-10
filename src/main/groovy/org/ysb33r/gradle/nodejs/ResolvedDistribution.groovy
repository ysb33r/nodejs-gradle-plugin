package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic

/** Holds a reference to a resolved NodeJS distribution
 *
 * @since 0.1
 */
@CompileStatic
interface ResolvedDistribution {

    /** Location of the {@code node} executable.
     *
     * @return Full path to {@code node} or @{code node.exe}
     */
    File getNodeExecutable()
}