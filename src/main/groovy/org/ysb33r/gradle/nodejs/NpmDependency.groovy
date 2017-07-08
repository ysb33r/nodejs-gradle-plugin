//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2017
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//

package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import org.gradle.api.GradleException

/** Creates an NPM dependency
 * @since 0.1
 */
@CompileStatic
class NpmDependency implements  NpmPackageDescriptor {

    NpmDependency(final String name, final String version ) {
        this.scope = null
        this.name= name
        this.tag = version
    }

    NpmDependency(final String scope, final String name, final String version ) {
        this.scope = scope
        this.name= name
        this.tag = version
    }

    /** Allows additional parameters to be set
     *
     * <ul>
     *     <li> {@code scope} - The scope if needed
     *     <li> {@code name} - The package name
     *     <li> {@code tag} - The tag/version
     *     <li> {@code type} - One of {@code prod, dev, optional}
     * </ul>
     *
     * @param project
     * @param properties
     */
    NpmDependency(final Map<String,?> properties) {
        List checkKeys = []
        checkKeys.addAll(properties.keySet())
        checkKeys.removeAll(VALID_KEYS)
        if (!checkKeys.empty) {
            throw new GradleException("Invalid properties specified for NPM dependency: ${checkKeys.join(', ')}")
        }

        if (properties['name']) {
            this.name = (String)(properties['name'])
        } else {
            throw new GradleException( "No package name specified in ${properties}")
        }

        if (properties['tag']) {
            this.tag = (String)(properties['tag'])
        } else {
            throw new GradleException( "A tag name is required in ${properties}. 'latest' or '+' can also be specified")
        }

        if(properties['scope']) {
            this.scope = (String)(properties['scope'])
        }

        if(properties['type']) {
            if(properties['type'] instanceof NpmDependencyGroup) {
                installGroup = (NpmDependencyGroup)(properties['type'])
            } else {
                switch((String)(properties['type'])) {
                    case 'prod':
                        installGroup = NpmDependencyGroup.PRODUCTION
                        break
                    case 'dev':
                        installGroup = NpmDependencyGroup.DEVELOPMENT
                        break
                    case 'optional':
                        installGroup = NpmDependencyGroup.OPTIONAL
                        break
                    default:
                        throw new GradleException('Only prod, dev or optional can be used to quality type')
                }
            }
        }
    }


    /** Name of the package without tags
     *
     * @return NPM name
     */
    @Override
    String getPackageName() {
        this.name
    }

    /** Name of NPM tag
     *
     * @return NPM tag (or {@code null} if not defined).
     */
    @Override
    String getTagName() {
        return this.tag
    }

    /** Package scope
     *
     * @return NPM scope  (or {@code null} if not defined).
     */
    @Override
    String getScope() {
        this.scope
    }

    /** NPM installation group
     *
     * @return {@link NpmDepencyGroup}
     */
    NpmDependencyGroup getInstallGroup() {
        this.installGroup
    }

    @Override
    String toString() {
        scope ? "@${scope}/${name}@${tag}" : "${name}@${tag}"
    }

    private final String scope
    private final String name
    private final String tag
    private final NpmDependencyGroup installGroup = NpmDependencyGroup.PRODUCTION

    static private  List<String> VALID_KEYS = [ 'scope', 'name', 'tag', 'type']

}
