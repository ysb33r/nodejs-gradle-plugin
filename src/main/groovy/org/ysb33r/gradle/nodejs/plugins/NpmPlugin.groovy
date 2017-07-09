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

package org.ysb33r.gradle.nodejs.plugins

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecSpecInstantiator
import org.ysb33r.gradle.nodejs.impl.npm.NpmSelfResolvingDependency
import org.ysb33r.gradle.olifant.ExtensionUtils

/**
 *
 * @since 0.1
 */
@CompileStatic
class NpmPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply plugin : 'org.ysb33r.nodejs.base'

        project.extensions.create(NpmExtension.NAME,NpmExtension,project)

        addNpmExecExtension(project)
        addNpmPackageExtension(project)
        addConfigurations(project)
    }

    void addNpmExecExtension(Project project) {
        ExtensionUtils.addExecProjectExtension('npmexec',project,NpmExecSpecInstantiator.INSTANCE)
    }

    void addConfigurations(Project project) {
        project.configurations.create('npm')
    }

    void addNpmPackageExtension(Project project) {
        project.extensions.extraProperties.set('npmPackage', { Map<String,Object> spec ->
            new NpmSelfResolvingDependency(project,spec)
        })
    }
}
