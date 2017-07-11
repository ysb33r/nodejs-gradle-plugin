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

package org.ysb33r.gradle.nodejs.helper

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class UnittestBaseSpecification extends Specification {

    final String NODEJS_VERSION = System.getProperty('NODEJS_VERSION') ?: '7.10.0'

    Project project = ProjectBuilder.builder().build()

    void setup() {
        project.allprojects {
            apply plugin : 'org.ysb33r.nodejs.npm'

            nodejs {
                executable version : NODEJS_VERSION
            }
        }
    }


}