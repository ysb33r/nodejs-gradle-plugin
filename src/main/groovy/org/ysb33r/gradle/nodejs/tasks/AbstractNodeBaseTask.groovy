package org.ysb33r.gradle.nodejs.tasks

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExtension

/** A base class that will provide NodeJS and NPM task extensions.
 *
 * @since 0.1
 */
@CompileStatic
class AbstractNodeBaseTask extends DefaultTask {

    protected AbstractNodeBaseTask() {
        super()
        npmExtension = (NpmExtension)(extensions.create(NpmExtension.NAME,NpmExtension,this))
        nodeExtension = (NodeJSExtension)(extensions.create(NodeJSExtension.NAME,NodeJSExtension,this))
    }

    /** Internal access to attached NPM extension.
     *
      * @return NPM extension.
     */
    protected NpmExtension getNpmExtension() {
        this.npmExtension
    }

    /** Internal access to attached NodeJS extension.
     *
     * @return NodeJS extension.
     */
    protected NodeJSExtension getNodeExtension() {
        this.nodeExtension
    }

    private final NpmExtension npmExtension
    private final NodeJSExtension nodeExtension

}
